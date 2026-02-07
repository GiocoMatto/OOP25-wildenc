package it.unibo.wildenc.mvc.model.enemies;

import java.util.Random;
import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.util.Utilities;

/**
 * A enemy that run in random direction in the map and 
 * is immortal for some times by the spawn.
 */
public class RoamingEnemy extends AbstractEnemy {
    public static final int STEP_FOR_CHANGE_DIRECTION = 11;
    public static final long TIME_SAFE = 5000;
    private int steps;
    private long startTime;
    private final Random rand;
    private Vector2d actualTarget;

    /**
     * Create a new roaming Enemey.
     * 
     * @param abf the {@link AbstractEnemyField} used to initialize the enemy.
     */
    public RoamingEnemy(final AbstractEnemyField abf) {
        super(abf);
        this.rand = new Random();
        updateDirection();
        this.steps = 0;
        this.startTime = System.currentTimeMillis();
    }

    private void updateDirection() {
        this.actualTarget = new Vector2d(
            rand.nextInt() * STEP_FOR_CHANGE_DIRECTION + this.getPosition().x(), 
            rand.nextInt() * STEP_FOR_CHANGE_DIRECTION + this.getPosition().y()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2dc alterDirection() {
        if (this.steps == STEP_FOR_CHANGE_DIRECTION) {
            updateDirection();
            this.steps = 0;
        }
        this.steps++;
        return Utilities.normalizeVector(direction(actualTarget, this.getPosition()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canTakeDamage() {
        final long now = System.currentTimeMillis();
        if (now - this.startTime >= TIME_SAFE) {
            return true;
        }
        return false;
    }

}
