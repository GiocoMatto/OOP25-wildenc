package it.unibo.wildenc.mvc.model.weaponary.weapons;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.joml.Vector2d;

import it.unibo.wildenc.mvc.model.Type;
import it.unibo.wildenc.mvc.model.weaponary.AttackMovementInfo;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.Projectile;
/**
 * Interface for modelling a Weapon. For instance, a Weapon is a factory of {@link Projectile}s which have specific
 * characteristics which are determined by the Weapon they're generated.
 */
public interface Weapon {
    
    public record WeaponStats(
        double weaponCooldown, double projDamage, double projVelocity,
        Type projType, String projID, double hbRadius, 
        BiFunction<Vector2d, AttackMovementInfo, Vector2d> moveFunction,
        BiConsumer<Integer, WeaponStats> upgradeLogics, Function<Type, String> nameFunc
    ) {}
    
    /**
     * Factory Method for the production of projectiles. The stats for the projectiles (damage, velocity, type of trajectory...)
     * are written inside the weapon - thus when the weapon will be upgraded the projectiles will be upgraded as well.
     * A {@link Projectile} is generated only if the weapon is not in "cooldown".
     * 
     * @param startingPoint the point where the projectile will be generated.
     * @return an {@link Optional} containing a {@link Projectile} if the attack was succesful, an empty one instead.
     */
    Optional<Projectile> attack(Vector2d startingPoint, Vector2d atkDirection);

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