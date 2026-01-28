package it.unibo.wildenc.mvc.model.weaponary;

import java.util.Optional;
import java.util.function.Supplier;

import org.joml.Vector2d;
import org.joml.Vector2dc;

/**
 * Class for representing important information used in an attack.
 */
public class AttackContext {
    private Vector2d lastPosition;
    private double atkDirection;
    private Optional<Supplier<Vector2d>> toFollow;
    private double velocity;

    /**
     * Constructor for the class.
     * @param initialDirection the direction of the attack in degrees
     * @param entityToFollow an {@link Optional} of a {@link Supplier} for a position to follow.
     */
    public AttackContext(
        final Vector2d initialPosition,
        final double initialDirection,
        Optional<Supplier<Vector2d>> entityToFollow
    ) {
        this.lastPosition = initialPosition;
        this.atkDirection = Math.toRadians(initialDirection % 360);
        this.toFollow = entityToFollow;
    }

    /**
     * Getter method for the position the attack has to follow.
     * @return the {@link Optional} of a {@link Supplier} for a position to follow. Could be empty.
     */
    public Optional<Supplier<Vector2d>> getFollowing() {
        return this.toFollow;
    }

    /**
     * Getter method for the direction versor which the attack has to follow.
     * @return a {@link Vector2d} representing the direction versor the attack has to follow.
     */
    public Vector2dc getDirectionVersor() {
        return new Vector2d(
            Math.cos(atkDirection),
            Math.sin(atkDirection)
        );
    }

    /**
     * Getter method for returning the last reference point.
     * @return the point contained in toFollow if present, if not the last position occupied by the Projectile.
     */
    public Vector2dc getLastPosition() {
        return toFollow.isPresent() ? toFollow.get().get() : lastPosition;
    }

    /**
     * Method for updating the last occupied position by the Projectile, if toFollow isn't specified.
     * @param newPos the new position occupied by the Projectile.
     */
    public void updateLastPosition(final Vector2dc newPos) {
        if(!toFollow.isPresent()) {
            this.lastPosition = new Vector2d(newPos);
        }
    }

    /**
     * Setter method for changing the angle of the direction of the attack.
     * @param newDirection the new direction, in degrees, to be set.
     */
    public void setDirection(final double newDirection) {
        this.atkDirection = Math.toRadians(newDirection % 360);
    }

    /**
     * Setter method for setting the current velocity of the projectile.
     * This could be also an angular velocity.
     * @param newVel the new velocity assumed by the projectile.
     */
    public void setVelocity(final double newVel) {
        this.velocity = newVel;
    }

    /**
     * Getter method for the velocity of the attack
     * @return the velocity of the projectile.
     */
    public double getVelocity() {
        return this.velocity;
    }
}
