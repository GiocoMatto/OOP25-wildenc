package it.unibo.wildenc.mvc.model.weaponary.weapons;

import java.util.function.Supplier;

import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.Weapon;

/**
 * Interface for modelling a factory for a weapons.
 */
@FunctionalInterface
public interface WeaponFactory {
    /**
     * Method for creating a weapon based off its statistics.
     * 
     * @param weaponName the name of the weapon
     * @param baseCooldown the initial cooldown of the weapon
     * @param baseDamage the base damage of the projectiles of the weapon
     * @param hbRadius the hitbox radius of the projectiles of the weapon
     * @param baseVelocity the base velocity of the projectiles of the weapon
     * @param baseTTL the base time to live of the projectiles of the weapon
     * @param baseProjAtOnce the number of projectiles shot at once, initially
     * @param baseBurst the number of projectiles in a burst, initially
     * @param ownedBy the owner of the weapon
     * @param immortal whether the projectile is immune to collision
     * @param posToHit the initial position which the weapon is aiming to
     * @return a {@link Weapon} with all the specified characteristics.
     */
    Weapon createWeapon(
        String weaponName,
        double baseCooldown,
        double baseDamage,
        double hbRadius, 
        double baseVelocity,
        double baseTTL,
        int baseProjAtOnce,
        int baseBurst,
        Entity ownedBy,
        boolean immortal,
        Supplier<Vector2dc> posToHit
    );
}
