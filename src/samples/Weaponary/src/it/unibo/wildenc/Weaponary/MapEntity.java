package it.unibo.wildenc.Weaponary;

/**
 * A MapEntity defines any entity that has a position on the map.
 */
public interface MapEntity {
    /**
     * Getter method for returning the position of an entity on the map.
     * @return a {@link Point2D} representing the (x,y) position of the entity.
     */
    Point2D getPosition();
}
