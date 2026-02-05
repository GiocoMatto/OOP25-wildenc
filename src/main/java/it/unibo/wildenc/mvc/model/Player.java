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
     * Get player's current exp (resets on level up).
     */
    int getExp();

    /**
     * Whether the player can level up.
     * 
     * @return true if the player can level up, false otherwise.
     */
    boolean canLevelUp();

    /**
     * Sets the movement direction of the player.
     * This is typically called by an Input Controller.
     * * @param direction 
     * the new direction vector (as a {@link Vector2dc})
     */
    void setDirection(Vector2dc direction);

}
