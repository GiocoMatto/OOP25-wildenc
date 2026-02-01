package it.unibo.wildenc.mvc.model;

import java.util.Set;
import java.util.function.BiConsumer;

import it.unibo.wildenc.mvc.model.weaponary.projectiles.Projectile;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats;
/**
 * Interface for modelling a Weapon. For instance, a Weapon is a factory of {@link Projectile}s which have specific
 * characteristics which are determined by the Weapon they're generated.
 */
public interface Weapon {
    
    public record WeaponStats(
        double weaponCooldown, ProjectileStats pStats, int burstSize,
        BiConsumer<Integer, WeaponStats> upgradeLogics
    ) {}
    
    /**
     * Factory Method for the production of projectiles. The stats for the projectiles (damage, velocity, type of trajectory...)
     * are written inside the weapon - thus when the weapon will be upgraded the projectiles will be upgraded as well.
     * A {@link Projectile} is generated only if the weapon is not in "cooldown".
     * 
     * @param atkInfo the informations regarding each projectile to spawn, such as starting position, starting direction, etc...
     * @return a {@link Set} containing one or more {@link Projectile}s if the attack was succesful, an empty one instead.
     */
    Set<Projectile> attack(final double deltaTime);

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
     * @return a {@link WeaponStats} object containing all the weapon's characteristics.
     */
    WeaponStats getStats();
}