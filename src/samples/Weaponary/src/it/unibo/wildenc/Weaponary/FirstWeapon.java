package it.unibo.wildenc.Weaponary;

import java.util.function.BiFunction;

public class FirstWeapon extends AbstractWeapon {

    public FirstWeapon(int dmg, int vel, Type type, BiFunction<Point2D, Double, Point2D> movement, String name) {
        super(dmg, vel, type, movement, name);
    }

    @Override
    public Projectile attack(Point2D startingPoint) {
        return new ConcreteProjectile(
            this.weaponStats.projDamage(),
            this.weaponStats.projVelocity(),
            this.weaponStats.projType(),
            startingPoint,
            this.weaponStats.moveFunction()
        );
    }

    @Override
    public void upgrade() {
        this.weaponStats = new WeaponStats(
            weaponStats.projDamage()+1,
            weaponStats.projVelocity()+1,
            weaponStats.projType(),
            weaponStats.moveFunction()
        );
    }
}
