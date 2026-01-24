package it.unibo.wildenc.mvc.model;

import org.joml.Vector2dc;

/**
 * Interface for modelling entities that are able to move. A movement is here determined by an algorithm, which
 * has to select the next point to move to.
 */
public interface Movable extends MapObject {
    /**
     * Update position of this movable object 
     * 
     * @param deltaTime 
     *                  movement decided by time
     */
    void updatePosition(long deltaTime);

    /**
     * Getter for the direction of this Movable object.
     * 
     * @return The object's direction as a read-only vector.
     */
    Vector2dc getDirection();

    /**
     * Set the direction of this Movable object.
     * 
     * @param direction Direction as a normalized {@link Vector2dc}.
     */
    void setDirection(Vector2dc direction);

    /**
     * Getter for the movement speed of this Movable object.
     * 
     * @return The object's movement speed as a double.
     */
    double getSpeed();
}
