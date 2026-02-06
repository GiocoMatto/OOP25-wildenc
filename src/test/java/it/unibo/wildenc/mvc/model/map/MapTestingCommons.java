package it.unibo.wildenc.mvc.model.map;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Enemy;
import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.Player;
import it.unibo.wildenc.mvc.model.Weapon;
import it.unibo.wildenc.mvc.model.enemies.AbstractEnemy.AbstractEnemyField;
import it.unibo.wildenc.mvc.model.enemies.CloseRangeEnemy;
import it.unibo.wildenc.mvc.model.map.objects.AbstractCollectible;
import it.unibo.wildenc.mvc.model.map.objects.AbstractMapObject;
import it.unibo.wildenc.mvc.model.map.objects.AbstractMovable;
import it.unibo.wildenc.mvc.model.map.objects.ExperienceGem;
import it.unibo.wildenc.mvc.model.player.PlayerImpl;
import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponFactory;

/**
 * Testing constants for the map.
 */
public final class MapTestingCommons {
    /* Collectible */
    private static final double HITBOX_COLLECTIBLE = 5;
    private static final int VALUE_COLLECTIBLE = 34;

    /**
     * 1 second in nanoseconds.
     */
    public static final int TEST_TIME_NANOSECONDS = 1_000_000_000;
    /**
     * 1 second.
     */
    public static final double TEST_TIME_SECONDS = 1.0;
    /**
     * 20 ticks of 1 second each, 20 seconds.
     */
    public static final int TEST_SIMULATION_TICKS = 20;

    private MapTestingCommons() { }

    /**
     * Utility function for calculating linear movement.
     * 
     * @param start start position;
     * @param direction direction vector (will be normalized);
     * @param speed movement speed;
     * @param seconds time to move.
     * @return the new position after the movement as a {@link Vector2dc}
     */
    public static Vector2dc calculateMovement(
        final Vector2dc start, final Vector2dc direction, final double speed, final double seconds) {
        return new Vector2d(direction)
            .mul(speed * seconds)
            .add(start);
    }

    /**
     * Basic implementation for testing purposes.
     */
    public static final class MapObjectTest extends AbstractMapObject {

        /**
         * Creates a basic map object.
         * 
         * @param spawnPosition {@link AbstractMapObject#getPosition()}
         * @param hitbox {@link AbstractMapObject#getHitbox()}
         */
        public MapObjectTest(final Vector2dc spawnPosition, final double hitbox) {
            super(spawnPosition, hitbox);
        }

        /**
         * Always true for map testing purposes.
         */
        @Override
        public boolean isAlive() {
            return true;
        }

        @Override
        public String getName() {
            throw new UnsupportedOperationException("Not needed for testing.");
        }

    }

    /**
     * Basic implementation for testing purposes.
     */
    public static final class MovableObjectTest extends AbstractMovable {

        /**
         * Creates a basic MovableObject.
         * 
         * @param spawnPosition {@link AbstractMapObject#getPosition()}
         * @param hitbox {@link AbstractMapObject#getHitbox()}
         * @param movementSpeed {@link AbstractMovable#getSpeed()}
         */
        public MovableObjectTest(final Vector2dc spawnPosition, final double hitbox, final double movementSpeed) {
            super(spawnPosition, hitbox, movementSpeed);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setDirection(final Vector2dc direction) {
            super.setDirection(direction);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isAlive() {
            return true;
        }

        @Override
        public String getName() {
            throw new UnsupportedOperationException("Not needed for testing.");
        }
    }

    /**
     * Some default objects for testing purposes.
     */
    public enum TestObject {
        StaticObject(0, 10, 5, 0, 0),
        MovableObject(0, 10, 5, 1, 0),
        PlayerObject(0, 0, 5, 1, 100),
        EnemyObject(0, 30, 5, 1, 100);

        private final Vector2dc pos;
        private final double hitbox;
        private final double speed;
        private final int health;

        TestObject(final int x, final int y, final double hitbox, final double speed, final int health) {
            this.pos = new Vector2d(x, y);
            this.hitbox = hitbox;
            this.speed = speed;
            this.health = health;
        }

        /**
         * Starting object position.
         * 
         * @return the starting point as a {@link Vector2dc}.
         */
        public Vector2dc getPos() {
            return pos;
        }

        /**
         * Hitbox of the object.
         * 
         * @return circle radius.
         */
        public double getHitbox() {
            return hitbox;

        }

        /**
         * Speed as pixels per second.
         * 
         * @return speed as pixels per second.
         */
        public double getSpeed() {
            return speed;
        }

        /**
         * Max health of the entity.
         * 
         * @return max health.
         */
        public int getHealth() {
            return health;
        }

        /**
         * Generate a {@link MapObject} by the constant statistics in the enum.
         * 
         * @return a {@link MapObjectTest}.
         */
        public MapObjectTest getAsStaticObj() {
            return new MapObjectTest(pos, hitbox);
        }

        /**
         * Generate a {@link MovableObject} by the constant statistics in the enum.
         * 
         * @return a {@link MovableObjectTest}.
         */
        public MovableObjectTest getAsMovableObj() {
            return new MovableObjectTest(pos, hitbox, speed);
        }

        /**
         * Generate a {@link Player} by the constant statistics in the enum.
         * 
         * @return a {@link PlayerImpl}.
         */
        public PlayerImpl getAsPlayer() {
            return new PlayerImpl(pos, hitbox, speed, health);
        }

        /**
         * Generate a {@link Enemy} by the constant statistics in the enum.
         * 
         * @param weapons starting weapons of the enemy;
         * @param name name of the enemy;
         * @param target target that the enemy will follow.
         * @return a {@link CloseRangeEnemy} with the weapons assigned.
         */
        public CloseRangeEnemy getAsCloseRangeEnemy(final Set<Weapon> weapons, final String name, 
            final Optional<MapObject> target) {
            final CloseRangeEnemy e = new CloseRangeEnemy(new AbstractEnemyField(
                pos, 
                hitbox, 
                speed, 
                health, 
                name, 
                target, 
                Set.of(new ExperienceGem(pos, VALUE_COLLECTIBLE))));
                
            for (final var w : weapons) {
                e.addWeapon(w);
            }
            return e;
        }
    }

    /**
     * Some default weapons for testing purposes.
     */
    public enum TestWeapon {
        DEFAULT_WEAPON(5, 10, 2, 2, 99, 1, e -> () -> new Vector2d(e));

        private double baseCooldown;
        private double baseDamage;
        private double hbRadius;
        private double baseVelocity;
        private double baseTTL;
        private int baseBurst;
        private int baseProjAtOnce;
        private Function<Vector2dc, Supplier<Vector2dc>> posToHit;

        TestWeapon(final double baseCooldown, final double baseDamage, final double hbRadius, 
            final double baseVelocity, final double baseTTL, final int baseBurst, 
            final Function<Vector2dc, Supplier<Vector2dc>> posToHit) {
            this.baseCooldown = baseCooldown;
            this.baseDamage = baseDamage;
            this.hbRadius = hbRadius;
            this.baseVelocity = baseVelocity;
            this.baseTTL = baseTTL;
            this.baseBurst = baseBurst;
            this.posToHit = posToHit;
        }

        Weapon getAsWeapon(final Entity owner, final Vector2dc target) {
            return new WeaponFactory().getDefaultWeapon(
                baseCooldown,
                baseDamage,
                hbRadius,
                baseVelocity,
                baseTTL,
                baseProjAtOnce,
                baseBurst,
                owner,
                posToHit.apply(target)
            );
        }
    }

    /**
     * Directions to easly generate vectors for testing purposes.
     */
    public enum TestDirections {
        STILL(0, 0),
        RIGHT(1, 0),
        LEFT(-1, 0),
        UP(0, 1),
        DOWN(0, -1);

        private final Vector2dc vect;

        TestDirections(final int x, final int y) {
            this.vect = new Vector2d(x, y);
        }

        /**
         * Get the direction as a vector.
         * 
         * @return the vector.
         */
        public Vector2dc getVect() {
            return vect;
        }
    }

}
