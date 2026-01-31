package it.unibo.wildenc.mvc.controller.impl;

import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.mvc.model.GameMap;
import it.unibo.wildenc.mvc.model.map.GameMapImpl;

public class EngineImpl implements Engine{
    // a field for the view
    private final GameMap gm;
    private final GameLoop loop = new GameLoop();

    public EngineImpl() {
        gm = new GameMapImpl(null);
        
    }

    @Override
    public void startGameLoop() {
        this.loop.start();    
    }

    public final class GameLoop extends Thread {
        private static final long SLEEP_TIME = 10;

        @Override
        public void run() {
            /* update view */
            final long timeUpdateView = System.nanoTime();
            /* get command and sleep */
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            /* update model and other */
            gm.updateEntities(System.nanoTime() - timeUpdateView);
            /* update enemys in model */
        }

    }

}
