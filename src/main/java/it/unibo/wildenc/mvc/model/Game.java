package it.unibo.wildenc.mvc.model;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.dataloaders.StatLoader;
import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponFactory;

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
     * @return earned money.
     */
    PlayerInfos getPlayerInfos();

    /**
     * Constant default player types.
     */
    enum PlayerType {
        CHARMANDER(300, 20, 100, (wf, p) -> {
            p.addWeapon(StatLoader.getInstance().getWeaponFactoryForWeapon("ember", p, () -> new Vector2d(0, 0)));
        }),
        BULBASAUR(200, 20, 120, (wf, p) -> {
            p.addWeapon(StatLoader.getInstance().getWeaponFactoryForWeapon("frenzyplant", p, () -> new Vector2d(0, 0)));
        }),
        SQUIRTLE(250, 20, 90, (wf, p) -> {
            p.addWeapon(StatLoader.getInstance().getWeaponFactoryForWeapon("bubble", p, () -> new Vector2d(0, 0)));
        });

        private final PlayerStats playerStats;

        PlayerType(final int speed, final double hitbox, final int health, 
            final BiConsumer<WeaponFactory, Player> defaultWeapon) {
            playerStats = new PlayerStats(speed, hitbox, health, defaultWeapon);
        }

        /**
         * Get the player stats.
         * 
         * @return a {@link PlayerStats} instance with the current player stats.
         */
        public PlayerStats getPlayerStats() {
            return playerStats;
        }

        /**
         * Player Stats.
         * 
         * @param speed The player's speed (pixel-per-second);
         * @param hitbox The player's hitbox radius;
         * @param health The player's max health;
         * @param addDefaultWeapon A BiConsumer which takes a WeaponFactory 
         */
        public record PlayerStats(int speed, double hitbox, int health,
            BiConsumer<WeaponFactory, Player> addDefaultWeapon) { }
    }

    /**
     * Represents a weapon to choose from on level up.
     * 
     * @param name the name of the weapon.
     * @param lvlUpDescription a brief explanation on 
     *      what clicking this specific upgrade does.
     */
    record WeaponChoice(String name, String lvlUpDescription) {
        @Override
        public final String toString() {
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
     */
    record PlayerInfos(int experience, int level, int neededExp, int coins) {
    }

    /**
     * gets the player entity (used for sounds of collectibles)
     * @return the player
     */
    Player getPlayer();
}
