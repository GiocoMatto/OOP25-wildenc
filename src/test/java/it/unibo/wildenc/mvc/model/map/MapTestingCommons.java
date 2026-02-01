package it.unibo.wildenc.mvc.model.map;

import java.util.Optional;
import java.util.Set;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Enemy;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.Weapon;
import it.unibo.wildenc.mvc.model.enemies.CloseRangeEnemy;
import it.unibo.wildenc.mvc.model.map.objects.AbstractMapObject;
import it.unibo.wildenc.mvc.model.map.objects.AbstractMovable;
import it.unibo.wildenc.mvc.model.player.PlayerImpl;

public final class MapTestingCommons {
    
    // Objects
    public enum TestObject {
        StaticObject(0, 10, 5, 0, 0),
        MovableObject(0, 10, 5, 1, 0),
        PlayerObject(0, 0, 5, 1, 100),
        EnemyObject(0, 30, 5, 1, 100);

        public final Vector2dc pos;
        public final double hitbox;
        /**
         * pixels per second
         */
        public final double speed;
        public final int health;

        private TestObject(int x, int y, double hitbox, double speed, int health) {
            this.pos = new Vector2d(x, y);
            this.hitbox = hitbox;
            this.speed = speed;
            this.health = health;
        }

        public MapObjectTest getAsStaticObj() {
            return new MapObjectTest(pos, hitbox);
        }

        public MovableObjectTest getAsMovableObj() {
            return new MovableObjectTest(pos, hitbox, speed);
        }

        public PlayerImpl getAsPlayer() {
            return new PlayerImpl(pos, hitbox, speed, health);
        }

        public Enemy getAsCloseRangeEnemy(Set<Weapon> weapons, String name, Optional<MapObject> target) {
            final Enemy e = new CloseRangeEnemy(pos, hitbox, speed, health, name, target);
            for (final var w : weapons) {
                e.addWeapon(w);
            }
            return e;
        }
    }

    // Directions
    public enum TestDirections {
        STILL(0,0),
        RIGHT(1, 0),
        LEFT(-1, 0),
        UP(0, 1),
        DOWN(0, -1);

        public final Vector2dc vect;

        TestDirections(int x, int y) {
            this.vect = new Vector2d(x, y);
        }
    }
    /**
     * 1 second in nanoseconds
     */
    public static final int TEST_TIME_NANOSECONDS = 1_000_000_000;
    /**
     * 1 second
     */
    public static final double TEST_TIME_SECONDS = 1.0;
    /**
     * 20 ticks of 1 second each, 20 seconds
     */
    public static final int TEST_SIMULATION_TICKS = 20;
    
    public static class MapObjectTest extends AbstractMapObject {

        public MapObjectTest(Vector2dc spawnPosition, double hitbox) {
            super(spawnPosition, hitbox);
        }

    }

    public static class MovableObjectTest extends AbstractMovable {

        public MovableObjectTest(Vector2dc spawnPosition, double hitbox, double movementSpeed) {
            super(spawnPosition, hitbox, movementSpeed);
        }

        @Override
        public void setDirection(Vector2dc direction) {
            super.setDirection(direction);
        }

    }

    public static Vector2dc calculateMovement(Vector2dc start, Vector2dc direction, double speed, double seconds) {
        return new Vector2d(direction)
            .mul(speed * seconds)
            .add(start);
    }

    private MapTestingCommons() { }
}