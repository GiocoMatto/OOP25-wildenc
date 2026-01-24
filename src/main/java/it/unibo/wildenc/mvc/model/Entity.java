package it.unibo.wildenc.mvc.model;

import java.util.List;

import it.unibo.wildenc.mvc.model.weaponary.weapons.Weapon;

/**
 * A living entity. An Entity has an amount of health, can take damage, can have weapons.
 * Entities typically move on the map.
 */
public interface Entity extends Movable {

    /**
     * Get entity's health
     * @return entity's health
     */
    int getHealth();

    /**
     * Decrease health by dmg amount
     * @param dmg amount of damage
     * @return true if the entity has taken damage
     */
    boolean takeDamage(int dmg);

    /**
     * Weapons helded by this Entity
     * @return 
     *          A {@link List} collecting all the weapons helded by the Entity;
     *          an empty {@link List} is returned if the entity has no weapon.
     */
    List<Weapon> getWeapons();

}
