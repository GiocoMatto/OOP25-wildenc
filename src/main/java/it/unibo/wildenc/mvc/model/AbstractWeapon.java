package it.unibo.wildenc.mvc.model;

import org.joml.Vector2d;
import java.util.function.BiFunction;

/**
 * Abstract class for a Weapon. This is done because ALL weapons share these things:
 *  - Different weapons shoot different types of projectiles, so the weapon has the information needed to generate
 *    that specific kind of projectiles;
 *  - All weapons have a "cooldown": they won't attack at every moment, but an attack will happen some time after
 *    the last one;
 *  - Preventing the creation of copies of the same weapon for different types of projectile, the name of the weapon
 *    changes based off the type of the projectile it shoots (i.e. if SomeWeapon shoots some FIRE projectiles its name
 *    will be "Fire Attack", if it shoots WATER it will be "Water Attack", etc...). Also, the type of the projectile
 *    doesn't change how the weapon works, but its appearance in game.
 */
public abstract class AbstractWeapon implements Weapon {

    /**
     * Record for collecting the Weapon's characteristics. These include:
     *  - The cooldown of the weapon
     *  - The characteristics of the projectile it shoots (how much damage, how it moves...)
     */
    record WeaponStats(
        double weaponCooldown, double projDamage, double projVelocity,
        Type projType, double hbRadius, BiFunction<Vector2d, Double, Vector2d> moveFunction
    ) {}

    protected WeaponStats weaponStats;
    protected long timeAtLastAtk;

    /**
     * Constructor for the class. This will create a new instance of the record that saves
     * the weapon's initial statistics.
     * 
     * @param cooldown the cooldown of the weapon - how much time it needs to be elapsed to be able to attack again
     * @param dmg the base damage of the projectile that this weapon will generate
     * @param vel the base velocity of the projectile that this weapon will generate
     * @param type the type of the projectile that this weapon will generate
     * @param movement the function that describes how the projectile will move in the map
     * @param name the name of the weapon
     */
    public AbstractWeapon(double cooldown, int dmg, int vel, Type type, 
        double hitboxRadius, BiFunction<Vector2d, Double, Vector2d> movement)
    {
        this.weaponStats = new WeaponStats(cooldown, dmg, vel, type, hitboxRadius, movement);
    }

    /**
     * {@inheritDocs}
     */
    @Override
    public abstract Projectile attack(Vector2d startingPoint);

    /**
     * {@inheritDocs}
     */
    @Override
    public abstract void upgrade();

    /**
     * {@inheritDocs}
     */
    @Override
    abstract public String getName();
    
    public WeaponStats getStats() {
        return this.weaponStats;
    }
}
