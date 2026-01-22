package it.unibo.wildenc.mvc.model;

import java.util.function.BiFunction;
import org.joml.Vector2d;

public class ConcreteProjectile implements Projectile {

    protected double projDamage;
    protected double projVelocity;
    protected Type projType;
    protected BiFunction<Vector2d, Double, Vector2d> projMovingFunc;
    protected final double radius = 2;
    private Vector2d currentPosition;

    public ConcreteProjectile(
        double dmg, double vel, Type type,
        Vector2d initialPosition,
        BiFunction<Vector2d, Double, Vector2d> func
    ) {
        this.projDamage = dmg;
        this.projVelocity = vel;
        this.projType = type;
        this.projMovingFunc = func;
        this.currentPosition = initialPosition;
    }

    @Override
    public void move() {
        this.currentPosition = projMovingFunc.apply(currentPosition, projVelocity);
    }

    @Override
    public Vector2d getPosition() {
        return this.currentPosition;
    }

    @Override
    public double getDamage() {
        return this.projDamage;
    }

    @Override
    public Type getType() {
        return this.projType;
    }

    @Override
    public double getHitbox() {
        return this.radius;
    }

}
