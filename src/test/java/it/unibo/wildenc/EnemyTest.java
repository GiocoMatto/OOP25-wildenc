package it.unibo.wildenc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Set;
import org.joml.Vector2d;
import org.joml.Vector2dc;
import org.junit.jupiter.api.Test;
import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.Enemy;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.enemies.CloseRangeEnemy;
import it.unibo.wildenc.mvc.model.enemies.RangedEnemy;
import it.unibo.wildenc.mvc.model.enemies.RoamingEnemy;
import it.unibo.wildenc.mvc.model.map.CollisionLogic;
import it.unibo.wildenc.mvc.model.weaponary.weapons.Weapon;

public class EnemyTest {
    private static final Vector2d SPAWN_POSITION = new Vector2d(0, 0);
    private static final int HITBOX = 2;
    private static final int SPEED = 10;
    private static final int HEALTH = 500;
    private static final Set<Weapon> START_WEAPONS = Set.of();
    private static final String NAME = "Pikachu";

    private static MapObject TARGET = new MapObject() {

        @Override
        public Vector2dc getPosition() {
            return new Vector2d(5, 0);
        }

        @Override
        public double getHitbox() {
            return 1;
        }

    };
    private Enemy enemy;

    @Test
    public void CloseRangeEnemyTest() {
        this.enemy = new CloseRangeEnemy(SPAWN_POSITION, HITBOX, SPEED, HEALTH, START_WEAPONS, NAME, TARGET);
        int count = 0;
        while (!CollisionLogic.areColliding(enemy, TARGET)) {
            enemy.updatePosition(0.1);
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void RangedEnemyTest() {
        MapObject target = new MapObject() {

            @Override
            public Vector2dc getPosition() {
                return new Vector2d(105, 0);
            }

            @Override
            public double getHitbox() {
                return 3;
            }

        };
        this.enemy = new RangedEnemy(SPAWN_POSITION, HITBOX, SPEED, HEALTH, START_WEAPONS, NAME, target);
        int count = 0;
        while (!CollisionLogic.areInRange(enemy, target, RangedEnemy.MAX_DISTANCE)) {
            enemy.updatePosition(0.1);
            count++;
        }
        assertEquals(1, count);
        target = new MapObject() {

            @Override
            public Vector2dc getPosition() {
                return new Vector2d(77, 0);
            }

            @Override
            public double getHitbox() {
                return 3;
            }

        };
        this.enemy = new RangedEnemy(SPAWN_POSITION, HITBOX, SPEED, HEALTH, START_WEAPONS, NAME, target);
        count = 0;
        while (CollisionLogic.areInRange(enemy, target, RangedEnemy.MIN_DISTANCE)) {
            enemy.updatePosition(0.1);
            count++;
        }
        assertEquals(8, count);
    }

    @Test
    public void RoamingEnemyTest() {
        this.enemy = new RoamingEnemy(SPAWN_POSITION, HITBOX, SPEED, HEALTH, START_WEAPONS, NAME, TARGET);
        try {
            Thread.sleep(5000);
            assertTrue(((Entity)enemy).canTakeDamage());
        } catch (final InterruptedException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
