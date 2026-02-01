package it.unibo.wildenc.mvc.model;

/**
 * A collectible item on the Map, such as Experience, Health drops and others...
 */
public interface Collectible extends MapObject {

    /**
     * Gets the value of the collectible
     * 
     * @param p the player to apply the collectible
     */
    int apply(Player p);

}
