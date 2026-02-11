package it.unibo.wildenc.mvc.model.map;

import it.unibo.wildenc.mvc.model.MapObject;

/**
 * Collision Logic.
 */
public final class CollisionLogic {
    private CollisionLogic() { }

    /**
     * Whether two {@link MapObject}s are in a range.
     * 
     * @param m1 first {@link MapObject};
     * @param m2 second {@link MapObject};
     * @param range the range.
     * @return true if are in range, false otherwise.
     */
    public static boolean areInRange(final MapObject m1, final MapObject m2, final double range) {
        final var vertexDistance = m1.getPosition().distanceSquared(m2.getPosition());
        final var hitboxes = Math.pow(m1.getHitbox() + m2.getHitbox() + range, 2);
        return vertexDistance <= hitboxes;
    }

    /**
     * Whether two {@link MapObject}s are colliding.
     * 
     * @param m1 first {@link MapObject}
     * @param m2 second {@link MapObject}
     * @return true if they're colliding, false otherwise.
     */
    public static boolean areColliding(final MapObject m1, final MapObject m2) {
        return areInRange(m1, m2, 0);
    }

}
