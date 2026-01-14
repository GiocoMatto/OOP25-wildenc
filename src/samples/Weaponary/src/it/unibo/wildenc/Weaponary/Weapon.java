package it.unibo.wildenc.Weaponary;

import it.unibo.wildenc.Weaponary.AbstractWeapon.WeaponStats;

/**
 * Interface for modelling a Weapon. For instance, a Weapon is a factory of {@link Projectile}s which have specific
 * characteristics specified by the Weapon they're generated.
 */
public interface Weapon {
    /**
     * Factory Method for the production of projectiles. The stats for the projectiles (damage, velocity, type of trajectory...)
     * are written inside the weapon - thus when the weapon will be upgraded the projectiles will be upgraded as well.
     * @param startingPoint the point where the projectile will be generated.
     * @return the {@link Projectile} this weapon produces.
     */
    Projectile attack(Point2D startingPoint);

    /**
     * Method for upgrading the weapon. For mantaining the SRP, this will upgrade a {@link WeaponStats},
     * which contains all the weapon's statistics. 
     */
    void upgrade();
    
    /**
     * Method for getting the name of the weapon.
     * @return the name of the weapon.
     */
    String getName();

    /**
     * Method for getting the current stats of the weapon.
     */
    WeaponStats getStats();
}