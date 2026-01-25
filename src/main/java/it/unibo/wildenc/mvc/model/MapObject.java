package it.unibo.wildenc.mvc.model;

import org.joml.Vector2dc;

/**
 * A MapObject defines any object that has a position on the map.
 * If something is a MapObject it has a position, defined as a {@link Vector2d}
 * and an hitbox, which is a double representing the radius of a circle
 * that has as center the object position.
 */
public interface MapObject {
    /**
     * Getter method for returning the position of an entity on the map.
     * @return a {@link Vector2dc} (read-only) representing the (x,y) position of the entity.
     */
    Vector2dc getPosition();

    /**
     * Getter method for returning the radius of the entity's hitbox.
     * This will be used to calculate collisions between map objects
     * @return the radius of the hitbox
     */
    double getHitbox();
}
