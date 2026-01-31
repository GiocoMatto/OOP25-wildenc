package it.unibo.wildenc.mvc.view.api;

import java.util.Collection;
import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.mvc.model.MapObject;

public interface GameView {

    void start();

    void setEngine(Engine e);

    void updateSprites(Collection<MapObject> mObj);

    void won();

    void lost();

    void pause();

}
