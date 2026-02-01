package it.unibo.wildenc.mvc.model;

import java.util.Set;

@FunctionalInterface
public interface EnemySpawner {
    Set<Enemy> spawn(Player p, int enemyCount);
}
