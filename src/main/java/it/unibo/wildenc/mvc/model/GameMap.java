package it.unibo.wildenc.mvc.model;

import java.util.Collection;
import java.util.List;

import org.joml.Vector2dc;

/**
 * Map of the game, it includes all core logic to update all the entities on it.
 */
public interface GameMap {

    /**
     * Get the player.
     * 
     * @return the {@link Player}.
     */
    Player getPlayer();

    /**
     * Get all objects on this Map.
     * 
     * @return A {@link List} of all {@link MapObject}s on this Map.
     */
    List<MapObject> getAllObjects();

    /**
     * Adds all objects to this Map.
     */
    void addAllObjects(final Collection<? extends MapObject> mObjs);

    /**
     * Update every living object on this Map including collisions.
     * 
     * @param deltaTime how much to update in time;
     * @param playerDirection the player-chosen direction as a {@link Vector2dc}.
     */
    void updateEntities(long deltaTime, Vector2dc playerDirection);

    /**
     * Spawn enemies on the map.
     */
    void spawnEnemies();

    /**
     * Set the enemy spawn logic.
     * 
     * @param spawnLogic a {@link EnemySpawner} logic.
     */
    void setEnemySpawnLogic(EnemySpawner spawnLogic);

}
