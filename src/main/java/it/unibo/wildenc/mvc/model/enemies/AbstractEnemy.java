package it.unibo.wildenc.mvc.model.enemies;

import java.util.Optional;
import java.util.Set;
import org.joml.Vector2d;
import org.joml.Vector2dc;
import it.unibo.wildenc.mvc.model.weaponary.weapons.Weapon;
import it.unibo.wildenc.mvc.model.*;
import it.unibo.wildenc.mvc.model.entities.AbstractEntity;

/**
 * Represent a general enemy with a Target to attack 
 * and a Name for the identification.
 */
public abstract class AbstractEnemy extends AbstractEntity implements Enemy {
    private final Optional<MapObject> target;
    private final String name;

    /**
     * Create a new general Enemey.
     * @param spawnPosition The position of spawn.
     * @param hitbox The area of map where the paler can hit the nemey.
     * @param movementSpeedfinal the speed of movement of the enemy.
     * @param health The health of the enemy.
     * @param weapons The weapon that can hit the palyer.
     * @param name The name of the enemy.
     * @param target The Optinal Posion of the payler to hit.
     */
    public AbstractEnemy(
        final Vector2dc spawnPosition, 
        final double hitbox, 
        final double movementSpeedfinal, 
        final int health, 
        final Set<Weapon> weapons, 
        final String name,
        final Optional<MapObject> target
    ) {
        super(spawnPosition, hitbox, movementSpeedfinal, health, weapons);
        this.name = name;
        this.target = target;
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
     * @param v1 Destinatino vector.
     * @param v2 Origin vector.
     * @return The vector.
     */
    protected Vector2d direction(final Vector2dc v1, final Vector2dc v2) {
        return new Vector2d(v1).sub(v2);
    }

    /**
     * Say if a enemy can take Damage rispect some condition. 
     * By default a enemy can alway take damage.
     */
    @Override
    public boolean canTakeDamage() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWeapons(final Weapon p) {
        throw new IllegalStateException();
    }

}
