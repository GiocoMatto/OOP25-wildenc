package it.unibo.wildenc.Weaponary;

/**
 * Interface for modelling entities that are able to move. A movement is here determined by an algorithm, which
 * has to select the next point to move to.
 */
public interface Movable extends MapEntity {
    /**
     * Method for moving the entity. There are no parameters due to the fact that the movement doesn't require
     * a destination as it is determined by the algorithm put inside.
     */
    void move();
}
