package it.unibo.wildenc.mvc.model.map.objects;

import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Player;

/**
 * Class that models an ExperienceGem, a type of collectible.
 */
public class ExperienceGem extends AbstractCollectible {

    private static final int HITBOX_RADIUS = 20;

    /**
     * Constructor for the class.
     * 
     * @param position the position which the gem spawns at
     * @param value the value of that gem
     */
    public ExperienceGem(final Vector2dc position, final int value) {
        super(position, HITBOX_RADIUS, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(final Player target) {
        target.addExp(getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "collectible:experience";
    }
}
