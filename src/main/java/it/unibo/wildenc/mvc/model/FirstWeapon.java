package it.unibo.wildenc.mvc.model;

import java.util.function.BiFunction;

import org.joml.Vector2d;

public class FirstWeapon extends AbstractWeapon {

    public FirstWeapon(
        double cooldown, int dmg, int vel, Type type, 
        double hbRadius, BiFunction<Vector2d, Double, Vector2d> movement
    ) {
        super(cooldown, dmg, vel, type, hbRadius, movement);
        this.timeAtLastAtk = System.currentTimeMillis();
    }

    @Override
    public Projectile attack(Vector2d startingPoint) {
        return new ConcreteProjectile(
            this.weaponStats.projDamage(),
            this.weaponStats.projVelocity(),
            this.weaponStats.projType(),
            startingPoint,
            this.weaponStats.hbRadius(),
            this.weaponStats.moveFunction()
        );
    }

    @Override
    public void upgrade() {
        this.weaponStats = new WeaponStats(
            this.weaponStats.weaponCooldown(),
            this.weaponStats.projDamage()+1,
            this.weaponStats.projVelocity()+1,
            this.weaponStats.projType(),
            this.weaponStats.hbRadius(),
            this.weaponStats.moveFunction()
        );
    }

    @Override
    public String getName() {
        switch (this.weaponStats.projType()) {
            case WATER: return "Bollaraggio";
            case FIRE: return "Braciere";
            case GRASS: return "Fogliamagica";
            default: return "Basic Attack";
        }
    }
}
