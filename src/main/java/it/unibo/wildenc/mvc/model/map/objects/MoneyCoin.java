package it.unibo.wildenc.mvc.model.map.objects;

import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Player;

public class MoneyCoin extends AbstractCollectible {

    protected MoneyCoin(Vector2dc position, int value) {
        super(position, 5.0, value);
    }

    @Override
    public void apply(Player target) {
        target.addMoney(getValue());
    }

    @Override
    public String getName() {
        return "collectible:coin";
    }
}

