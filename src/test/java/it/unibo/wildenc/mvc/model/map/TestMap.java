package it.unibo.wildenc.mvc.model.map;

import static it.unibo.wildenc.mvc.model.map.TestingVariables.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.joml.Vector2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.wildenc.mvc.model.GameMap;
import it.unibo.wildenc.mvc.model.MapObject;

public class TestMap {
    
    private GameMap map;
    private MapObject mapObj = new MapObjectTest(new Vector2d(TEST_X, TEST_Y), TEST_HITBOX);
    private MovableObjectTest movableObj = new MovableObjectTest(new Vector2d(TEST_X, TEST_Y), TEST_HITBOX, TEST_SPEED);

    @BeforeEach
    void setup() {
        map = new GameMapImpl();
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

    @Test
    void testCollisions() {
        testAddingObjects();
        // Check that two objects spawned at the same coordinate are colliding
        assertTrue(CollisionLogic.areColliding(movableObj, mapObj));
        // Now move the Movable object and check if they are not colliding anymore
        movableObj.setDirection(TEST_DIRECTION_DOWN);
        map.updateEntities(TEST_TIME_NANOSECONDS);
        assertFalse(CollisionLogic.areColliding(movableObj, mapObj));
    }
}
