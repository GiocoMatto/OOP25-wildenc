package it.unibo.wildenc;

import it.unibo.wildenc.mvc.controller.impl.EngineImpl;
import it.unibo.wildenc.mvc.model.Game;
import it.unibo.wildenc.mvc.view.impl.GameViewImpl;
import it.unibo.wildenc.mvc.controller.api.Engine;

import javafx.application.Application;
import javafx.stage.Stage;

public class EntryPoint extends Application {
    final Engine e = new EngineImpl();

    @Override
    public void start(Stage primaryStage) throws Exception {
        e.registerView(new GameViewImpl());
        //e.registerView(new GameViewImpl());
        e.start(Game.PlayerType.CHARMANDER);
    }

}
