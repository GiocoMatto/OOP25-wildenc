package it.unibo.wildenc.mvc.model;

import org.joml.Vector2d;

/**
 * Create defaut type of enemys.
 */
public interface EnemyFactory {

    /**
     * A classic close range Enemy.
     * 
     * @param spawnPosition the spawn position.
     * @param healt hte health of enemy.
     * @param name the name of the enemy.
     * @return a new close Ranged enemy.
     */
    Enemy closeRangeEnemy(
        Vector2d spawnPosition, 
        int healt,
        String name
    );

    /**
     * A Close range fast Enemy.
     * 
     * @param spawnPosition the spawn position.
     * @param healt hte health of enemy.
     * @param name the name of the enemy.
     * @return a new close Ranged enemy.
     */
    Enemy closeRangeFastEnemy(
        Vector2d spawnPosition, 
        int healt,
        String name
    );

    /**
     * A classic ranged Enemy.
     * 
     * @param spawnPosition the spawn position.
     * @param healt hte health of enemy.
     * @param name the name of the enemy.
     * @return a new close Ranged enemy.
     */
    Enemy rangedEnemy(
        Vector2d spawnPosition, 
        int healt,
        String name
    );

    /**
     * A classic range double shot Enemy.
     * 
     * @param spawnPosition the spawn position.
     * @param healt hte health of enemy.
     * @param name the name of the enemy.
     * @return a new close Ranged enemy.
     */
    Enemy rangedDoubleShotEnemy(
        Vector2d spawnPosition, 
        int healt,
        String name
    );

    /**
     * A classic roaming Enemy.
     * 
     * @param spawnPosition the spawn position.
     * @param healt hte health of enemy.
     * @param name the name of the enemy.
     * @return a new close Ranged enemy.
     */
    Enemy roamingEnemy(
        Vector2d spawnPosition, 
        int healt,
        String name
    );

    /**
     * A roaming long file Enemy.
     * 
     * @param spawnPosition the spawn position.
     * @param healt hte health of enemy.
     * @param name the name of the enemy.
     * @return a new close Ranged enemy.
     */
    Enemy roamingLongLifeEnemy(
        Vector2d spawnPosition, 
        int healt,
        String name
    );

}
