package it.unibo.wildenc.mvc.controller.impl;

import java.util.Collections;
import java.util.HashSet;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import it.unibo.wildenc.mvc.controller.api.SavedData;
import it.unibo.wildenc.mvc.controller.api.SavedDataHandler;
import it.unibo.wildenc.mvc.controller.api.InputHandler.MovementInput;
import it.unibo.wildenc.mvc.controller.api.InputHandler;
import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.Game;
import it.unibo.wildenc.mvc.model.Game.WeaponChoice;
import it.unibo.wildenc.mvc.model.game.GameImpl;
import it.unibo.wildenc.mvc.model.weaponary.weapons.PointerWeapon;
import it.unibo.wildenc.mvc.view.api.GamePointerView;
import it.unibo.wildenc.mvc.view.api.GameView;

/**
 * {@inheritDoc}.
 */
public class EngineImpl implements Engine {
    //Set per i movimenti attivi, non piu queue
    private final Set<MovementInput> activeMovements = Collections.synchronizedSet(new HashSet<>());
    private final SavedDataHandler dataHandler = new SavedDataHandlerImpl();
    private final List<GameView> views = new LinkedList<>();
    private final GameLoop loop = new GameLoop();
    private final Object pauseLock = new Object();
    private volatile STATUS gameStatus = STATUS.RUNNING;
    private volatile Game model;
    private Game.PlayerType playerType;
    private SavedData data;
    private final InputHandler ih = new InputHandlerImpl();

    /**
     * The status of the game loop.
     */
    public enum STATUS { RUNNING, PAUSE, END }

    /**
     * Create a Engine.
     */
    public EngineImpl() {
        try {
            this.data = dataHandler.loadData();
        } catch (final ClassNotFoundException | IOException e) {
            this.data = new SavedDataImpl();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startGameLoop() {
        chosePlayerType(Game.PlayerType.CHARMANDER);
        model = new GameImpl(playerType);
        this.loop.setDaemon(true);
        this.loop.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addInput(final MovementInput movement) {
        activeMovements.add(movement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeInput(final MovementInput movement) {
        activeMovements.remove(movement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAllInput() {
        activeMovements.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLeveUpChoise(final String choise) {
        this.model.choosenWeapon(new WeaponChoice(choise));
        setPause(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPause(final boolean status) {
        synchronized (pauseLock) {
            this.gameStatus = status ? STATUS.PAUSE : STATUS.RUNNING;
            pauseLock.notifyAll();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void chosePlayerType(final Game.PlayerType pt) {
        this.playerType = pt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pokedex() {
        this.views.forEach(e -> e.pokedexView(data.getPokedex()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        try {
            this.dataHandler.saveData(data);
            gameStatus = STATUS.END;
        } catch (final IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void menu() {
        this.views.forEach(e -> e.menu());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shop() {
        this.views.forEach(e -> e.shop());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerView(final GameView gv) {
        this.views.add(gv);
        gv.setEngine(this);
        gv.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterView(final GameView gv) {
        this.views.remove(gv);
        if (this.views.isEmpty()) {
            this.close();
        }
    }

    /**
     * The game loop.
     */
    public final class GameLoop extends Thread {
        private static final long SLEEP_TIME = 20;

        @Override
        public void run() {
            try {
                long lastTime = System.nanoTime();
                while (STATUS.RUNNING == gameStatus) {
                    synchronized (pauseLock) {
                        while (gameStatus == STATUS.PAUSE) {
                            pauseLock.wait();
                        }
                    }
                    final long now = System.nanoTime();
                    final long dt = now - lastTime;
                    lastTime = now;
                    //passo il nuovo vettore calcolato
                    model.updateEntities(dt, ih.handleMovement(activeMovements));
                    if (model.hasPlayerLevelledUp()) {
                        setPause(true);
                        final var levelUpChoise = model.weaponToChooseFrom();
                        views.forEach(e -> e.powerUp(levelUpChoise));
                    }
                    if (model.isGameEnded()) {
                        views.forEach(e -> e.lost(model.getGameStatistics()));
                        gameStatus = STATUS.END;
                    }
                    final Vector2dc currentMousePos = views.stream()
                        .filter(view -> view instanceof GamePointerView)
                        .map(view -> (GamePointerView) view)
                        .findFirst()
                        .orElse(() -> new Vector2d(0, 0))
                        .getMousePointerInfo();
                    model.getAllMapObjects().stream()
                        .filter(o -> o instanceof Entity)
                        .map(e -> (Entity) e)
                        .flatMap(e -> e.getWeapons().stream())
                        .filter(w -> w instanceof PointerWeapon)
                        .forEach(wp -> {
                            ((PointerWeapon) wp).setPosToHit(() -> currentMousePos);
                        });
                    final Collection<MapObjViewData> mapDataColl = model.getAllMapObjects().stream()
                        .map(mapObj -> {
                            if (mapObj instanceof Entity e) {
                                return new MapObjViewData(
                                    mapObj.getName(), 
                                    mapObj.getPosition().x(), 
                                    mapObj.getPosition().y(), 
                                    mapObj.getHitbox(),
                                    Optional.of(e.getDirection().x()), 
                                    Optional.of(e.getDirection().y())
                                );
                            } else {
                                return new MapObjViewData(
                                    mapObj.getName(),
                                    mapObj.getPosition().x(),
                                    mapObj.getPosition().y(),
                                    mapObj.getHitbox(),
                                    Optional.empty(), Optional.empty()
                                );
                            }
                        })
                        .toList();
                    views.stream()
                        .forEach(view -> view.updateSprites(mapDataColl));
                    Thread.sleep(SLEEP_TIME);
                }
            } catch (final InterruptedException e) {
                System.out.println(e.toString());
                Thread.currentThread().interrupt();
            }
        }
    }

}
