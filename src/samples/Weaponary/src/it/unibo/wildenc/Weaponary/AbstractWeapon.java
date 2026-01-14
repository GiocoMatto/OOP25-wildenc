package it.unibo.wildenc.Weaponary;

import java.util.function.BiFunction;

/**
 * Abstract class for a Weapon. This is used as a skeleton for future weapons.
 */
public abstract class AbstractWeapon implements Weapon {

    record WeaponStats(double projDamage, double projVelocity, Type projType, BiFunction<Point2D, Double, Point2D> moveFunction) { }

    protected WeaponStats weaponStats;
    private final String weaponName;

    /**
     * Constructor for the class. This will create a new instance of the record that saves
     * the weapon's initial statistics.
     * 
     * @param dmg the base damage of the projectile that this weapon will generate
     * @param vel the base velocity of the projectile that this weapon will generate
     * @param type the type of the projectile that this weapon will generate
     * @param movement the function that describes how the projectile will move in the map
     * @param name the name of the weapon
     */
    public AbstractWeapon(int dmg, int vel, Type type, BiFunction<Point2D, Double, Point2D> movement, String name) {
        this.weaponStats = new WeaponStats(dmg, vel, type, movement);
        this.weaponName = name;
    }

    /**
     * {@inheritDocs}
     */
    @Override
    public abstract Projectile attack(Point2D startingPoint);

    /**
     * {@inheritDocs}
     */
    @Override
    public abstract void upgrade();

    /**
     * {@inheritDocs}
     */
    @Override
    public String getName() {
        return this.weaponName;
    }
    
    public WeaponStats getStats() {
        return this.weaponStats;
    }
}
