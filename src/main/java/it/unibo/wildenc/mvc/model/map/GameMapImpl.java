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
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Collectible;
import it.unibo.wildenc.mvc.model.Enemy;
import it.unibo.wildenc.mvc.model.EnemySpawner;
import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.GameMap;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.Movable;
import it.unibo.wildenc.mvc.model.Player;
import it.unibo.wildenc.mvc.model.Projectile;
import it.unibo.wildenc.mvc.model.enemies.EnemySpawnerImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.Level;

/**
 * Basic {@link Map} implementation.
 */
public class GameMapImpl implements GameMap {

    private static final Logger LOGGER = LogManager.getLogger("Ciao!");
    private static final double NANO_TO_SECOND_FACTOR = 1_000_000_000.0;

    private final Player player;
    private final List<MapObject> mapObjects = new ArrayList<>();
    private EnemySpawner es;

    /** 
     * Create a new map.
     * 
     * @param p the player.
     */
    public GameMapImpl(final Player p) {
        player = p;
        this.es = new EnemySpawnerImpl(p);
        setupLogger();
    }

    /**
     * Test only constructor to provide objects useful to test purposes.
     * 
     * @param p the player;
     * @param es the enemy spawning logic;
     * @param initialObjs objects that the map should have from the beginning.
     */
    @TestOnly
    GameMapImpl(final Player p, final EnemySpawner es, final Set<MapObject> initialObjs) {
        player = p;
        this.es = es;
        mapObjects.addAll(initialObjs);
        setupLogger();
    }

    private void setupLogger() {
        Configurator.setRootLevel(Level.DEBUG);
    }

    /**
     * Add a {@link MapObject} on this Map.
     * 
     * @param mObj the {@link MapObject} to add.
     */
    protected void addObject(final MapObject mObj) {
        mapObjects.add(mObj);
    }

    /**
     * Add every {@link MapObject} inside of a {@link Collection} to the GameMap.
     * 
     * @param mObjs the objects to add.
     */
    @Override
    public void addAllObjects(final Collection<? extends MapObject> mObjs) {
        mObjs.forEach(this::addObject);
    }

    /**
     * Remove a {@link MapObject} from this Map.
     * 
     * @param mObj the {@link MapObject} to remove;
     * @return true if the {@link MapObject} was removed successfully.
     */
    protected boolean removeObject(final MapObject mObj) {
        return mapObjects.remove(mObj);
    }

    /**
     * {@inheritDoc}
     */
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
        final List<MapObject> objToRemove = new LinkedList<>();
        /*
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
        handleEnemyHits(objToRemove);
        /*
         * Check Collectibles
         */
        handleCollectibles(objToRemove);
        /*
         * Handle attacks
         */
        handleAttacks(deltaSeconds);
        // remove used objects
        mapObjects.removeAll(objToRemove);
    }

    private void handleCollectibles(final List<MapObject> objToRemove) {
        mapObjects.stream()
            .filter(e -> e instanceof Collectible)
            .map(e -> (Collectible) e)
            .filter(c -> CollisionLogic.areColliding(player, c))
            .forEach(c -> {
                c.apply(player);
                objToRemove.add(c);
            });
    }

    private void handleEnemyHits(final List<MapObject> objToRemove) {
        final List<Projectile> projectiles = getAllObjects().stream()
            .filter(e -> e instanceof Projectile)
            .map(e -> (Projectile) e)
            .filter(p -> p.getOwner() instanceof Player)
            .toList();
        final List<Enemy> enemies = getAllObjects().stream()
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

    private void checkPlayerHits(final List<MapObject> objToRemove) {
        mapObjects.stream()
            .filter(e -> e instanceof Projectile)
            .map(o -> (Projectile) o)
            .filter(p -> p.getOwner() instanceof Enemy) // check only Projectiles shot by enemies
            .filter(o -> CollisionLogic.areColliding(player, o))
            .forEach(o -> projectileHit(o, player, objToRemove));
    }

    private void updateObjectPositions(final double deltaSeconds) {
        mapObjects.stream()
            .filter(e -> e instanceof Movable)
            .map(o -> (Movable) o)
            .peek(o -> {
                log(o);
            })
            .forEach(o -> o.updatePosition(deltaSeconds));
    }

    private void log(final Movable o) {
        LOGGER.debug(o.getClass() + " x: " + o.getPosition().x() + " y: " + o.getPosition().y());
        if (o instanceof Entity e) {
            LOGGER.debug("health: " + e.getCurrentHealth());
        }
        if (o instanceof Projectile) {
            LOGGER.debug("direzione proiettile: " + o.getDirection());
        }
    }

    private void projectileHit(final Projectile p, final Entity e, final List<MapObject> toRemove) {
        if (!e.canTakeDamage()) { 
            return;
        }
        LOGGER.debug("!!!!!! Projectile hit !!!!!!");
        e.takeDamage((int) p.getDamage()); // FIXME: avoidable cast
        toRemove.add(p);
        if (e.getCurrentHealth() <= 0) {
            LOGGER.debug(e.getClass().toString() + " died!!!"); 
            toRemove.add(e);
        }
    }

    private void handleAttacks(final double deltaSeconds) {
        final List<MapObject> toAdd = new LinkedList<>();
        Stream.concat(Stream.of(player), this.getAllObjects().stream())
            .filter(e -> e instanceof Entity)
            .map(e -> (Entity) e)
            .forEach(e -> {
                e.getWeapons().stream()
                    .forEach(w -> {
                        toAdd.addAll(w.attack(deltaSeconds));
                    });
                });
        this.addAllObjects(toAdd); // add projectiles to the map objects.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void spawnEnemies(final double deltaSeconds) {
        // FIXME: avoidable cast?
        this.addAllObjects(es.spawn(player, (int) mapObjects.stream().filter(e -> e instanceof Enemy).count(), deltaSeconds));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnemySpawnLogic(final EnemySpawner spawnLogic) {
        this.es = spawnLogic;
    }

}
