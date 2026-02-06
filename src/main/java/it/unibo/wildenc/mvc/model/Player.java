package it.unibo.wildenc.mvc.model;

import org.joml.Vector2dc;

/**
 * Interface modeling the Player in the game.
 * The Player is an {@link Entity} capable of moving via input, gaining experience, and leveling up.
 */
public interface Player extends Entity {

    /**
     * Increases the player's level.
     * This usually triggers a stats upgrade or a reward selection.
     */
    void levelUp();

    /**
     * Gets the current experience points.
     * @return the current experience
     */
    int getExp();

    /**
     * Adds experience points to the player.
     * @param amount the experience to add.
     */
    void addExp(int amount);

    /**
     * Adds money coins
     * @param amount money to add
     */
    void addMoney(int amount);

    /**
     * @return current money
     */
    int getMoney();

    /**
     * 
     * @param amount of heal to add
     */
    void heal(int amount);

    /**
     * Sets the movement direction of the player.
     * This is typically called by an Input Controller.
     * * @param direction 
     * the new direction vector (as a {@link Vector2dc})
     */
    void setDirection(Vector2dc direction);

    /**
     * 
     * @return 
     */
    boolean canLevelUp();

}
