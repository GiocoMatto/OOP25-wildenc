package it.unibo.wildenc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;

import org.joml.Vector2d;
import org.joml.Vector2dc;
import org.junit.jupiter.api.Test;
import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.Collectible;
import it.unibo.wildenc.mvc.model.Enemy;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.enemies.AbstractEnemy.AbstractEnemyField;
import it.unibo.wildenc.mvc.model.enemies.CloseRangeEnemy;
import it.unibo.wildenc.mvc.model.enemies.RangedEnemy;
import it.unibo.wildenc.mvc.model.enemies.RoamingEnemy;
import it.unibo.wildenc.mvc.model.map.CollisionLogic;
import it.unibo.wildenc.mvc.model.map.objects.ExperienceGem;

public class EnemyTest {
    private static final double DELTA_SECONDS = 0.1;
    private static final Vector2d SPAWN_POSITION = new Vector2d(0, 0);
    private static final int HITBOX = 2;
    private static final int SPEED = 10;
    private static final int HEALTH = 500;
    private static final String NAME = "Pikachu";
    /* Collectible */
    private static final double HITBOX_COLLECTIBLE = 5;
    private static final int VALUE_COLLECTIBLE = 34;
    private static final MapObject TARGET_1 = new MapObject() {

        @Override
        public Vector2dc getPosition() {
            return new Vector2d(5, 0);
        }

        @Override
        public double getHitbox() {
            return 1;
        }

        @Override
        public boolean isAlive() {
            return true;
        }

        @Override
        public String getName() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getName'");
        };

    };
    private static final MapObject TARGET_2 = new MapObject() {

        @Override
        public Vector2dc getPosition() {
            return new Vector2d(105, 0);
        }

        @Override
        public double getHitbox() {
            return 3;
        }

        @Override
        public boolean isAlive() {
            return true;
        }

        @Override
        public String getName() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getName'");
        };

    };
    private static final MapObject TARGET_3 = new MapObject() {

        @Override
        public Vector2dc getPosition() {
            return new Vector2d(77, 0);
        }

        @Override
        public double getHitbox() {
            return 3;
        }

        @Override
        public boolean isAlive() {
            return true;
        }

        @Override
        public String getName() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getName'");
        };

    };
    private Enemy enemy;

    private Optional<Collectible> experienceLoot(final Vector2dc pos) {
        return Optional.of(new ExperienceGem(pos, VALUE_COLLECTIBLE));
    }

    @Test
    public void CloseRangeEnemyTest() {
        this.enemy = new CloseRangeEnemy(new AbstractEnemyField(SPAWN_POSITION, HITBOX, SPEED, HEALTH, NAME, Optional.of(TARGET_1), Set.of(e -> experienceLoot(e.getPosition()))));
        int count = 0;
        while (!CollisionLogic.areColliding(enemy, TARGET_1)) {
            enemy.updatePosition(DELTA_SECONDS);
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void RangedEnemyTest() {
        /* enemy is fare away the player */
        this.enemy = new RangedEnemy(new AbstractEnemyField(SPAWN_POSITION, HITBOX, SPEED, HEALTH, NAME, Optional.of(TARGET_2), Set.of(e -> experienceLoot(e.getPosition()))));
        int count = 0;
        while (!CollisionLogic.areInRange(enemy, TARGET_2, RangedEnemy.MAX_DISTANCE)) {
            enemy.updatePosition(DELTA_SECONDS);
            count++;
        }
        assertEquals(1, count);
        /* enemy is too much near the player */
        this.enemy = new RangedEnemy(new AbstractEnemyField(SPAWN_POSITION, HITBOX, SPEED, HEALTH, NAME, Optional.of(TARGET_3), Set.of(e -> experienceLoot(e.getPosition()))));
        count = 0;
        while (CollisionLogic.areInRange(enemy, TARGET_3, RangedEnemy.MIN_DISTANCE)) {
            enemy.updatePosition(DELTA_SECONDS);
            count++;
        }
        assertEquals(8, count);
    }

    @Test
    public void RoamingEnemyTest() {
        /* Try enemy is immortal for 5s */
        this.enemy = new RoamingEnemy(new AbstractEnemyField(SPAWN_POSITION, HITBOX, SPEED, HEALTH, NAME, Optional.empty(), Set.of(e -> experienceLoot(e.getPosition()))));
        try {
            Thread.sleep(RoamingEnemy.TIME_SAFE);
            assertTrue(((Entity)enemy).canTakeDamage());
        } catch (final InterruptedException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        /* Verify enemy direction randomly change evry 11 hit */
        enemy.updatePosition(DELTA_SECONDS);
        final Vector2dc dir1 = new Vector2d(enemy.getDirection());
        for (int i = 0; i < RoamingEnemy.STEP_FOR_CHANGE_DIRECTION; i++) {
            enemy.updatePosition(DELTA_SECONDS);
        }
        assertNotEquals(dir1, enemy.getDirection());
    }
}
