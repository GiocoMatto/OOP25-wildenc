package it.unibo.wildenc;

import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.mvc.controller.impl.EngineImpl;
import it.unibo.wildenc.mvc.model.Game;
import it.unibo.wildenc.mvc.view.impl.GameViewImpl;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Entrypoint class for booting up the engine.
 */
public class EntryPoint extends Application {
    private final Engine e = new EngineImpl();

    /**
     * Method for starting the application.
     * 
     * @param primaryStage the main stage for running the app.
     */
    @Override
    public void start(final Stage primaryStage) throws Exception {
        e.registerView(new GameViewImpl());
        e.start(Game.PlayerType.CHARMANDER);
    }

}
