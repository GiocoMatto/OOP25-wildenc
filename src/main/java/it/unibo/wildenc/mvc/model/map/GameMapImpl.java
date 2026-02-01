package it.unibo.wildenc.mvc.model.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.function.BiConsumer;

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
import it.unibo.wildenc.mvc.model.enemies.EnemySpawnerImpl;
import it.unibo.wildenc.mvc.model.player.PlayerImpl;
import it.unibo.wildenc.mvc.model.weaponary.AttackContext;
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
    private final EnemySpawner es;

    public enum PlayerType {
        Charmender(10, 5, 100, (wf, p) -> {
            p.addWeapon(wf.getDefaultWeapon(10, 10, 2, 2, 100, 1, p, () -> new Vector2d(0, 30))); // FIXME: understand how to pass this value in a better way. It should be mouse position
        }),
        Bulbasaur(20, 30, 200, (wf, p) -> {
            // p.addWeapon(wf.getMeleeWeapon(7, 5, p));
        }),
        Squirtle(10, 5, 90, (wf, p) -> {
            // p.addWeapon(wf.getMeleeWeapon(8,4, p));
        });

        private final int speed;
        private final double hitbox;
        private final int health;
        private final BiConsumer<WeaponFactory, Player> default_weapon;

        private PlayerType(int speed, double hitbox, int health, BiConsumer<WeaponFactory, Player> default_weapon) {
            this.speed = speed;
            this.hitbox = hitbox;
            this.health = health;
            this.default_weapon = default_weapon;
        }

        Player getPlayer() {
            Player p = new PlayerImpl(new Vector2d(0, 0), this.hitbox, this.speed, this.health);
            default_weapon.accept(new WeaponFactory(), p);
            return p;
        }
    }

    /** 
     * Create a new basic map.
     * 
     * @param p
     *          the player.
     */
    public GameMapImpl(PlayerType p) {
        player = p.getPlayer();
        es = new EnemySpawnerImpl(player);
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
    public void addAllObjects(final Collection<? extends MapObject> mObj) {
        mObj.forEach(this::addObject);
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
        updatePositions(deltaSeconds);
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
        // spawnEnemies(); FIXME: EnemySpawner needs tweaking. Commented to allow testing of other mechanics
        // remove used objects
        mapObjects.removeAll(objToRemove);
    }
    
    private void handleAttacks(double deltaSeconds) {
        List<MapObject> toAdd = new LinkedList<>();
        mapObjects.stream()
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

    private void updatePositions(final double deltaSeconds) {
        Stream.concat(Stream.of(player), mapObjects.stream())
            .filter(e -> e instanceof Movable)
            .map(o -> (Movable)o)
            .peek(o -> {
                System.out.println(o.getClass() + " x: " + o.getPosition().x() + " y: " + o.getPosition().y()); // FIXME: think about better logging
                if (o instanceof Entity e) {
                    System.out.println("health: " + e.getCurrentHealth());  // FIXME: think about better logging
                }
                if (o instanceof Projectile) {
                    System.out.println("direzione proiettile: " + o.getDirection());
                }
            })
            .forEach(o -> o.updatePosition(deltaSeconds));
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

}
