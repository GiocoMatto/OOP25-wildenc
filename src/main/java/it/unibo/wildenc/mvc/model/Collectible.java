package it.unibo.wildenc.mvc.model;

/**
 * A collectible item on the Map, such as Experience, Health drops and others
 */
public interface Collectible extends MapObject {

    /**
     * Applies the effect of the collectible to the player
     * @param target the player collecting the item.
     */
    void apply(Player target);

    /**
     * Gets the value of the collectible
     ** @return the value of the item
     */
    int getValue();

}
