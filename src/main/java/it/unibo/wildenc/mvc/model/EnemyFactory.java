package it.unibo.wildenc.mvc.model;

import org.joml.Vector2d;

public interface EnemyFactory {

    Enemy CloseRangeEnemy(
        Vector2d spawnPosition, 
        String name, 
        MapObject target
    );
    
    Enemy CloseRangeFastEnemy(
        Vector2d spawnPosition, 
        String name, 
        MapObject target
    );

    Enemy RangedEnemy(
        Vector2d spawnPosition, 
        String name, 
        MapObject target
    );

    Enemy RangedDoubleShotEnemy(
        Vector2d spawnPosition, 
        String name, 
        MapObject target
    );

    Enemy RoamingEnemy(
        Vector2d spawnPosition, 
        String name
    );

    Enemy RoamingLongLifeEnemy(
        Vector2d spawnPosition, 
        String name
    );
}
