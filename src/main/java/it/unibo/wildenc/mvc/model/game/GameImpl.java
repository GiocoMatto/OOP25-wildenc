package it.unibo.wildenc.mvc.model.game;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Game;
import it.unibo.wildenc.mvc.model.GameMap;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.Player;
import it.unibo.wildenc.mvc.model.dataloaders.StatLoader;
import it.unibo.wildenc.mvc.model.map.GameMapImpl;
import it.unibo.wildenc.mvc.model.player.PlayerImpl;

/**
 * Basic implementation of the Game.
 */
public class GameImpl implements Game {

    private static final int WEAPON_CHOICE_NUM = 3;
    private static final StatLoader STATLOADER = StatLoader.getInstance();
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
        return !map.getPlayer().isAlive();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void choosenWeapon(final String wc) {
        if (player.getWeapons().stream()
            .noneMatch(w -> wc.equalsIgnoreCase(
                w.getName().split(":")[1]
            ))
        ) {
            player.addWeapon(
                STATLOADER.getWeaponFactoryForWeapon(
                    wc.toLowerCase(), 
                    player, 
                    () -> new Vector2d(0, 0))
            );            
        } else {
            player.getWeapons().stream()
                .filter(w -> wc.equalsIgnoreCase(
                    w.getName().split(":")[1]
                ))
                .findFirst()
                .get()
                .upgrade();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<WeaponChoice> weaponToChooseFrom() {
        return STATLOADER.getAllLoadedWeapons().stream()
            .filter(ws -> ws.availableToPlayer())
            .map(ws -> {
                if (!doPlayerHasWeapon(ws.weaponName())) {
                    return new WeaponChoice(
                        ws.weaponName(),
                        "New weapon!"
                    );
                } else {
                    final int weaponLevel = player.getWeapons()
                        .stream()
                        .filter(w -> w.getName().split(":")[1].equalsIgnoreCase(ws.weaponName()))
                        .findFirst()
                        .get().getStats().getLevel();
                    return new WeaponChoice(
                        ws.weaponName(),
                        "New level! [%d -> %d]".formatted(weaponLevel, weaponLevel + 1)
                    );
                }
            })
            .limit(WEAPON_CHOICE_NUM)
            .collect(Collectors.toSet());
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
        return Collections.unmodifiableMap(map.getMapBestiary());
    }

    private Player getPlayerByPlayerType(final PlayerType playerType) {
        final var playerStats = playerType.getPlayerStats();
        final Player actualPlayer = new PlayerImpl(
            playerType.name().toLowerCase(),
            new Vector2d(0, 0),
            playerStats.hitbox(),
            playerStats.speed(),
            playerStats.health()
        );
        playerStats.addDefaultWeapon().accept(null, actualPlayer);;
        return actualPlayer;
    }

    private boolean doPlayerHasWeapon(final String weaponName) {
        return !player.getWeapons().isEmpty() &&
            !player.getWeapons().stream()
                .noneMatch(w -> weaponName.equalsIgnoreCase(
                    w.getName().split(":")[1]
        ));
    }

    @Override
    public PlayerInfos getPlayerInfos() {
        return new PlayerInfos(player.getExp(), player.getLevel(), player.getExpToNextLevel(), player.getMoney());
    }

}
