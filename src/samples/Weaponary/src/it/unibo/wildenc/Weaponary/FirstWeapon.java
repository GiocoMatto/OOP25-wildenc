package it.unibo.wildenc.Weaponary;

import java.util.function.BiFunction;

public class FirstWeapon extends AbstractWeapon {

    public FirstWeapon(int dmg, int vel, Type type, BiFunction<Point2D, Integer, Point2D> movement, String name) {
        super(dmg, vel, type, movement, name);
    }

    @Override
    public Projectile attack(Point2D startingPoint) {
        return new Projectile() {
            @Override
            public void move() {
                // TODO Implement the position thing and change position of projectile based on function.
                // Still undone because there's no MapEntity yet.
                throw new UnsupportedOperationException("Unimplemented method 'move'");
            }

            @Override
            public double getDamage() {
                return weaponStats.projDamage();
            }

            @Override
            public Type getType() {
                return weaponStats.projType();
            }
        };
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
