package it.unibo.wildenc.mvc.model;

import java.util.List;
import java.util.function.BiConsumer;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponFactory;

/**
 * Map of the game, it includes all core logic to update all the entities on it.
 */
public interface GameMap {

    /**
     * Constant default player types.
     */
    public enum PlayerType {
        Charmender(10, 5, 100, (wf, p) -> {
            p.addWeapon(wf.getDefaultWeapon(10, 10, 2, 2, 100, 1, p, () -> new Vector2d(0, 30))); // FIXME: understand how to pass this value in a better way. It should be mouse position
        }),
        Bulbasaur(20, 30, 200, (wf, p) -> {
            // p.addWeapon(wf.getMeleeWeapon(7, 5, p));
        }),
        Squirtle(10, 5, 90, (wf, p) -> {
            // p.addWeapon(wf.getMeleeWeapon(8,4, p));
        });

        public record PlayerTypeRecord(int speed, double hitbox, int health, BiConsumer<WeaponFactory, Player> addDefaultWeapon) { }
        
        private PlayerTypeRecord playerType;

        public PlayerTypeRecord getPlayerType() {
            return playerType;
        }

        private PlayerType(int speed, double hitbox, int health, BiConsumer<WeaponFactory, Player> defaultWeapon) {
            playerType = new PlayerTypeRecord(speed, hitbox, health, defaultWeapon);
        }
        
    }

    /**
     * Get the player.
     * 
     * @return the {@link Player}.
     */
    Player getPlayer();

    /**
     * Get all objects on this Map.
     * 
     * @return A {@link List} of all {@link MapObject}s on this Map.
     */
    List<MapObject> getAllObjects();

    /**
     * Update every living object on this Map.
     * 
     * @param deltaTime 
     *                  how much to update in time.
     */
    void updateEntities(final long deltaTime, final Vector2dc playerDirection);

    /**
     * Spawn enemies on the map.
     */
    void spawnEnemies();

    /**
     * Set the enemy spawn logic.
     * 
     * @param spawnLogic a {@link EnemySpawner} logic.
     */
    void setEnemySpawnLogic(final EnemySpawner spawnLogic);

    /**
     * Whether the game is ended.
     * 
     * @return true if the game ended, false otherwise.
     */
    boolean gameEnded();

}
