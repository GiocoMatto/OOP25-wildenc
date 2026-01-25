package it.unibo.wildenc.mvc.model.map.objects;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Movable;

/**
 * Abstraction of any movable object.
 * 
 * @author Andrea Maria Castronovo
 */
public class AbstractMovable extends AbstractMapObject implements Movable {

    private final Vector2d direction = new Vector2d(0, 0); // start by staying still
    private final double speed;

    /**
     * Creates a new movable object.
     * 
     * @param spawnPosition the initial positin of the movable object;
     * @param hitbox the radius of the hitbox;
     * @param movementSpeed how fast it moves in pixel per seconds.
     */
    protected AbstractMovable(final Vector2dc spawnPosition, final double hitbox, final double movementSpeed) {
        super(spawnPosition, hitbox);
        speed = movementSpeed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePosition(final double deltaTime) {
        getWritablePosition().add(direction.x() * speed * deltaTime, direction.y() * speed * deltaTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2dc getDirection() {
        return direction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getSpeed() {
        return speed;
    }

    /**
     * Edit the direction of the Movable object.
     * 
     * @param direction normalized vector of the direction.
     */
    protected void setDirection(final Vector2dc direction) {
        this.direction.set(direction);
    }
    
}
