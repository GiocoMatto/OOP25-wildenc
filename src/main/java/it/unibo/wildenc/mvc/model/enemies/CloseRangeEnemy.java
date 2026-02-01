package it.unibo.wildenc.mvc.model.enemies;

import java.util.Optional;
import java.util.Set;
import org.joml.Vector2d;
import org.joml.Vector2dc;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.Weapon;

/**
 * A enemy that move to the palyer until their hitbox collaps.
 */
public class CloseRangeEnemy extends AbstractEnemy{

    /**
     * {@inheritDoc}
     */
    public CloseRangeEnemy(
        final Vector2dc spawnPosition, 
        final double hitbox, 
        final double movementSpeedfinal, 
        final int health,
        final String name,
        final Optional<MapObject> target
    ) {
        super(
            spawnPosition, 
            hitbox, 
            movementSpeedfinal, 
            health, 
            name,
            target
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2dc alterDirection() {
        final var movement = direction(getTarget().get().getPosition(), this.getPosition());
        if (movement.lengthSquared() > 0) {
            return movement.normalize();
        }
        return new Vector2d(0 ,0);
    }

}
