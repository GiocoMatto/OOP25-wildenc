package it.unibo.wildenc.mvc.model.weaponary.weapons;

import java.util.function.Supplier;

import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.Weapon;

public interface WeaponFactory {
    Weapon createWeapon(
        final String weaponName,
        final double baseCooldown,
        final double baseDamage,
        final double hbRadius, 
        final double baseVelocity,
        final double baseTTL,
        final int baseProjAtOnce,
        final int baseBurst,
        final Entity ownedBy,
        final boolean immortal,
        final Supplier<Vector2dc> posToHit
    );
}
