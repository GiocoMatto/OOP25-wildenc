package it.unibo.wildenc.mvc.model.map.objects;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.MapObject;

/**
 * Abstraction of any map object.
 * 
 * @author Andrea Maria Castronovo
 */
public abstract class AbstractMapObject implements MapObject {

    private final Vector2d position;
    private final double hitboxRadius;
    
    /**
     * Creates a {@link MapObject} with a position and an hitbox.
     * 
     * @param spawnPosition the position of the object in the {@link Map}
     * @param hitbox
     */
    public AbstractMapObject(final Vector2dc spawnPosition, final double hitbox) {
        position = new Vector2d(spawnPosition);
        hitboxRadius = hitbox;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2dc getPosition() {
        return position; // Vector2dc exposes only read methods.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getHitbox() {
        return hitboxRadius;
    }
    
    /**
     * A special method for subclasses that exposes the {@link Vector2d} to permit changes on the vector.
     * 
     * @return the writable vector.
     */
    protected Vector2d getWritablePosition() {
        return this.position;
    }
}
