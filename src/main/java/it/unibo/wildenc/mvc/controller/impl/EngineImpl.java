package it.unibo.wildenc.mvc.controller.impl;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.LockSupport;
import org.joml.Vector2d;
import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import it.unibo.wildenc.mvc.controller.api.InputHandler.MovementInput;
import it.unibo.wildenc.mvc.model.GameMap;
import it.unibo.wildenc.mvc.model.map.GameMapImpl;
import it.unibo.wildenc.mvc.view.api.GameView;
import it.unibo.wildenc.mvc.view.impl.GameViewImpl;

public class EngineImpl implements Engine{
    private final LinkedBlockingQueue<MovementInput> movements = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<String> upgrade = new LinkedBlockingQueue<>();
    private final GameView view = new GameViewImpl();
    private final GameLoop loop = new GameLoop();
    private final GameMap model;
    private STATUS status = STATUS.GAME;
    private boolean running = true;

    public enum STATUS {GAME, PAUSE;}

    public EngineImpl(final GameMap.PlayerType playerType) {
        model = new GameMapImpl(playerType);
        view.setEngine(this);
        view.start();
    }

    public void startGameLoop() {
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
        this.upgrade.add(choise);
    }


    public final class GameLoop extends Thread {
        private static final long SLEEP_TIME = 10;

        @Override
        public void run() {
            long lastTime = System.nanoTime();
            while (running) {
                final long now = System.nanoTime();
                final long dt = now - lastTime;
                lastTime = now;
                /* condition game state */
                view.updateSprites(model.getAllObjects().stream()
                    .map(e -> new MapObjViewData(
                        "null", 
                        e.getPosition().x(), 
                        e.getPosition().y()
                    ))
                    .iterator()
                );
                LockSupport.parkNanos(SLEEP_TIME);
                // TODO: fare controllo se aggiungere un arma ogni livello. Nel model 
                // bisognerebbe aggiungere se è il player ha livellato.
                /**
                 * if (model.levelUp() {
                 *  status = STATUS.PAUSE;
                 *  view.levelUp();
                 * }
                 *//*
                 * if (model.end()) {
                 *  view.end();
                 *  running = false;
                 * }
                 */
                final var levUp = upgrade.poll();
                if (levUp != null) {
                    //model.aggiornastastisticheplayer.
                    status = STATUS.GAME;
                }
                if (status == STATUS.GAME) {
                    final var move = movements.poll();
                    model.updateEntities(dt, (move != null) ? move.getVector() : new Vector2d(0, 0));
                }
            }
        }

    }

}
