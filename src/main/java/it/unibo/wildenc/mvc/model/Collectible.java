package it.unibo.wildenc.mvc.model;

/**
 * A collectible item on the Map, such as Experience, Health drops and others...
 */
public interface Collectible extends MapObject {

    /**
     * Gets the value of the collectible
     * @return quantity of the collectible (example: 15 experience, 2 health regen...)
     */
    int getValue();

}
