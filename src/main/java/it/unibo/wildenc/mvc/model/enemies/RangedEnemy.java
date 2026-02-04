package it.unibo.wildenc.mvc.model.enemies;

import org.joml.Vector2d;
import org.joml.Vector2dc;
import it.unibo.wildenc.mvc.model.map.CollisionLogic;

/**
 * A enemy that attach to a specific distance by the player.
 */
public class RangedEnemy extends AbstractEnemy {
    public static final int MAX_DISTANCE = 100;
    public static final int MIN_DISTANCE = 80;

    /**
     * Create a new ranged Enemey.
     * 
     * @param abf the {@link AbstractEnemyField} used to initialize the enemy.
     */
    public RangedEnemy(final AbstractEnemyField abf) {
        super(abf);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2dc alterDirection() {
        if (!CollisionLogic.areInRange(this, getTarget().get(), MAX_DISTANCE)) {
            return direction(getTarget().get().getPosition(), this.getPosition()).normalize();
        } else if (CollisionLogic.areInRange(this, getTarget().get(), MIN_DISTANCE)) {
            return direction(this.getPosition(), getTarget().get().getPosition()).normalize();
        }
        return new Vector2d(0, 0);
    }

}
