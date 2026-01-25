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
     * Adds experience points to the player.
     * * @param exp 
     * the amount of experience to gain
     */
    void getExp(int exp);

    /**
     * Sets the movement direction of the player.
     * This is typically called by an Input Controller.
     * * @param direction 
     * the new direction vector (as a {@link Vector2dc})
     */
    void setDirection(Vector2dc direction);

}