package it.unibo.wildenc.mvc.model.weaponary.projectiles;

import java.util.function.BiFunction;
import org.joml.Vector2d;

import it.unibo.wildenc.mvc.model.Type;
import it.unibo.wildenc.mvc.model.weaponary.AttackMovementInfo;

public class ConcreteProjectile implements Projectile {

    private ProjectileStats projStats;

    public ConcreteProjectile(
        double dmg, Type type, double hitboxRadius, String projID, Vector2d initialPosition, 
        AttackMovementInfo movement, BiFunction<Vector2d, AttackMovementInfo, Vector2d> func
    ) {
        this.projStats = new ProjectileStats(
            dmg, movement, type, func, projID, hitboxRadius, initialPosition
        );
    }

    @Override
    public void move() {
        this.projStats.updatePosition(
            this.projStats.movingFunc().apply(
                this.projStats.currentPosition(), this.projStats.movementInfo()
            )
        );
    }

    @Override
    public Vector2d getPosition() {
        return this.projStats.currentPosition();
    }

    @Override
    public double getDamage() {
        return this.projStats.damage();
    }

    @Override
    public Type getType() {
        return this.projStats.type();
    }

    @Override
    public double getHitbox() {
        return this.projStats.hitboxRadius();
    }

    @Override
    public String getID(){
        return this.projStats.id();
    }
}
