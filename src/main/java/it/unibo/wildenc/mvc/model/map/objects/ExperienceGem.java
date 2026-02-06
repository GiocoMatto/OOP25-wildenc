package it.unibo.wildenc.mvc.model.map.objects;

import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Player;

public class ExperienceGem extends AbstractCollectible {

    public ExperienceGem(Vector2dc position, int value) {
        super(position, 5.0, value);
    }

    @Override
    public void apply(Player target) {
        target.addExp(getValue());
    }

    @Override
    public String getName() {
        return "experience";
    }
}
