package it.unibo.wildenc.mvc.model.weaponary.projectiles;

import java.util.function.BiFunction;

import org.joml.Vector2d;

import it.unibo.wildenc.mvc.model.Movable;
import it.unibo.wildenc.mvc.model.Type;
import it.unibo.wildenc.mvc.model.weaponary.AttackMovementInfo;

/**
 * Interface that models projectiles, entities which have to move.
 */
public interface Projectile extends Movable {

    record ProjectileStats (
        double damage, AttackMovementInfo movementInfo, Type type, 
        BiFunction<Vector2d, AttackMovementInfo, Vector2d> movingFunc,
        String id, double hitboxRadius, Vector2d currentPosition
    ) {
        ProjectileStats updatePosition(final Vector2d newPos) {
            return new ProjectileStats(damage, movementInfo, type, movingFunc, id, hitboxRadius, newPos);
        }
    }
    /**
     * Getter method for getting the projectile damage.
     * @return the damage of the projectile.
     */
    double getDamage();

    /**
     * Getter method for getting the projectile type.
     * @return the {@link Type} of the projectile.
     */
    Type getType();

    String getID();
}
