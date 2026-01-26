package it.unibo.wildenc.mvc.model.enemies;

import java.util.Set;

import org.joml.Vector2d;
import org.joml.Vector2dc;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.weaponary.weapons.Weapon;

public class RangedEnemy extends AbstractEnemy {
    public static final int MAX_DISTANCE = 100;
    public static final int MIN_DISTANCE = 80;

    public RangedEnemy(
        final Vector2dc spawnPosition, 
        final double hitbox, 
        final double movementSpeedfinal, 
        final int health,
        final Set<Weapon> weapons, 
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
    public Vector2dc alterDirection() {
        // if (!maxArea.contains(posEnemy)) {
        //     return direction(getTarget().getPosition(), this.getPosition()).normalize();
        // } else if (!minArea.contains(posEnemy)) {
        //     return new Vector2d(0 ,0);
        // }
        // return direction(this.getPosition(), getTarget().getPosition()).normalize();
        throw new IllegalStateException();
    }

}
