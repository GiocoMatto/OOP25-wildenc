package it.unibo.wildenc.mvc.model.weaponary;

import java.util.Optional;
import java.util.function.Supplier;

import org.joml.Vector2d;
import org.joml.Vector2dc;

/**
 * Class for representing important information used in an attack.
 */
public class AttackContext {

    private Vector2dc lastPosition;
    private Vector2dc atkVersorDirection;
    private Supplier<Vector2dc> toFollow;
    private double velocity;

    /**
     * Constructor for the class.
     * 
     * @param initialPosition the position where the Projectile will start.
     * @param velocity the Projectile's velocity
     * @param positionToFollow the position which the Projectile has to follow.
     */
    public AttackContext(
        final Vector2dc initialPosition,
        final double velocity,
        final Supplier<Vector2dc> positionToFollow
    ) {
        this.lastPosition = initialPosition;
        this.toFollow = positionToFollow;
        this.velocity = velocity;
        this.atkVersorDirection = new Vector2d(this.toFollow.get()).sub(initialPosition).normalize();
    }

    /**
     * Getter method for the position the attack has to follow.
     * 
     * @return a {@link Supplier} for a position to follow. Could be empty.
     */
    public Supplier<Vector2dc> getFollowing() {
        return this.toFollow;
    }

    /**
     * Setter method for the position the attack has to follow.
     * 
     * @param newFollow the new {@link Supplier} which gives the direction.
     */
    public void setFollowing(final Supplier<Vector2dc> newFollow) {
        this.toFollow = newFollow;
    }

    /**
     * Getter method for the direction versor which the attack has to follow.
     * 
     * @return a {@link Vector2d} representing the direction versor the attack has to follow.
     */
    public Vector2dc getDirectionVersor() {
        return this.atkVersorDirection;
    }

    /**
     * Getter method for the angle which the attack has to follow.
     * 
     * @return the angle of the direction of the attack.
     */
    public double getActualAngle() {
        return Math.toRadians(Math.acos(this.atkVersorDirection.x()));
    }

    /**
     * Getter method for returning the last reference point.
     * 
     * @return the point contained in toFollow if present, if not the last position occupied by the Projectile.
     */
    public Vector2dc getLastPosition() {
        return lastPosition;
    }

    /**
     * Method for updating the last occupied position by the Projectile.
     * 
     * @param newPos the new position occupied by the Projectile.
     */
    public void updateLastPosition(final Vector2dc newPos) {
        this.lastPosition = new Vector2d(newPos);
    }

    /**
     * Setter method for changing the angle of the direction of the attack.
     * 
     * @param newDirection the new direction, in degrees, to be set.
     */
    public void setDirection(final double newDirection) {
        this.atkVersorDirection = new Vector2d(
            Math.cos(Math.toRadians(newDirection)),
            Math.sin(Math.toRadians(newDirection))
        );
    }

    /**
     * Setter method for setting the current velocity of the projectile.
     * This could be also an angular velocity.
     * 
     * @param newVel the new velocity assumed by the projectile.
     */
    public void setVelocity(final double newVel) {
        this.velocity = newVel;
    }

    /**
     * Getter method for the velocity of the attack.
     * 
     * @return the velocity of the projectile.
     */
    public double getVelocity() {
        return this.velocity;
    }

    /**
     * Method to create a protective copy of this class.
     * 
     * @return a copy of this object.
     */
    public AttackContext protectiveCopy() {
        return new AttackContext(this.lastPosition, this.velocity, this.toFollow);
    }
}
