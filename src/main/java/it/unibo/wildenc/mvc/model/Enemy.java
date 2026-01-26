package it.unibo.wildenc.mvc.model;

public interface Enemy extends Entity {

    /**
     * Get the name ofthe enemy.
     * @return 
     */
    String getName();

    /**
     * Get the {@link MapObject} of the target (the Player).
     * @return Relative MapObject Player.
     */
    MapObject getTarget();

}
