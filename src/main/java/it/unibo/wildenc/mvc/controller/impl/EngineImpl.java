package it.unibo.wildenc.mvc.controller.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.joml.Vector2d;

import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.Game;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.game.GameImpl;
import it.unibo.wildenc.mvc.model.Game.PlayerType;
import it.unibo.wildenc.mvc.model.map.GameMapImpl;
import it.unibo.wildenc.mvc.view.api.GameView;

public class EngineImpl implements Engine{
    private final Game gm = new GameImpl(PlayerType.Charmender);
    private final List<GameView> views;
    private final GameLoop loop = new GameLoop();

    public EngineImpl() {
        views = new LinkedList<>();
    }

    @Override
    public void startGameLoop() {
        this.loop.start();    
    }

    @Override
    public void registerView(GameView gv) {
        gv.setEngine(this);
        gv.start();
        views.add(gv);
    }

    private Collection<MapObjViewData> getObjectViewData(Collection<MapObject> c) {
        return c.stream()
            .map(mapObj -> {
                Optional<Double> dirX = Optional.empty();
                Optional<Double> dirY = Optional.empty();

                if(mapObj instanceof Entity entity) {
                    dirX = Optional.of(entity.getDirection().x());
                    dirY = Optional.of(entity.getDirection().y());
                }

                return new MapObjViewData(
                    mapObj.getName(), 
                    mapObj.getPosition().x(), 
                    mapObj.getPosition().y(), 
                    dirX, 
                    dirY
                );
            }) 
            .toList();
    }

    public final class GameLoop extends Thread {
        private static final long SLEEP_TIME = 20;

        @Override
        public void run() {
            while (true) {
                /* update view */
                views.forEach(v -> v.updateSprites(
                    getObjectViewData(Stream.concat(Stream.of(gm.getPlayer()), gm.getAllObjects().stream()).toList())));
                final long timeUpdateView = System.nanoTime();
                /* get command and sleep */
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                /* update model and other */
                gm.updateEntities(System.nanoTime() - timeUpdateView, new Vector2d(0, 1));
                /* update enemys in model */
            }
        }
    }

}
