package it.unibo.wildenc.mvc.model.enemies;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.util.Utilities;

/**
 * A enemy that move to the palyer until their hitbox collaps.
 */
public class CloseRangeEnemy extends AbstractEnemy {

    /**
     * Create a new close range Enemey.
     * 
     * @param abf the {@link AbstractEnemyField} used to initialize the enemy.
     */
    public CloseRangeEnemy(final AbstractEnemyField abf) {
        super(abf);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2dc alterDirection() {
        final var movement = direction(getTarget().get().getPosition(), this.getPosition());
        if (movement.lengthSquared() > 0) {
            return Utilities.normalizeVector(movement);
        }
        return new Vector2d(0, 0);
    }

}
