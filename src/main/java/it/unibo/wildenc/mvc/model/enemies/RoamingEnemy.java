package it.unibo.wildenc.mvc.model.enemies;

import java.util.Random;
import java.util.Set;
import org.joml.Vector2d;
import org.joml.Vector2dc;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.weaponary.weapons.Weapon;

public class RoamingEnemy extends AbstractEnemy {
    private static final int STEP_FOR_CHANGE_DIRECTION = 11;
    private static final long TIME_SAFE = 5;
    private final Random rand;
    private int steps;
    private Vector2d v;
    private long startTime;

    public RoamingEnemy(
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
        this.rand = new Random();
        this.steps = 0;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public Vector2dc alterDirection() {
        if (this.steps == STEP_FOR_CHANGE_DIRECTION) {
            this.v = new Vector2d(
                rand.nextInt(), 
                rand.nextInt()
            );
            this.steps = 0;
        }
        this.steps++;
        return direction(v, this.getPosition()).normalize();
    }

    @Override
    protected boolean canTakeDamage() {
        final long now = System.currentTimeMillis();
        if (now - this.startTime >= 3) {
            return true;
        }
        return false;
    }

}
