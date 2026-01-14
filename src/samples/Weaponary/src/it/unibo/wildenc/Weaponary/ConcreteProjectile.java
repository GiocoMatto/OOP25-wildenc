package it.unibo.wildenc.Weaponary;

import java.util.function.BiFunction;

public class ConcreteProjectile implements Projectile {

    protected double projDamage;
    protected double projVelocity;
    protected Type projType;
    protected BiFunction<Point2D, Double, Point2D> projMovingFunc;
    private Point2D currentPosition;

    public ConcreteProjectile(
        double dmg, double vel, Type type,
        Point2D initialPosition,
        BiFunction<Point2D, Double, Point2D> func
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
    public Point2D getPosition() {
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

}
