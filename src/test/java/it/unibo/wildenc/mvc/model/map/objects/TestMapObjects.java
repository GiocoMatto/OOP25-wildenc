package it.unibo.wildenc.mvc.model.map.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.joml.Vector2d;
import org.junit.jupiter.api.Test;

import static it.unibo.wildenc.mvc.model.map.TestingVariables.MapObjectTest;
import static it.unibo.wildenc.mvc.model.map.TestingVariables.MovableObjectTest;
import static it.unibo.wildenc.mvc.model.map.TestingVariables.TEST_X;
import static it.unibo.wildenc.mvc.model.map.TestingVariables.TEST_Y;
import static it.unibo.wildenc.mvc.model.map.TestingVariables.TEST_HITBOX;
import static it.unibo.wildenc.mvc.model.map.TestingVariables.TEST_SPEED;
import static it.unibo.wildenc.mvc.model.map.TestingVariables.TEST_TIME_SECONDS;
import static it.unibo.wildenc.mvc.model.map.TestingVariables.TEST_DIRECTION_RIGHT;

public class TestMapObjects {

    @Test
    void TestMapObject() {
        var obj = new MapObjectTest(new Vector2d(0, 10), 1);
        assertEquals(0, obj.getPosition().x());
        assertEquals(10, obj.getPosition().y());
    }

    @Test
    void TestMovableObject() {
        // Test object creation.
        var obj = new MovableObjectTest(new Vector2d(TEST_X, TEST_Y), TEST_HITBOX, TEST_SPEED);
        assertEquals(TEST_X, obj.getPosition().x());
        assertEquals(TEST_Y, obj.getPosition().y());
        // Test object movement with no direction, no change expected because direction was not changed.
        obj.updatePosition(TEST_TIME_SECONDS);
        assertEquals(TEST_X, obj.getPosition().x());
        assertEquals(TEST_Y, obj.getPosition().y());
        // Test movement with a direction.
        obj.setDirection(TEST_DIRECTION_RIGHT);
        obj.updatePosition(TEST_TIME_SECONDS);
        assertEquals(TEST_X + TEST_DIRECTION_RIGHT.x() * TEST_SPEED * TEST_TIME_SECONDS, obj.getPosition().x());
        assertEquals(TEST_Y + TEST_DIRECTION_RIGHT.y() * TEST_SPEED * TEST_TIME_SECONDS, obj.getPosition().y());
    }
}
