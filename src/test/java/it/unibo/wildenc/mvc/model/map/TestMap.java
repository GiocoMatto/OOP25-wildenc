package it.unibo.wildenc.mvc.model.map;

import static it.unibo.wildenc.mvc.model.map.TestingVariables.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.joml.Vector2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.wildenc.mvc.model.Map;
import it.unibo.wildenc.mvc.model.MapObject;

public class TestMap {
    
    private Map map;
    private MapObject mapObj = new MapObjectTest(new Vector2d(TEST_X, TEST_Y), TEST_HITBOX);
    private MovableObjectTest movableObj = new MovableObjectTest(new Vector2d(TEST_X, TEST_Y), TEST_HITBOX, TEST_SPEED);

    @BeforeEach
    void setup() {
        map = new MapImpl();
        movableObj.setDirection(TEST_DIRECTION_UP);
    }

    @Test
    void testMapCreation() {
        assertTrue(map.getAllObjects().isEmpty());
    }

    @Test
    void testAddingObjects() {
        // Static object
        map.addObject(mapObj);
        assertEquals(1, map.getAllObjects().size());
        assertTrue(map.getAllObjects().contains(mapObj));
        // Movable object
        map.addObject(movableObj);
        assertEquals(2, map.getAllObjects().size());
        assertTrue(map.getAllObjects().contains(movableObj));
    }

    @Test
    void testMovementOnMap() {
        testAddingObjects();
        var oldPos = new Vector2d(movableObj.getPosition());
        map.updateEntities(TEST_TIME_NANOSECONDS);
        assertEquals(oldPos.x() + TEST_SPEED * TEST_TIME_SECONDS * TEST_DIRECTION_UP.x(), movableObj.getPosition().x());
        assertEquals(oldPos.y() + TEST_SPEED * TEST_TIME_SECONDS * TEST_DIRECTION_UP.y(), movableObj.getPosition().y());
    }
}
