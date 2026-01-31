package it.unibo.wildenc.mvc.view;

import java.util.Collection;

import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.view.api.GameView;

public class GameViewImpl implements GameView{
    private Engine eg;

    @Override
    public void setEngine(Engine e) {
        this.eg = e;
    }

    @Override
    public void updateSprites(Collection<MapObject> mObj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateSprites'");
    }

    @Override
    public void won() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'won'");
    }

    @Override
    public void lost() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'lost'");
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pause'");
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'start'");
    }

}
