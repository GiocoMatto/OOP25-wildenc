package it.unibo.wildenc.mvc.model.mapobjects;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Movable;

public class MovableAbstract extends MapObjectAbstract implements Movable {

    private final Vector2d direction = new Vector2d(0, 0);
    private final double speed;

    public MovableAbstract(final Vector2dc spawnPosition, final double hitbox, final double movementSpeed) {
        super(spawnPosition, hitbox);
        speed = movementSpeed;
    }

    @Override
    public void updatePosition(final long deltaTime) {
        getWritablePosition().add(
            direction.x() * speed * deltaTime,
            direction.y() * speed * deltaTime
        );
    }

    @Override
    public Vector2dc getDirection() {
        return direction;
    }

    @Override
    public double getSpeed() {
        return speed;
    }

    @Override
    public void setDirection(final Vector2dc direction) {
        this.direction.set(direction);
    }
    
}
