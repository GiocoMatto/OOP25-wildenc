package it.unibo.wildenc.mvc.model.map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.unibo.wildenc.mvc.model.MapObject;

import org.junit.jupiter.api.Test;

import static it.unibo.wildenc.mvc.model.map.MapTestingConstants.TEST_TIME_SECONDS;
import static it.unibo.wildenc.mvc.model.map.MapTestingConstants.calculateMovement;

import it.unibo.wildenc.mvc.model.map.MapTestingConstants.TestDirections;
import it.unibo.wildenc.mvc.model.map.MapTestingConstants.TestObject;

/**
 * Testing for the {@link MapObject}s.
 */
public class TestMapObjects {

    @Test
    void mapObjectShouldBeCreatedWithCorrectCoordinates() {
        final TestObject to = TestObject.StaticObject;
        final var obj = to.getAsStaticObj();
        assertEquals(to.getPos().x(), obj.getPosition().x());
        assertEquals(to.getPos().y(), obj.getPosition().y());
        assertEquals(to.getHitbox(), obj.getHitbox());
    }

    @Test
    void movableObjectWithNoDirectionShouldNotMove() {
        final TestObject to = TestObject.MovableObject;
        final var obj = to.getAsMovableObj();
        obj.updatePosition(TEST_TIME_SECONDS);
        assertEquals(to.getPos().x(), obj.getPosition().x());
        assertEquals(to.getPos().y(), obj.getPosition().y());
    }

    @Test
    void movableObjectWithDirectionShouldMoveCorrectly() {
        final TestObject to = TestObject.MovableObject;
        final TestDirections td = TestDirections.RIGHT;
        final var obj = to.getAsMovableObj();
        // Test movement with a direction.
        obj.setDirection(td.getVect());
        obj.updatePosition(TEST_TIME_SECONDS);
        assertEquals(calculateMovement(to.getPos(), td.getVect(), to.getSpeed(), TEST_TIME_SECONDS), obj.getPosition());
    }
}
