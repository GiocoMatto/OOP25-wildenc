package it.unibo.wildenc.mvc.model.game;

import java.util.Map;
import java.util.Set;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Game;
import it.unibo.wildenc.mvc.model.GameMap;
import it.unibo.wildenc.mvc.model.Player;
import it.unibo.wildenc.mvc.model.map.GameMapImpl;
import it.unibo.wildenc.mvc.model.player.PlayerImpl;
import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponFactory;

public class GameImpl implements Game {

    private final GameMap map;
    private final Player player;

    private boolean playerLevelledUp = false;

    public GameImpl(final PlayerType pt) {
        player = getPlayerByPlayerType(pt);
        map = new GameMapImpl(player);
    }

    @Override
    public void updateEntities(final long deltaTime, final Vector2dc playerDirection) {
        // Update objects positions on map
        map.updateEntities(deltaTime, playerDirection);
        // check players level up
        if (player.canLevelUp()) {
            player.levelUp();
            this.playerLevelledUp = true;
        }
        // Spawn enemies into the map by its logic
        map.spawnEnemies();
    }

    @Override
    public boolean isGameEnded() {
        return map.getPlayer().isAlive();
    }

    @Override
    public void choosenWeapon(WeaponChoice wc) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'choosenWeapon'");
    }

    @Override
    public Set<WeaponChoice> weaponToChooseFrom() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'weaponToChooseFrom'");
    }

    @Override
    public boolean hasPlayerLevelledUp() {
        if (playerLevelledUp) {
            playerLevelledUp = false; // consume level up state for the next level up.
            return true;
        }
        return false;
    }
    
    @Override
    public Map<String, Integer> getGameStatistics() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGameStatistics'");
    }

    private Player getPlayerByPlayerType(final PlayerType playerType) {
        final var playerStats = playerType.getPlayerStats();
        final Player actualPlayer = new PlayerImpl(
            new Vector2d(0, 0),
            playerStats.hitbox(),
            playerStats.speed(),
            playerStats.health()
        );
        playerStats.addDefaultWeapon().accept(new WeaponFactory(), actualPlayer);
        return actualPlayer;
    }

}
