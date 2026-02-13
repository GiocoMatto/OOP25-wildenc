package it.unibo.wildenc.mvc.controller.impl;

import java.util.Collections;
import java.util.HashSet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import org.joml.Vector2d;
import org.joml.Vector2dc;
import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.mvc.controller.api.InputHandler;
import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import it.unibo.wildenc.mvc.controller.api.SavedData;
import it.unibo.wildenc.mvc.controller.api.SavedDataHandler;
import it.unibo.wildenc.mvc.controller.api.InputHandler.MovementInput;
import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.Game;
import it.unibo.wildenc.mvc.model.Lobby.PlayerType;
import it.unibo.wildenc.mvc.model.Game.WeaponChoice;
import it.unibo.wildenc.mvc.model.Lobby;
import it.unibo.wildenc.mvc.model.Game.PlayerInfos;
import it.unibo.wildenc.mvc.model.game.GameImpl;
import it.unibo.wildenc.mvc.model.lobby.LobbyImpl;
import it.unibo.wildenc.mvc.model.weaponary.weapons.PointerWeapon;
import it.unibo.wildenc.mvc.view.api.GamePointerView;
import it.unibo.wildenc.mvc.view.api.GameView;

/**
 * {@inheritDoc}.
 */
public class EngineImpl implements Engine {
    //Set per i movimenti attivi, non piu queue
    private final Set<MovementInput> activeMovements = Collections.synchronizedSet(new HashSet<>());
    private final List<GameView> views = Collections.synchronizedList(new ArrayList<>());
    private final SavedDataHandler dataHandler = new SavedDataHandlerImpl();
    private GameLoop loop;
    private final Object pauseLock = new Object();
    private volatile STATUS gameStatus;
    private volatile Game model;
    private final Lobby lobby = new LobbyImpl();
    private Lobby.PlayerType playerType;
    private SavedData data;
    private final InputHandler ih = new InputHandlerImpl();

    /**
     * The status of the game loop.
     */
    public enum STATUS { RUNNING, PAUSE, END }

    @Override
    public void start() {
        playerType = lobby.getSelectablePlayers().getFirst();
        this.views.forEach(e -> e.start(playerType));
    }

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

    private void setPause(final boolean status) {
        synchronized (pauseLock) {
            this.gameStatus = status ? STATUS.PAUSE : STATUS.RUNNING;
            pauseLock.notifyAll();
        }
    }

    private void open(final Consumer<GameView> c) {
        setPause(true);
        this.views.forEach(c::accept);
    }

    private void close(final Consumer<GameView> c) {
        this.views.forEach(c::accept);
        setPause(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(final Game.PlayerType pt) {
        playerType = pt;
        this.views.forEach(e -> e.start(pt));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startGameLoop() {
        this.model = new GameImpl(playerType);
        this.views.forEach(v -> v.switchRoot(v.game()));
        this.loop = new GameLoop();
        this.loop.setDaemon(true);
        this.loop.start();
        this.gameStatus = STATUS.RUNNING;
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
        this.model.choosenWeapon(choise);
        this.views.forEach(v -> v.playSound("levelUp"));
        close(GameView::closePowerUp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pokedex() {
        this.views.forEach(e -> e.switchRoot(e.pokedexView(data.getPokedex())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        gameStatus = STATUS.END;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void menu(final Lobby.PlayerType pt) {
        this.playerType = pt;
        this.views.forEach(e -> e.switchRoot(e.menu(pt)));
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
     * {@inheritDoc}
     */
    @Override
    public List<Lobby.PlayerType> getSelectablePlayers() {
        return lobby.getSelectablePlayers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerType getPlayerTypeChoise() {
        return this.playerType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void openViewPause() {
        open(e -> {
            e.pause();
            e.pauseMusic();
        });
    }

    /**
     * {@hineritDoc}
     */
    @Override
    public void closeViewPause() {
        close(e -> {
            e.closePause();
            e.resumeMusic();
        });
    }

    private void saveAllData() {
        try {
            model.getGameStatistics()
                .entrySet()
                .stream()
                .forEach(entry -> data.updatePokedex(entry.getKey(), entry.getValue()));
            data.updateCoins(model.getPlayerInfos().coins());
            dataHandler.saveData(data);
        } catch (final IOException e) { }
    }

    /**
     * The game loop.
     */
    public final class GameLoop extends Thread {
        private static final long SLEEP_TIME = 20;
        private static final long DIVISOR = 1_000_000;
        private static final long LIMIT = 350;

        @Override
        public void run() {
            try {
                //variabili per i suoni
                long lastStepTime = 0; //per il ritmo dei passi
                int lastExp = 0;

                if (model != null) {
                    lastExp = model.getPlayer().getExp();
                }
                long lastTime = System.nanoTime();
                while (STATUS.END != gameStatus) {
                    synchronized (pauseLock) {
                        while (gameStatus == STATUS.PAUSE) {
                            pauseLock.wait();
                            lastTime = System.nanoTime();
                        }
                    }
                    final long now = System.nanoTime();
                    final long dt = now - lastTime;
                    lastTime = now;
                    //passo il nuovo vettore calcolato
                    model.updateEntities(dt, ih.handleMovement(activeMovements));

                    if (!activeMovements.isEmpty()) {
                        // Suona ogni 350ms per simulare il passo
                        if ((now - lastStepTime) / DIVISOR > LIMIT) {
                            views.forEach(v -> v.playSound("walk"));
                            lastStepTime = now;
                        }
                    }
                    final int currentExp = model.getPlayer().getExp();

                    if (currentExp != lastExp) {
                        views.forEach(v -> v.playSound("collect"));
                        lastExp = currentExp;
                    }

                    final PlayerInfos playerInfos = model.getPlayerInfos();
                    if (model.hasPlayerLevelledUp()) {
                        open(e -> e.openPowerUp(model.weaponToChooseFrom()));
                    }
                    if (model.isGameEnded()) {
                        saveAllData();
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
                            return new MapObjViewData(
                                mapObj.getName(), 
                                mapObj.getPosition().x(), 
                                mapObj.getPosition().y(), 
                                mapObj.getHitbox(),
                                (mapObj instanceof Entity e) 
                                ? Optional.of(e.getDirection().x()) 
                                : Optional.empty(), 
                                (mapObj instanceof Entity e) 
                                ? Optional.of(e.getDirection().y()) 
                                : Optional.empty()
                            );
                        })
                        .toList();
                    views.stream()
                        .forEach(view -> {
                            view.updateSprites(mapDataColl);
                            view.updateExpBar(
                                (int) model.getPlayer().getCurrentHealth(), 
                                playerInfos.level(), 
                                (int) model.getPlayer().getMaxHealth()
                            );
                        });
                    sleep(SLEEP_TIME);
                }
            } catch (final InterruptedException e) {
                System.out.println(e.toString());
                currentThread().interrupt();
            }
        }
    }

}
