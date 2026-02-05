package it.unibo.wildenc.mvc.model;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponFactory;

/**
 * Main game model logics, it provides infos outside of the model
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
     * @param wc chosen weapon.
     */
    void choosenWeapon(WeaponChoice wc);

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
     * Constant default player types.
     */
    enum PlayerType {
        Charmender(10, 5, 100, (wf, p) -> {
            // FIXME: understand how to pass the Vector2d Supplier. It should be the mouse position.
            p.addWeapon(wf.getDefaultWeapon(
                10.0, 
                10.0, 
                2.0,
                2.0,
                100,
                1,
                1,
                p,
                () -> new Vector2d(0, 0)
            )); 
        }),
        Bulbasaur(20, 30, 200, (wf, p) -> {
            // p.addWeapon(wf.getMeleeWeapon(7, 5, p));
        }),
        Squirtle(10, 5, 90, (wf, p) -> {
            // p.addWeapon(wf.getMeleeWeapon(8,4, p));
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
    
    public record WeaponChoice(String name) {
    }
}
