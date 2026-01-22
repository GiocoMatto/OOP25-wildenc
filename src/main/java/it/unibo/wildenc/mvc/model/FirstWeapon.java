package it.unibo.wildenc.Weaponary;

import java.util.function.BiFunction;

import org.joml.Vector2d;

public class FirstWeapon extends AbstractWeapon {

    public FirstWeapon(int dmg, int vel, Type type, BiFunction<Vector2d, Double, Vector2d> movement, String name) {
        super(dmg, vel, type, movement, name);
    }

    @Override
    public Projectile attack(Vector2d startingPoint) {
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
