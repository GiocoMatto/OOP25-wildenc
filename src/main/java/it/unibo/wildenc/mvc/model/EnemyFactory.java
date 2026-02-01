package it.unibo.wildenc.mvc.model;

import org.joml.Vector2d;

public interface EnemyFactory {

    /**
     * A classic CloseRangeEnemy.
     * @param spawnPosition the spawn position.
     * @param name the name of the enemy.
     * @return a new close Ranged enemy.
     */
    Enemy CloseRangeEnemy(
        Vector2d spawnPosition, 
        String name
    );

    /**
     * 
     * @param spawnPosition
     * @param name
     * @return
     */
    Enemy CloseRangeFastEnemy(
        Vector2d spawnPosition, 
        String name
    );

    /**
     * 
     * @param spawnPosition
     * @param name
     * @return
     */
    Enemy RangedEnemy(
        Vector2d spawnPosition, 
        String name
    );

    /**
     * 
     * @param spawnPosition
     * @param name
     * @return
     */
    Enemy RangedDoubleShotEnemy(
        Vector2d spawnPosition, 
        String name
    );

    /**
     * 
     * @param spawnPosition
     * @param name
     * @return
     */
    Enemy RoamingEnemy(
        Vector2d spawnPosition, 
        String name
    );

    /**
     * 
     * @param spawnPosition
     * @param name
     * @return
     */
    Enemy RoamingLongLifeEnemy(
        Vector2d spawnPosition, 
        String name
    );

}
