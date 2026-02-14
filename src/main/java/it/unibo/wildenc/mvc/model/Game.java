package it.unibo.wildenc.mvc.model;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.joml.Vector2dc;

/**
 * Main game model logics, it provides infos outside of the model.
 */
public interface Game {

    /**
     * Update every living object on this Map.
     * 
     * @param deltaTime how much to update in nanoseconds;
     * @param playerDirection the player-chosen direction as a {@link Vector2dc}.
     */
    void updateEntities(long deltaTime, Vector2dc playerDirection);

    /**
     * Whether the game is ended.
     * 
     * @return true if the game ended, false otherwise.
     */
    boolean isGameEnded();

    /**
     * Notify which weapon was chosen.
     * 
     * @param wc name of chosen weapon.
     */
    void choosenWeapon(String wc);

    /**
     * Get the weapons to chose from when the player levels up.
     * 
     * @return A Set containing the weapons to choose from.
     */
    Set<WeaponChoice> weaponToChooseFrom();

    /**
     * Whether the player has levelled up.
     * 
     * @return true if the player has levelled up, false if not.
     */
    boolean hasPlayerLevelledUp();

    /**
     * Gets the game statistics such as kills, time.
     * 
     * @return a map with the statistics.
     */
    Map<String, Integer> getGameStatistics();

    /**
     * Gets all Map Objects (player included).
     * 
     * @return A {@link Collection} of all {@link MapObject}s inside the {@link GameMap} of the game.
     */
    Collection<MapObject> getAllMapObjects();

    /**
     * Gets earned money in this game.
     * 
     * @return player per-game infos as a {@link PlayerInfos} instance.
     */
    PlayerInfos getPlayerInfos();

    /**
     * Represents a weapon to choose from on level up.
     * 
     * @param name the name of the weapon.
     * @param lvlUpDescription a brief explanation on 
     *      what clicking this specific upgrade does.
     */
    record WeaponChoice(String name, String lvlUpDescription) {
        @Override
        public String toString() {
            return this.name() + " | " + this.lvlUpDescription();
        }
    }

    /**
     * Player infos that could be needed outside.
     * 
     * @param experience player's experience;
     * @param level player's level;
     * @param neededExp experience that player needs to level up;
     * @param coins player's earned coins in this game.
     * @param currentHealth player's current health
     * @param maxHealth player's max health
     */
    record PlayerInfos(
        int experience,
        int level,
        int neededExp,
        int coins,
        double currentHealth,
        double maxHealth
    ) { }
}
