package it.unibo.wildenc.Weaponary;

/**
 * Interface that models projectiles, entities which have to move.
 */
public interface Projectile extends Movable {
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
}
