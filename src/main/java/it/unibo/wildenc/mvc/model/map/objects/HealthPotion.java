package it.unibo.wildenc.mvc.model.map.objects;

import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Player;

public class HealthPotion extends AbstractCollectible {

    protected HealthPotion(Vector2dc position, int value) {
        super(position, 5.0, value);
    }

    @Override
    public void apply(Player target) {
        target.heal(getValue());
    }

    @Override
    public String getName() {
        return "collectible:health_potion";
    }
}

