package it.unibo.wildenc.mvc.model.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import it.unibo.wildenc.mvc.model.Enemy;
import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.GameMap;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.Movable;
import it.unibo.wildenc.mvc.model.Player;
import it.unibo.wildenc.mvc.model.Projectile;

/**
 * Basic {@link Map} implementation
 * 
 */
public class GameMapImpl implements GameMap {

    private static final double NANO_TO_SECOND_FACTOR = 1_000_000_000.0;

    private final Player player;
    private final List<MapObject> mapObjects = new ArrayList<>();

    /**
     * Create a new basic map.
     * 
     * @param p
     *          the player.
     */
    public GameMapImpl(Player p) {
        player = p;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addObject(final MapObject mObj) {
        mapObjects.add(mObj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeObject(final MapObject mObj) {
        return mapObjects.remove(mObj);
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MapObject> getAllObjects() {
        return Collections.unmodifiableList(mapObjects);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEntities(final long deltaTime) {
        final double deltaSeconds = deltaTime / NANO_TO_SECOND_FACTOR;
        List<MapObject> objToRemove = new LinkedList<>();
        /*
         * Update objects positions
         */
        Stream.concat(Stream.of(player), mapObjects.stream())
            .filter(e -> e instanceof Movable)
            .map(o -> (Movable)o)
            .peek(o -> {
                System.out.println(o.getClass() + " x: " + o.getPosition().x() + " y: " + o.getPosition().y()); // FIXME: think about better logging
                if (o instanceof Entity e) {
                    System.out.println("health: " + e.getCurrentHealth());  // FIXME: think about better logging
                }
            })
            .forEach(o -> o.updatePosition(deltaSeconds));
        /*
         * Check collisions of projectiles with player 
         */
        mapObjects.stream()
            .filter(e -> e instanceof Projectile)
            .map(o -> (Projectile)o)
            .filter(p -> p.getOwner() instanceof Enemy) // check only Projectiles shot by enemies
            .filter(o -> CollisionLogic.areColliding(player, o))
            .forEach(o -> projectileHit(o, player, objToRemove));
        /*
         * Check collision of projectiles with enemies
         */ 
        List<Projectile> projectiles = getAllObjects().stream()
            .filter(e -> e instanceof Projectile)
            .map(e -> (Projectile) e)
            .filter(p -> p.getOwner() instanceof Player)
            .toList();
        List<Enemy> enemies = getAllObjects().stream()
            .filter(e -> e instanceof Enemy)
            .map(e -> (Enemy) e)
            .toList();
        projectiles.stream()
            .forEach(p -> {
                enemies.stream()
                    .filter(e -> CollisionLogic.areColliding(e, p))
                    .findFirst()
                    .ifPresent(e -> projectileHit(p, e, objToRemove));
        });
        // remove used objects
        mapObjects.removeAll(objToRemove);
    }

    private void projectileHit(Projectile p, Entity e, List<MapObject> toRemove) {
        if (!e.canTakeDamage()) { 
            return;
        }
        System.out.println("!!!!!! Projectile hit !!!!!!");  // FIXME: better logging
        e.takeDamage((int) p.getDamage()); // FIXME: avoidable cast
        toRemove.add(p);
        if (e.getCurrentHealth() <= 0) {
            System.out.println(e.getClass().toString() + " died!!!");  // FIXME: think about better logging
            toRemove.add(e);
        }
    }
}
