package it.unibo.wildenc.mvc.controller.impl;

import java.util.concurrent.LinkedBlockingQueue;
import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import it.unibo.wildenc.mvc.controller.api.InputHandler.MovementInput;
import it.unibo.wildenc.mvc.model.GameMap;
import it.unibo.wildenc.mvc.model.map.GameMapImpl;
import it.unibo.wildenc.mvc.view.api.GameView;
import it.unibo.wildenc.mvc.view.impl.GameViewImpl;

public class EngineImpl implements Engine{
    private final LinkedBlockingQueue<MovementInput> movements = new LinkedBlockingQueue<>();
    private final GameView view = new GameViewImpl();
    private final GameLoop loop = new GameLoop();
    private final GameMap model;

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

    public final class GameLoop extends Thread {
        private static final long SLEEP_TIME = 10;

        @Override
        public void run() {
            while (true) {
                final long timeUpdateView = System.nanoTime();
                /* condition game state */
                view.updateSprites(model.getAllObjects().stream()
                    .map(e -> new MapObjViewData(
                        "null", 
                        e.getPosition().x(), 
                        e.getPosition().y()
                    ))
                    .iterator()
                );
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {}
                model.updateEntities(System.nanoTime() - timeUpdateView, movements.poll().getVector());
            }
        }

    }

}
