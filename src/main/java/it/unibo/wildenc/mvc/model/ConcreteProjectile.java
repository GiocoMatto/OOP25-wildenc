package it.unibo.wildenc.mvc.model;

import java.util.function.BiFunction;
import org.joml.Vector2d;

/**
 * Basic implementation of a {@link Projectile}.
 */
public class ConcreteProjectile implements Projectile {

    /* 
        To Fix: Does having a single projectile type for every weapon have sense?
        In the future I could want to model weapons which shoot different projectiles
        at different times (with random speeds, random damage, crit chance...)
    */
   
    private double projDamage;
    private double projVelocity;
    private Type projType;
    private BiFunction<Vector2d, Double, Vector2d> projMovingFunc;
    private double hbRadius;
    private Vector2d currentPosition;

    public ConcreteProjectile(
        double dmg, double vel, Type type,
        Vector2d initialPosition, double hitboxRadius,
        BiFunction<Vector2d, Double, Vector2d> func
    ) {
        this.projDamage = dmg;
        this.projVelocity = vel;
        this.projType = type;
        this.projMovingFunc = func;
        this.hbRadius = hitboxRadius;
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
        return this.hbRadius;
    }

}
