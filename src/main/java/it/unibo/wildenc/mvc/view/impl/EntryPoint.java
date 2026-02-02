package it.unibo.wildenc.mvc.view.impl;

import it.unibo.wildenc.mvc.controller.impl.EngineImpl;
import it.unibo.wildenc.mvc.controller.api.Engine;

import javafx.application.Application;
import javafx.stage.Stage;

public class EntryPoint extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Engine e = new EngineImpl();
        e.registerView(new GameViewImpl());
        e.startGameLoop();
    }

}
