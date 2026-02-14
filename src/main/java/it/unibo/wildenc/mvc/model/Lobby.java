package it.unibo.wildenc.mvc.model;

import java.util.List;

/**
 * Represents lobby phase of the game, where the player is chosen.
 */
@FunctionalInterface
public interface Lobby {

    /**
     * Get the selectable players.
     * 
     * @return A {@link List} of {@link PlayerType} record that contains some infoes about the selectable player.
     */
    List<PlayerType> getSelectablePlayers();

    /**
     * Encapsulates all infos that should be known about every player.
     * 
     * @param name Player's name;
     * @param speed Player's speed;
     * @param hitbox Player's hitbox radius;
     * @param health Max player's health;
     * @param weapon Player's weapon
     */
    record PlayerType(String name, double speed, double hitbox, double health, String weapon) {
    }

}
