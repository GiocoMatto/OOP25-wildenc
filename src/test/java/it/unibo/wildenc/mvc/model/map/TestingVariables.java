package it.unibo.wildenc.mvc.model.map;

import java.util.Set;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Player;
import it.unibo.wildenc.mvc.model.entities.AbstractEntity;
import it.unibo.wildenc.mvc.model.map.objects.AbstractMapObject;
import it.unibo.wildenc.mvc.model.map.objects.AbstractMovable;
import it.unibo.wildenc.mvc.model.Weapon;

public class TestingVariables {
    public static final int TEST_X = 0;
    public static final int TEST_Y = 10;
    public static final int TEST_HITBOX = 5;
    public static final int TEST_SPEED = 10;
    public static final int TEST_TIME_NANOSECONDS = 1_000_000_000;
    public static final double TEST_TIME_SECONDS = 1.0;
    public static final Vector2d TEST_DIRECTION_RIGHT = new Vector2d(1, 0);
    public static final Vector2d TEST_DIRECTION_LEFT = new Vector2d(-1, 0); 
    public static final Vector2d TEST_DIRECTION_UP = new Vector2d(0, 1); 
    public static final Vector2d TEST_DIRECTION_DOWN = new Vector2d(0, -1);
    
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

    public static class PlayerTest extends AbstractEntity implements Player {

        protected PlayerTest(Vector2dc spawnPosition, double hitbox, double movementSpeed, int health,
                Set<Weapon> weapons) {
            super(spawnPosition, hitbox, movementSpeed, health, weapons);
            //TODO Auto-generated constructor stub
        }

        @Override
        public void addWeapons(Weapon p) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'addWeapons'");
        }

        @Override
        public void levelUp() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'levelUp'");
        }

        @Override
        public void getExp() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getExp'");
        }

        @Override
        public void setDirection(Vector2d dir) {
            setDirection(dir);
        }

        @Override
        public boolean canTakeDamage() {
            return true;
        }

        @Override
        protected Vector2dc alterDirection() {
            return getDirection();
        }

    }

    private TestingVariables() { }
}