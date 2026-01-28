package it.unibo.wildenc.mvc.model.map.objects;

import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Collectible;

/**
 * Implementation for collectible items
 * 
 */
public abstract class AbstractCollectible extends AbstractMapObject implements Collectible {

    private final int value;

    /**
     * Creates a new Collectible.
     * * @param position the initial position on the map
     * @param hitbox   the radius of the hitbox
     * @param value    the value of this collectible (amount of experience or health)
     */
    protected AbstractCollectible(final Vector2dc position, final double hitbox, final int value) {
        super(position, hitbox);
        this.value = value;
    }

    @Override
    public int getValue() {
        return this.value;
    }
}