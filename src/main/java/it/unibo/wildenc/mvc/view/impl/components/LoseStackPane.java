package it.unibo.wildenc.mvc.view.impl.components;

import java.util.Map;
import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.util.Utilities;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * StackPane that displays Game Over screen.
 */
public class LoseStackPane extends StackPane {

    /**
     * Creates a new LoseStackPane.
     * 
     * @param engine the game engine
     * @param lostInfo the statistics of the lost game
     */
    public LoseStackPane(final Engine engine, final Map<String, Integer> lostInfo) {
        final VBox stackContainer = new VBox(); //per il layout
        stackContainer.getStyleClass().add("stack");

        final Label title = new Label("GAME OVER"); //testo
        title.getStyleClass().add("title");
        final VBox statsBox = new VBox();
        statsBox.getStyleClass().add("statsbox");
        final Label subTitle = new Label("Statistiche Partita");
        statsBox.getChildren().add(subTitle);

        // Itera sulla mappa per creare le label
        if (lostInfo != null && !lostInfo.isEmpty()) {
            lostInfo.forEach((key, value) -> {
                final String labelText = Utilities.capitalize(key.split(":")[1]) + ": " + value;
                final Label statLabel = new Label(labelText);
                statsBox.getChildren().add(statLabel);
            });
        } else {
            final Label noStats = new Label("Nessuna statistica disponibile.");
            statsBox.getChildren().add(noStats);
        }

        //Pulsanti
        final Button btnMenu = new Button("Torna al Menu");
        btnMenu.setStyle("");
        btnMenu.setOnAction(e -> {
            //riapre menu usando l'ultimo personaggio scelto
            engine.menu(engine.getPlayerTypeChoise());
        });

        final Button btnExit = new Button("Esci dal Gioco");
        btnExit.setOnAction(e -> Platform.exit());

        stackContainer.getChildren().addAll(title, statsBox, btnMenu, btnExit);

        getChildren().addAll(stackContainer);
    }

}
