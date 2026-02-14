package it.unibo.wildenc.mvc.view.impl.components;

import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.mvc.view.api.SoundManager;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

public class PauseBox extends VBox {

    private final Button resumeBtn = new Button("Riprendi");

    /**
     * todo.
     * 
     * @param engine abc
     * @param sm abc
     */
    public PauseBox(final Engine engine, final SoundManager sm) {
        getStyleClass().add("pauseMenu");

        Label title = new Label("PAUSA");

        //pulsanti riprendi e torna al menu
        resumeBtn.setOnAction(e -> {
            engine.closeViewPause();
        });

        Button exitBtn = new Button("Torna al Menu");
        exitBtn.setOnAction(e -> {
            sm.stopMusic(); //ferma musica background
            engine.stopEngine();
            engine.menu(engine.getPlayerTypeChoise()); //torna al menu principale
        });

        setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                engine.closeViewPause();
            }
        });

        getChildren().addAll(title, resumeBtn, exitBtn);
    }

    @Override
    public void requestFocus() {
        resumeBtn.requestFocus();
    }

}
