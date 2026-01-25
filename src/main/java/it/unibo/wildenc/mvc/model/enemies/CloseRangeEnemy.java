package it.unibo.wildenc.mvc.model.enemies;

import java.util.List;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.weaponary.weapons.Weapon;

public class CloseRangeEnemy extends AbstractEnemy{

    public CloseRangeEnemy(
        final Vector2dc spawnPosition, 
        final double hitbox, 
        final double movementSpeedfinal, 
        final int health,
        final List<Weapon> weapons, 
        final String name,
        final MapObject target
    ) {
        super(
            spawnPosition, 
            hitbox, 
            movementSpeedfinal, 
            health, 
            weapons, 
            name,
            target
        );
    }

    @Override
    public Vector2dc specificMovement() {
        final var movement = new Vector2d(this.getTarget().getPosition()).sub(this.getPosition());
        if (movement.lengthSquared() > 0) {
            return movement.normalize();
        }
        return new Vector2d(0 ,0);
    }
    
}
