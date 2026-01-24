package it.unibo.wildenc.mvc.model.mapobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.joml.Vector2d;
import org.joml.Vector2dc;
import org.junit.jupiter.api.Test;

public class TestMapObjects {
    
    private class MapObjectTest extends MapObjectAbstract {

        public MapObjectTest(Vector2dc spawnPosition, double hitbox) {
            super(spawnPosition, hitbox);
        }
        
    }

    private class MovableObjectTest extends MovableAbstract {

        public MovableObjectTest(Vector2dc spawnPosition, double hitbox, double movementSpeed) {
            super(spawnPosition, hitbox, movementSpeed);
        }

    }

    
    private final static int TEST_X = 0;
    private final static int TEST_Y = 10;
    private final static int TEST_HITBOX = 0;
    private final static int TEST_SPEED = 10;
    private final static int TEST_TIME = 20;
    private final static Vector2d TEST_DIRECTION_RIGHT = new Vector2d(1, 0);
    private final static Vector2d TEST_DIRECTION_LEFT = new Vector2d(-1, 0); 
    private final static Vector2d TEST_DIRECTION_UP = new Vector2d(0, 1); 
    private final static Vector2d TEST_DIRECTION_DOWN = new Vector2d(0, -1); 

    @Test
    void TestMapObject() {
        var obj = new MapObjectTest(new Vector2d(0, 10), 1);
        assertEquals(0, obj.getPosition().x());
        assertEquals(10, obj.getPosition().y());
    }

    @Test
    void TestMovableObject() {

        var obj = new MovableObjectTest(new Vector2d(TEST_X, TEST_Y), TEST_HITBOX, TEST_SPEED);
        assertEquals(TEST_X, obj.getPosition().x());
        assertEquals(TEST_Y, obj.getPosition().y());
        
        obj.updatePosition(TEST_TIME);
        // No change because direction was not changed
        assertEquals(TEST_X, obj.getPosition().x());
        assertEquals(TEST_Y, obj.getPosition().y());

        obj.setDirection(TEST_DIRECTION_RIGHT);
        obj.updatePosition(TEST_TIME);
        assertEquals(TEST_X + TEST_DIRECTION_RIGHT.x() * TEST_SPEED * TEST_TIME, obj.getPosition().x());
        assertEquals(TEST_Y + TEST_DIRECTION_RIGHT.y() * TEST_SPEED * TEST_TIME, obj.getPosition().y());
    }
}
