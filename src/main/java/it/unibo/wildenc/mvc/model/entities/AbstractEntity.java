package it.unibo.wildenc.mvc.model.entities;

import java.util.Collections;
import java.util.Set;

import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.Movable;
import it.unibo.wildenc.mvc.model.map.objects.AbstractMovable;
import it.unibo.wildenc.mvc.model.weaponary.weapons.Weapon;

/**
 * Abstraction of a general entity.
 * 
 * 
 */
public abstract class AbstractEntity extends AbstractMovable implements Entity {

    private final int maxHealth;
    private final Set<Weapon> weapons;

    private int currentHealth;
    
    /**
     * Creates a {@link Movable} object that lives.
     * 
     * @param spawnPosition the initial positin of the movable object;
     * @param hitbox the radius of the hitbox;
     * @param movementSpeed how fast it moves in pixel per seconds;
     * @param health max health of the entity.
     */
    protected AbstractEntity(
        final Vector2dc spawnPosition, 
        final double hitbox, 
        final double movementSpeed, 
        final int health,
        final Set<Weapon> weapons
    ) {
        super(spawnPosition, hitbox, movementSpeed);
        this.maxHealth = health;
        this.currentHealth = health; // start with max hp
        this.weapons = weapons;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxHealth() {
        return maxHealth;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Weapon> getWeapons() {
        return Collections.unmodifiableSet(weapons);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void takeDamage(int dmg) {
        if (canTakeDamage()) {
            currentHealth = Math.max(currentHealth - dmg, 0);
        }
    }

    /**
     * Update the entity's position, which can be altered by its behavior.
     */
    @Override
    public void updatePosition(double deltaTime) {
        setDirection(alterDirection());
        super.updatePosition(deltaTime);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean canTakeDamage();
    
    /**
     * Alters the entity direction.
     * 
     * @return the {@link Vector2dc} representing the entity's new direction
     */
    protected abstract Vector2dc alterDirection();


}
