package it.unibo.wildenc.mvc.model;

import org.joml.Vector2d;

/**
 * A MapObject defines any entity that has a position on the map.
 * If something is a MapObject it has a position, defined as a {@link Vector2d}
 * and an hitbox, which is a double representing the radius of a circle
 * that has as center the entity position.
 */
public interface MapObject {
    /**
     * Getter method for returning the position of an entity on the map.
     * @return a {@link Vector2d} representing the (x,y) position of the entity.
     */
    Vector2d getPosition();

    /**
     * Getter method for returning the radius of the entity's hitbox.
     * This will be used to calculate collisions between 
     * @return the radius of the hitbox
     */
    double getHitbox();
}
