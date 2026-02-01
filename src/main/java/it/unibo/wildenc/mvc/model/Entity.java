package it.unibo.wildenc.mvc.model;

import java.util.Set;

/**
 * A living entity. An Entity has an amount of health, can take damage, can have weapons.
 * Entities typically move on the map.
 */
public interface Entity extends Movable {

    /**
     * Get current entity's health
     * @return current entity's health
     */
    int getCurrentHealth();

    /**
     * Get entity's max health
     * @return entity's max health
     */
    int getMaxHealth();

    /**
     * Decrease health by dmg amount
     * @param dmg amount of damage
     */
    void takeDamage(int dmg);

    /**
     * Weapons helded by this Entity
     * @return 
     *          A {@link Set} collecting all the weapons helded by the Entity;
     *          an empty {@link Set} is returned if the entity has no weapon.
     */
    Set<Weapon> getWeapons();

    /**
     * Add a new weapon.
     * 
     * @param p
     *          The weapon to add.
     */
    void addWeapon(Weapon p);
    
    /**
     * If the entity can take damage.
     * 
     * @return true if the entity can take damage.
     */
    boolean canTakeDamage();
}
