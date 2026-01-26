package it.unibo.wildenc.mvc.model;

import java.util.List;

/**
 * Map of the game, it includes all core logic to update all the entities on it.
 */
public interface GameMap {

    /**
     * Add a {@link MapObject} on this Map.
     * 
     * @param mObj 
     *              the {@link MapObject} to add
     */
    void addObject(MapObject mObj);

    /**
     * Remove a {@link MapObject} from this Map.
     * 
     * @param mObj 
     *              the {@link MapObject} to remove
     * @return
     *              true if the {@link MapObject} was removed successfully
     */
    boolean removeObject(MapObject mObj);

    /**
     * Get all objects on this Map.
     * 
     * @return A {@link List} of all {@link MapObject}s on this Map.
     */
    List<MapObject> getAllObjects();

    /**
     * Update every living object on this Map.
     * 
     * @param deltaTime 
     *                  how much to update in time.
     */
    void updateEntities(long deltaTime);
}
