package it.unibo.wildenc.mvc.model;

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
     * Getter method for getting the projectile's ID. This will be useful to
     * differentiate between different projectiles.
     * 
     * @return a {@link String} containing the ID of the projectile.
     */
    String getID();

    /**
     * Method used to know if the projectile has lived more than its Time To Live.
     * 
     * @return true if the projectile is still alive, false otherwise.
     */
    boolean isAlive();

    /**
     * Method to get the owner of a specific Projectile.
     * 
     * @return the {@link Entity} who generated the Projectile.
     */
    Entity getOwner();
}
