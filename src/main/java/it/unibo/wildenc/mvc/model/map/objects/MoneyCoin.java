package it.unibo.wildenc.mvc.model.map.objects;

import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Player;

/**
 * Class that models a MoneyCoin, a type of collectible.
 */
public class MoneyCoin extends AbstractCollectible {

    private static final int HITBOX_RADIUS = 20;

    /**
     * Constructor for the class.
     * 
     * @param position the position which the coin spawns at
     * @param value the value of that gem
     */
    public MoneyCoin(final Vector2dc position, final int value) {
        super(position, HITBOX_RADIUS, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(final Player target) {
        target.addMoney(getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "collectible:coin";
    }
}

