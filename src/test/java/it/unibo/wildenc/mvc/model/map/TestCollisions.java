package it.unibo.wildenc.mvc.model.map;

import static it.unibo.wildenc.mvc.model.map.MapTestingConstants.TEST_SIMULATION_TICKS;
import static it.unibo.wildenc.mvc.model.map.MapTestingConstants.TEST_TIME_SECONDS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.joml.Vector2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.wildenc.mvc.model.Movable;
import it.unibo.wildenc.mvc.model.map.MapTestingConstants.MovableObjectTest;
import it.unibo.wildenc.mvc.model.map.MapTestingConstants.TestObject;

public class TestCollisions {

    @Test
    void twoObjectsWithSameCenterShouldCollide() {
        final Movable first = new MovableObjectTest(new Vector2d(0, 0), 5, 0);
        final Movable second = new MovableObjectTest(new Vector2d(0, 0), 5, 0);

        assertTrue(CollisionLogic.areColliding(first, second));
    }

    @Test
    void twoObjectsWithTangetHitboxesShouldCollide() {
        final Movable first = new MovableObjectTest(new Vector2d(0, 0), 5, 0);
        final Movable second = new MovableObjectTest(new Vector2d(10, 0), 5, 0);

        assertTrue(CollisionLogic.areColliding(first, second));
    }

    @Test
    void twoObjectsWithSmallDistanceShouldNotCollide() {
        final Movable first = new MovableObjectTest(new Vector2d(0, 0), 5, 0);
        final Movable second = new MovableObjectTest(new Vector2d(10 + 1E-10, 0), 0, 0);

        assertFalse(CollisionLogic.areColliding(first, second));
    }

    @Test
    void twoMovingObjectsShouldCollide() {
        final Movable first = new MovableObjectTest(new Vector2d(0, 0), 5, 1);
        final Movable second = new MovableObjectTest(new Vector2d(10 + 1E-10, 0), 0, 1);

        for (int i = 0; i < TEST_SIMULATION_TICKS; i++) {
            first.updatePosition(TEST_TIME_SECONDS);
            second.updatePosition(TEST_TIME_SECONDS);
        }


    }


}
