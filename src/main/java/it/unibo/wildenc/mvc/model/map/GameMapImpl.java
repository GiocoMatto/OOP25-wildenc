package it.unibo.wildenc.mvc.model.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.jetbrains.annotations.TestOnly;
import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Collectible;
import it.unibo.wildenc.mvc.model.Enemy;
import it.unibo.wildenc.mvc.model.EnemySpawner;
import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.GameMap;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.Movable;
import it.unibo.wildenc.mvc.model.Player;
import it.unibo.wildenc.mvc.model.player.PlayerImpl;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.Projectile;
import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponFactory;

/**
 * Basic {@link Map} implementation
 * 
 */
public class GameMapImpl implements GameMap {

    private static final double NANO_TO_SECOND_FACTOR = 1_000_000_000.0;

    private final Player player;
    private final List<MapObject> mapObjects = new ArrayList<>();
    private EnemySpawner es;

    private Player getPlayerByPlayerType(PlayerType p) {
        var playerStats = p.getPlayerType();
        Player player = new PlayerImpl(new Vector2d(0, 0), playerStats.hitbox(), playerStats.speed(), playerStats.health());
        playerStats.addDefaultWeapon().accept(new WeaponFactory(), player);
        return player;
    }

    /** 
     * Create a new map.
     * 
     * @param p the player.
     */
    public GameMapImpl(PlayerType p) {
        player = getPlayerByPlayerType(p);
    }

    /** 
     * Create a new map with custom EnemySpawner and initial objects on Map.
     * 
     * @param p the player.
     */
    @TestOnly
    GameMapImpl(Player p, EnemySpawner es, Set<MapObject> initialObjs) {
        player = p;
        setEnemySpawnLogic(es);
        addAllObjects(initialObjs);
    }

    /**
     * Add a {@link MapObject} on this Map.
     * 
     * @param mObj 
     *              the {@link MapObject} to add
     */
    protected void addObject(final MapObject mObj) {
        mapObjects.add(mObj);
    }

    /**
     * Add every {@link MapObject} inside of a {@link Collection} to the GameMap.
     * 
     * @param mObjs the objects to add.
     */    
    protected void addAllObjects(final Collection<? extends MapObject> mObj) {
        mObj.forEach(this::addObject);
    }

    /**
     * Remove a {@link MapObject} from this Map.
     * 
     * @param mObj 
     *              the {@link MapObject} to remove
     * @return
     *              true if the {@link MapObject} was removed successfully
     */
    protected boolean removeObject(final MapObject mObj) {
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
    public void updateEntities(final long deltaTime, final Vector2dc playerDirection) {
        final double deltaSeconds = deltaTime / NANO_TO_SECOND_FACTOR;
        List<MapObject> objToRemove = new LinkedList<>();
        /**
         * Update player
         */
        player.setDirection(playerDirection);
        log(player);
        player.updatePosition(deltaSeconds);

        /*
         * Update objects positions
         */
        updateObjectPositions(deltaSeconds);
        /*
         * Check collisions of projectiles with player 
         */
        checkPlayerHits(objToRemove);
        /*
         * Check collision of projectiles with enemies
         */ 
        checkEnemyHits(objToRemove);
        /*
         * Check Collectibles
         */
        checkCollectibles(objToRemove);
        //
        handleAttacks(deltaSeconds);
        // Spawn enemies by the logic of the Enemy Spawner
        spawnEnemies();
        // remove used objects
        mapObjects.removeAll(objToRemove);
    }
    
    private void handleAttacks(double deltaSeconds) {
        List<MapObject> toAdd = new LinkedList<>();
        Stream.concat(Stream.of(player), mapObjects.stream())
            .filter(e -> e instanceof Entity)
            .map(e -> (Entity) e)
            .forEach(e -> {
                e.getWeapons().stream()
                    .forEach(w -> {
                        toAdd.addAll(w.attack(deltaSeconds));
                    });
                });
        this.addAllObjects(toAdd);
    }

    private void checkCollectibles(List<MapObject> objToRemove) {
        mapObjects.stream()
            .filter(e -> e instanceof Collectible)
            .map(e -> (Collectible) e)
            .filter(c -> CollisionLogic.areColliding(player, c))
            .forEach(c -> {
                c.apply(player);
                objToRemove.add(c);
            });
    }

    private void checkEnemyHits(List<MapObject> objToRemove) {
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
    }

    private void checkPlayerHits(List<MapObject> objToRemove) {
        mapObjects.stream()
            .filter(e -> e instanceof Projectile)
            .map(o -> (Projectile)o)
            .filter(p -> p.getOwner() instanceof Enemy) // check only Projectiles shot by enemies
            .filter(o -> CollisionLogic.areColliding(player, o))
            .forEach(o -> projectileHit(o, player, objToRemove));
    }

    private void updateObjectPositions(final double deltaSeconds) {
        mapObjects.stream()
            .filter(e -> e instanceof Movable)
            .map(o -> (Movable)o)
            .peek(o -> {
                log(o);
            })
            .forEach(o -> o.updatePosition(deltaSeconds));
    }

    private void log(Movable o) {
        System.out.println(o.getClass() + " x: " + o.getPosition().x() + " y: " + o.getPosition().y()); // FIXME: think about better logging
        if (o instanceof Entity e) {
            System.out.println("health: " + e.getCurrentHealth());  // FIXME: think about better logging
        }
        if (o instanceof Projectile) {
            System.out.println("direzione proiettile: " + o.getDirection());
        }
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

    /**
     * {@inheritDoc}
     */
    public void spawnEnemies() {
        this.addAllObjects(es.spawn(player, (int) mapObjects.stream().filter(e -> e instanceof Enemy).count())); // FIXME: avoidable cast?
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnemySpawnLogic(EnemySpawner spawnLogic) {
        this.es = spawnLogic;
    }

}
