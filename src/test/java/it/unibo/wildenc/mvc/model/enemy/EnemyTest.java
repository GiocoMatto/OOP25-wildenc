package it.unibo.wildenc.mvc.model.enemy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2d;
import org.joml.Vector2dc;
import org.junit.jupiter.api.Test;

import it.unibo.wildenc.mvc.model.Collectible;
import it.unibo.wildenc.mvc.model.Enemy;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.enemies.AbstractEnemy.AbstractEnemyField;
import it.unibo.wildenc.mvc.model.enemies.CloseRangeEnemy;
import it.unibo.wildenc.mvc.model.enemies.RangedEnemy;
import it.unibo.wildenc.mvc.model.enemies.RoamingEnemy;
import it.unibo.wildenc.mvc.model.map.CollisionLogic;
import it.unibo.wildenc.mvc.model.map.objects.ExperienceGem;

/**
 * Test class for Enemies.
 */
class EnemyTest {
    private static final double DELTA_SECONDS = 0.1;
    private static final Logger LOGGER = LogManager.getLogger("Ciao!");
    private static final Vector2d SPAWN_POSITION = new Vector2d(0, 0);
    private static final int HITBOX = 1;
    private static final int SPEED = 10;
    private static final int HEALTH = 500;
    private static final String NAME = "Pikachu";
    private static final double MAX_DISTANCE = 400;
    private static final double MIN_DISTANCE = 250;
    /* Collectible */
    private static final int VALUE_COLLECTIBLE = 34;

    private static final MapObject TARGET_1 = new MapObject() {
        private static final double X = 5;
        private static final double Y = 0;

        @Override
        public Vector2dc getPosition() {
            return new Vector2d(X, Y);
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
            return NAME;
        }

    };
    private static final MapObject TARGET_2 = new MapObject() {
        private static final double X = 405;
        private static final double Y = 0;

        @Override
        public Vector2dc getPosition() {
            return new Vector2d(X, Y);
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
            return NAME;
        }

    };
    private static final MapObject TARGET_3 = new MapObject() {
        private static final double X = 77;
        private static final double Y = 0;

        @Override
        public Vector2dc getPosition() {
            return new Vector2d(X, Y);
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
            return NAME;
        }

    };

    private Optional<Collectible> experienceLoot(final Vector2dc pos) {
        return Optional.of(new ExperienceGem(pos, VALUE_COLLECTIBLE));
    }

    /**
     * Test logic for CloseRangeEnemy.
     */
    @Test
    void closeRangeEnemyTest() {
        final Enemy enemy = new CloseRangeEnemy(new AbstractEnemyField(
            SPAWN_POSITION, 
            HITBOX, 
            SPEED, 
            HEALTH, 
            NAME, 
            Optional.of(TARGET_1), 
            Set.of(e -> experienceLoot(e.getPosition())
        )));
        int count = 0;
        while (!CollisionLogic.areColliding(enemy, TARGET_1)) {
            enemy.updatePosition(DELTA_SECONDS);
            count++;
        }
        assertEquals(3, count);
    }

    /**
     * Test logic for RangedEnemy.
     */
    @Test
    void rangedEnemyTest() {
        final int steps = 178;
        /* enemy is fare away the player */
        Enemy enemy = new RangedEnemy(new AbstractEnemyField(
            SPAWN_POSITION, 
            HITBOX, 
            SPEED, 
            HEALTH, 
            NAME, 
            Optional.of(TARGET_2), 
            Set.of(e -> experienceLoot(e.getPosition())
        )));
        int count = 0;
        while (!CollisionLogic.areInRange(enemy, TARGET_2, MAX_DISTANCE)) {
            enemy.updatePosition(DELTA_SECONDS);
            count++;
        }
        assertEquals(1, count);
        /* enemy is too much near the player */
        enemy = new RangedEnemy(new AbstractEnemyField(
            SPAWN_POSITION, 
            HITBOX, 
            SPEED, 
            HEALTH, 
            NAME, 
            Optional.of(TARGET_3), 
            Set.of(e -> experienceLoot(e.getPosition()))
        ));
        count = 0;
        while (CollisionLogic.areInRange(enemy, TARGET_3, MIN_DISTANCE)) {
            enemy.updatePosition(DELTA_SECONDS);
            count++;
        }
        assertEquals(steps, count);
    }

    /**
     * Test logic for RoamingEnemy.
     */
    @Test
    void roamingEnemyTest() {
        /* Try enemy is immortal for 5s */
        final Enemy enemy = new RoamingEnemy(new AbstractEnemyField(
            SPAWN_POSITION, 
            HITBOX, 
            SPEED, 
            HEALTH, 
            NAME, 
            Optional.empty(), 
            Set.of(e -> experienceLoot(e.getPosition())
        )));
        try {
            Thread.sleep(RoamingEnemy.TIME_SAFE);
            assertTrue(enemy.canTakeDamage());
        } catch (final InterruptedException e) {
            LOGGER.atLevel(Level.ERROR).log("ERROR: " + e.getMessage());
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
