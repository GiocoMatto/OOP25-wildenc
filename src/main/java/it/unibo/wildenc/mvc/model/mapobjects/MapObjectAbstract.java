package it.unibo.wildenc.mvc.model.mapobjects;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.MapObject;

public abstract class MapObjectAbstract implements MapObject {

    private final Vector2d position;
    private final double hitboxRadius;
    
    public MapObjectAbstract(final Vector2dc spawnPosition, final double hitbox) {
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
     * A special method for subclasses that exposes the actual Vector to permit changes on the vector.
     * 
     * @return the writable vector.
     */
    protected Vector2d getWritablePosition() {
        return this.position;
    }
}
