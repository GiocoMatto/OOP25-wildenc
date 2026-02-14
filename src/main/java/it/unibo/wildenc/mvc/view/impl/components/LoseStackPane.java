package it.unibo.wildenc.mvc.view.impl.components;

import java.util.Map;

import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.util.Utilities;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LoseStackPane extends StackPane {

    public LoseStackPane(Engine engine, Map<String,Integer> lostInfo) {
        VBox stackContainer = new VBox(); //per il layout
        stackContainer.getStyleClass().add("stack");

        Label title = new Label("GAME OVER"); //testo
        title.getStyleClass().add("title");
        VBox statsBox = new VBox();
        statsBox.getStyleClass().add("statsbox");
        Label subTitle = new Label("Statistiche Partita");
        statsBox.getChildren().add(subTitle);

        // Itera sulla mappa per creare le label
        if (lostInfo != null && !lostInfo.isEmpty()) {
            lostInfo.forEach((key, value) -> {
                String labelText = Utilities.capitalize(key.split(":")[1]) + ": " + value;
                Label statLabel = new Label(labelText);
                statsBox.getChildren().add(statLabel);
            });
        } else {
            Label noStats = new Label("Nessuna statistica disponibile.");
            statsBox.getChildren().add(noStats);
        }

        //Pulsanti
        Button btnMenu = new Button("Torna al Menu");
        btnMenu.setStyle("");
        btnMenu.setOnAction(e -> {
            //riapre menu usando l'ultimo personaggio scelto
            engine.menu(engine.getPlayerTypeChoise());
        });

        Button btnExit = new Button("Esci dal Gioco");
        btnExit.setOnAction(e -> Platform.exit());

        stackContainer.getChildren().addAll(title, statsBox, btnMenu, btnExit);

        getChildren().addAll(stackContainer);
    }

}
