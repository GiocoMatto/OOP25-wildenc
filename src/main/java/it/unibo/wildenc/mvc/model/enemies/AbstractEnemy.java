package it.unibo.wildenc.mvc.model.enemies;

import java.util.HashSet;
import java.util.Optional;
import org.joml.Vector2d;
import org.joml.Vector2dc;
import it.unibo.wildenc.mvc.model.Enemy;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.entities.AbstractEntity;

/**
 * Represent a general enemy with a Target to attack 
 * and a Name for the identification.
 */
public abstract class AbstractEnemy extends AbstractEntity implements Enemy {
    private final Optional<MapObject> target;
    private final String name;
    private final int loot;

    /**
     * Create a new general Enemey.
     * 
     * @param abf the {@link AbstractEnemyField} used to initialize the enemy.
     */
    public AbstractEnemy(final AbstractEnemyField abf) {
        super(
            abf.spawnPosition, 
            abf.hitbox, 
            abf.movementSpeedfinal, 
            abf.health, 
            new HashSet<>()
        );
        this.name = abf.name;
        this.target = abf.target;
        this.loot = abf.loot;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<MapObject> getTarget() {
        return this.target;
    }

    /**
     * Calculate the direction betwen two vectors.
     * 
     * @param v1 Destinatino vector.
     * @param v2 Origin vector.
     * @return The vector.
     */
    protected Vector2d direction(final Vector2dc v1, final Vector2dc v2) {
        return new Vector2d(v1).sub(v2);
    }

    /**
     * Say if a enemy can take Damage rispect some condition. 
     * By default a enemy can always take damage.
     */
    @Override
    public boolean canTakeDamage() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLoot() {
        return this.loot;
    }

    /**
     * Parameters for a general enemy.
     * 
     * @param spawnPosition The position of spawn.
     * @param hitbox The area of map where the player can hit the nemey.
     * @param movementSpeedfinal the speed of movement of the enemy.
     * @param health The health of the enemy.
     * @param name The name of the enemy.
     * @param target The Optional Position of the player to hit.
     * @param loot The value enemy realse at his death.
     */
    public record AbstractEnemyField(
        Vector2dc spawnPosition, 
        double hitbox, 
        double movementSpeedfinal, 
        int health, 
        String name, 
        Optional<MapObject> target, 
        int loot
    ) { }

}
