package it.unibo.wildenc.mvc.model;

import java.util.Set;

/**
 * Spawn enemy.
 */
@FunctionalInterface
public interface EnemySpawner {

    /**
     * Spawn eney.
     * 
     * @param p Give the information of the player.
     * @param enemyCount Actual numer of enemy spawned.
     * @param time Tick time.
     * @return The enemys to spawn.
     */
    Set<Enemy> spawn(Player p, int enemyCount, double time);
}
