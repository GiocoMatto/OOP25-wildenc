package it.unibo.wildenc.mvc.model;

import java.util.Set;
import java.util.function.Supplier;

import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponStats;

import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats;
/**
 * Interface for modelling a Weapon. For instance, a Weapon is a factory of {@link Projectile}s which have specific
 * characteristics which are determined by the Weapon they're generated.
 */
public interface Weapon {

    /**
     * Factory Method for the production of projectiles. The stats for the projectiles (damage, velocity, type of trajectory...)
     * are written inside the weapon - thus when the weapon will be upgraded the projectiles will be upgraded as well.
     * A {@link Projectile} is generated only if the weapon is not in "cooldown".
     * 
     * @param deltaTime the interval of time in which the attack occours
     * @return a {@link Set} containing one or more {@link Projectile}s if the attack was succesful, an empty one instead.
     */
    Set<Projectile> attack(double deltaTime);

    /**
     * Method for upgrading the weapon. For mantaining the SRP, this will upgrade a {@link WeaponStats},
     * which contains all the weapon's statistics. 
     */
    void upgrade();

    /**
     * Method for getting the name of the weapon.
     * 
     * @return the name of the weapon.
     */
    String getName();

    /**
     * Method for getting the current stats of the weapon.
     * 
     * @return a {@link WeaponStats} object containing all the weapon's characteristics.
     */
    WeaponStats getStats();
}
