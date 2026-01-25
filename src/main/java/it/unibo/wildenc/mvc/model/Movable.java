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
     * @param deltaSeconds 
     *                  movement decided by time (seconds)
     */
    void updatePosition(double deltaSeconds);

    /**
     * Getter for the direction of this Movable object.
     * 
     * @return The object's direction as a read-only vector.
     */
    Vector2dc getDirection();

    /**
     * Getter for the movement speed of this Movable object.
     * 
     * @return The object's movement speed as pixel per seconds.
     */
    double getSpeed();
}
