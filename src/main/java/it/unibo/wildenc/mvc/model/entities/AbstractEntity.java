package it.unibo.wildenc.mvc.model.entities;

import java.util.Collections;
import java.util.Set;

import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.Movable;
import it.unibo.wildenc.mvc.model.map.objects.AbstractMovable;
import it.unibo.wildenc.mvc.model.Weapon;

/**
 * Abstraction of a general entity.
 * 
 */
public abstract class AbstractEntity extends AbstractMovable implements Entity {

    private final int maxHealth;
    private final Set<Weapon> weapons;

    private int currentHealth;

    /**
     * Creates a {@link Movable} object that lives.
     * 
     * @param spawnPosition {@link AbstractMovable#getPosition()}
     * @param hitbox {@link AbstractMovable#getHitbox()}
     * @param movementSpeed {@link AbstractMovable#getSpeed()}
     * @param health max health of the entity.
     * @param weapons default weapons.
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
    public void takeDamage(final int dmg) {
        if (canTakeDamage()) {
            currentHealth = Math.max(currentHealth - dmg, 0);
        }
    }

    /**
     * Update the entity's position, which can be altered by its behavior.
     */
    @Override
    public void updatePosition(final double deltaTime) {
        setDirection(alterDirection());
        super.updatePosition(deltaTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWeapon(final Weapon p) {
        weapons.add(p);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAlive() {
        return currentHealth >= 0;
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

    public void getExp(int exp) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getExp'");
    }

}
