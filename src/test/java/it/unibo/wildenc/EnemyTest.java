package it.unibo.wildenc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Set;

import org.joml.Vector2d;
import org.joml.Vector2dc;
import org.junit.jupiter.api.Test;
import it.unibo.wildenc.mvc.model.Enemy;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.enemies.CloseRangeEnemy;
import it.unibo.wildenc.mvc.model.enemies.RangedEnemy;
import it.unibo.wildenc.mvc.model.weaponary.weapons.Weapon;

public class EnemyTest {
    private static final Vector2d SPAWN_POSITION = new Vector2d(0, 0);
    private static final int HITBOX = 5;
    private static final int SPEED = 10;
    private static final int HEALTH = 500;
    private static final Set<Weapon> START_WEAPONS = Set.of();
    private static final String NAME = "Pikachu";
    private static MapObject TARGET = new MapObject() {

        @Override
        public Vector2dc getPosition() {
            return new Vector2d(5, 5);
        }

        @Override
        public double getHitbox() {
            return 3;
        }

    };
    private Enemy enemy;

    @Test
    public void CloseRangeEnemyTest() {
        this.enemy = new CloseRangeEnemy(
            SPAWN_POSITION,
            HITBOX,
            SPEED,
            HEALTH,
            START_WEAPONS,
            NAME,
            TARGET
        );
        int count = 0;
        // TODO: modificare condizione con un futuro contains 
        while (!enemy.getPosition().equals(TARGET.getPosition())) {
            enemy.updatePosition(1);
        }
        assertEquals(5, count);
    }

    @Test
    public void RangedEnemyTest() {
        this.enemy = new RangedEnemy(
            SPAWN_POSITION,
            HITBOX,
            SPEED,
            HEALTH,
            START_WEAPONS,
            NAME,
            TARGET
        );
        int count = 0;
        // TODO: modificare condizione con un futuro contains 
        while (!enemy.getPosition().equals(TARGET.getPosition())) {
            enemy.updatePosition(1);
        }
        assertEquals(5, count);
    }

    @Test
    public void RoamingEnemyTest() {
        throw new IllegalStateException();
    }
}
