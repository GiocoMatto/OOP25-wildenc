package it.unibo.wildenc.mvc.model.enemies;

import org.joml.Vector2d;

import it.unibo.wildenc.mvc.model.Enemy;
import it.unibo.wildenc.mvc.model.EnemyFactory;
import it.unibo.wildenc.mvc.model.MapObject;

public class EnemyFactoryImpl implements EnemyFactory {
    private final double hitbox = 1;
    private final double movementSpeedfinal = 1;
    private final int health = 100;

    @Override
    public Enemy CloseRangeEnemy(Vector2d spawnPosition, String name, MapObject target) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'CloseRangeEnemy'");
    }

    @Override
    public Enemy CloseRangeFastEnemy(Vector2d spawnPosition, String name, MapObject target) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'CloseRangeFastEnemy'");
    }

    @Override
    public Enemy RangedEnemy(Vector2d spawnPosition, String name, MapObject target) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'RangedEnemy'");
    }

    @Override
    public Enemy RangedDoubleShotEnemy(Vector2d spawnPosition, String name, MapObject target) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'RangedDoubleShotEnemy'");
    }

    @Override
    public Enemy RoamingEnemy(Vector2d spawnPosition, String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'RoamingEnemy'");
    }

    @Override
    public Enemy RoamingLongLifeEnemy(Vector2d spawnPosition, String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'RoamingLongLifeEnemy'");
    }

}
