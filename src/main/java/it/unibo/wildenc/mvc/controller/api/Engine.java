package it.unibo.wildenc.mvc.controller.api;

import it.unibo.wildenc.mvc.view.api.GameView;

public interface Engine {
    
    void startGameLoop();

    void registerView(GameView gv);

}
