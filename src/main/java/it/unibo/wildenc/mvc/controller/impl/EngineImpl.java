package it.unibo.wildenc.mvc.controller.impl;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import org.joml.Vector2d;
import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import it.unibo.wildenc.mvc.controller.api.SavedData;
import it.unibo.wildenc.mvc.controller.api.SavedDataHandler;
import it.unibo.wildenc.mvc.controller.api.InputHandler.MovementInput;
import it.unibo.wildenc.mvc.model.GameMap;
import it.unibo.wildenc.mvc.model.GameMap.PlayerType;
import it.unibo.wildenc.mvc.model.map.GameMapImpl;
import it.unibo.wildenc.mvc.view.api.GameView;
import it.unibo.wildenc.mvc.view.impl.GameViewImpl;

public class EngineImpl implements Engine{
    private final LinkedBlockingQueue<MovementInput> movements = new LinkedBlockingQueue<>();
    private final SavedDataHandler dataHandler = new SavedDataHandlerImpl();
    private final GameView view = new GameViewImpl();
    private final GameLoop loop = new GameLoop();
    private final Object pauseLock = new Object();
    private volatile STATUS gameStatus = STATUS.RUNNING;
    private volatile GameMap model;
    private PlayerType playerType;
    private SavedData data;
    
    public enum STATUS {RUNNING, PAUSE;}
    
    public EngineImpl() {
        try {
            this.data = dataHandler.loadData();
        } catch (final ClassNotFoundException | IOException e) {
            this.data = new SavedDataImpl();
        }
        view.setEngine(this);
        view.start();
    }

    @Override
    public void startGameLoop() {
        model = new GameMapImpl(playerType);
        this.loop.start();
    }

    /**
     * {@inheritDoc}
    */
    @Override
    public void processInput(final MovementInput movement) {
        this.movements.add(movement);
    }

    @Override
    public void onLeveUpChoise(String choise) {
        //model.aggiornastastisticheplayer;
        setPause(false);
    }

    @Override
    public void setPause(boolean status) {
        synchronized (pauseLock) {
            this.gameStatus = status ? STATUS.PAUSE : STATUS.RUNNING;
            pauseLock.notifyAll();
        }
    }

    @Override
    public void chosePlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    @Override
    public void Pokedex() {
        view.pokedexView(data.getPokedex());
    }

    @Override
    public void close() {
        try {
            this.dataHandler.saveData(data);
        } catch (final IOException e) {
            
        }
    }

    public final class GameLoop extends Thread {
        private static final long SLEEP_TIME = 10;
        private boolean running = true;

        @Override
        public void run() {
            try {
                long lastTime = System.nanoTime();
                while (running) {
                    synchronized (pauseLock) {
                        while (gameStatus == STATUS.PAUSE) {
                            pauseLock.wait();
                        }
                    }
                    final long now = System.nanoTime();
                    final long dt = now - lastTime;
                    lastTime = now;
                    final var move = movements.poll();
                    model.updateEntities(dt, (move != null) ? move.getVector() : new Vector2d(0, 0));
                    /**
                     * if (model.levelUp() {
                     *  setPause(true);
                     *  view.levelUp(model.getLevelUp());
                     * }
                    */
                    /*
                    * if (model.end()) {
                    *  view.end(model.getStatistic());
                    *  running = false;
                    * }
                    */
                    view.updateSprites(model.getAllObjects().stream()
                        .map(e -> new MapObjViewData(
                            "name", 
                            e.getPosition().x(), 
                            e.getPosition().y()
                        ))
                        .iterator()
                    );
                    Thread.sleep(SLEEP_TIME);
                }
            } catch (InterruptedException e1) {
                Thread.currentThread().interrupt();
            }
        }

    }

    @Override
    public void menu() {
        //view.menu();
    }

    @Override
    public void shop() {
        //view.shop();
    }

}
