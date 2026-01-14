package it.unibo.wildenc.Weaponary;

/**
 * Record that models a point in space.
 */
public record Point2D(double x, double y) {

    public boolean isEqual(Point2D anotherPoint) {
        return this.x() == anotherPoint.x() && this.y() == anotherPoint.y();
    }
    
}
