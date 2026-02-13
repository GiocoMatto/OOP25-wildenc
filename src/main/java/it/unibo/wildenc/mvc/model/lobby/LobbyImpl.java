package it.unibo.wildenc.mvc.model.lobby;

import java.util.List;

import it.unibo.wildenc.mvc.model.Lobby;
import it.unibo.wildenc.mvc.model.dataloaders.StatLoader;

/**
 * Basic implementation of a Lobby.
 */
public class LobbyImpl implements Lobby {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PlayerType> getSelectablePlayers() {
        return StatLoader.getInstance().getAllPlayerData().stream()
            .map(data -> new PlayerType(
                data.entityName(),
                data.velocity(),
                data.hbRadius(),
                data.maxHealth(),
                data.weapon()
            ))
            .toList();
    }

}
