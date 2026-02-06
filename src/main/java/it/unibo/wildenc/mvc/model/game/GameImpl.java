package it.unibo.wildenc.mvc.model.game;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Game;
import it.unibo.wildenc.mvc.model.GameMap;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.Player;
import it.unibo.wildenc.mvc.model.map.GameMapImpl;
import it.unibo.wildenc.mvc.model.player.PlayerImpl;
import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponFactory;

/**
 * Basic implementation of the Game.
 */
public class GameImpl implements Game {

    private final GameMap map;
    private final Player player;

    private boolean playerLevelledUp;

    /**
     * Create a normal game.
     * 
     * @param pt The player type.
     * @see PlayerType
     */
    public GameImpl(final PlayerType pt) {
        player = getPlayerByPlayerType(pt);
        map = new GameMapImpl(player);
    }

    /**
     * {@inheritDoc}
     */
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
        map.spawnEnemies(deltaTime); //FIXME: nano to seconds.
    }

    @Override
    public Collection<MapObject> getAllMapObjects() {
        return Stream.concat(Stream.of(map.getPlayer()), map.getAllObjects().stream()).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGameEnded() {
        return map.getPlayer().isAlive();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void choosenWeapon(final WeaponChoice wc) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'choosenWeapon'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<WeaponChoice> weaponToChooseFrom() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'weaponToChooseFrom'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPlayerLevelledUp() {
        if (playerLevelledUp) {
            playerLevelledUp = false; // consume level up state for the next level up.
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
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
