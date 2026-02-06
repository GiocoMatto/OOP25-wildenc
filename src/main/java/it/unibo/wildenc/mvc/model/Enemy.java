package it.unibo.wildenc.mvc.model;

import java.util.Optional;
import java.util.Set;

/**
 * Enemy that attack a target.
 */
public interface Enemy extends Entity {

    /**
     * Get the name ofthe enemy.
     * 
     * @return the name of enemy.
     */
    String getName();

    /**
     * Get the {@link MapObject} of the target (the Player).
     * 
     * @return Relative MapObject Player.
     */
    Optional<MapObject> getTarget();

    /**
     * The loot that enemy release at his death.
     * 
     * @return the loot.
     */
    Set<Collectible> getLoot();

}
