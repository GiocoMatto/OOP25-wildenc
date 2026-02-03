package it.unibo.wildenc.mvc.controller.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.joml.Vector2d;

import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import it.unibo.wildenc.mvc.model.Enemy;
import it.unibo.wildenc.mvc.model.GameMap;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.Player;
import it.unibo.wildenc.mvc.model.GameMap.PlayerType;
import it.unibo.wildenc.mvc.model.map.GameMapImpl;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.Projectile;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats.ProjStatType;
import it.unibo.wildenc.mvc.view.api.GameView;

public class EngineImpl implements Engine{
    private final GameMap gm = new GameMapImpl(PlayerType.Charmender);
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
            .map(e -> new MapObjViewData(e.getName(), e.getPosition().x(), e.getPosition().y()))
            .toList();
    }

    public final class GameLoop extends Thread {
        private static final long SLEEP_TIME = 10;

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
                gm.updateEntities(System.nanoTime() - timeUpdateView, new Vector2d(0, 0));
                /* update enemys in model */
            }
        }

    }

}
