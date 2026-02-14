package it.unibo.wildenc.mvc.model;

import org.joml.Vector2dc;

/**
 * Interface that models projectiles, entities which have to move.
 */
public interface Projectile extends Movable { 
    /**
     * Getter method for getting the projectile damage.
     * 
     * @return the damage of the projectile.
     */
    double getDamage();

    /**
     * Method used to know if the projectile has lived more than its Time To Live.
     * 
     * @return true if the projectile is still alive, false otherwise.
     */
    @Override
    boolean isAlive();

    /**
     * Method to get the position of the owner of the projectile.
     * 
     * @return the position of the owner who generated the Projectile.
     */
    Vector2dc getOwnerPosition();

    /**
     * Method to get the owner of the projectile's name.
     * 
     * @return the name of the owner.
     */
    String getOwnerName();

    /**
     * Method for knowing if a specific projectile is immortal.
     * An immortal projectile won't be destroyed on collision.
     * 
     * @return true if the projectile is immortal, false otherwise.
     */
    boolean isImmortal();
}

