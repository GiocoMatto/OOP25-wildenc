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

    /**
     * Record for storing the projectile data, such as how much damage it does,
     * how does it moves, and other informations for differenciating the projectiles.
     */
    record ProjectileStats (
        double damage, AttackMovementInfo movementInfo, Type type, 
        BiFunction<Vector2d, AttackMovementInfo, Vector2d> movingFunc,
        String id, double hitboxRadius, Vector2d currentPosition
    ) {
        /**
         * Method for updating the position of the projectile without changing it's identity.
         * @param newPos the new position which the projectile has to travel to. 
         * @return a new {@link ProjectileStats} with the updated position.
         */
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

    /**
     * Getter method for getting the projectile's ID. This will be useful to
     * differentiate between different projectiles.
     * @return a {@link String} containing the ID of the projectile.
     */
    String getID();
}
