package it.unibo.wildenc.mvc.view.impl.components;

import java.util.Set;
import java.util.function.BiConsumer;

import it.unibo.wildenc.mvc.model.Game;
import it.unibo.wildenc.mvc.model.Game.WeaponChoice;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;

public class PowerUpStackPane extends StackPane {

    private final static double WIDTH_RATIO = 0.4;
    private final static double HEIGHT_RATIO = 0.2;
    private final static int CELLSIZE = 26;
    private final static int DOUBLE_CLICK = 2;

    private final ListView<String> listView = new ListView<>();

    public PowerUpStackPane(StackPane root, Set<WeaponChoice> powerUps, BiConsumer<Set<Game.WeaponChoice>, ListView<String>> handler) {
        final Label text = new Label("Sblocca arma o potenziamento:");
        
        final VBox box = new VBox(text, listView);
        box.getStyleClass().add("vbox");
        box.setAlignment(Pos.CENTER);
        box.setFillWidth(true);
        listView.getItems().addAll(powerUps.stream().map(Object::toString).toList());
        listView.getSelectionModel().selectFirst();
        listView.setFixedCellSize(CELLSIZE);
        listView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handler.accept(powerUps, listView);
            }
        });
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == DOUBLE_CLICK) {
                handler.accept(powerUps, listView);
            }
        });
        VBox.setVgrow(listView, Priority.ALWAYS);
        listView.setMaxWidth(Double.MAX_VALUE);
        setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        prefWidthProperty().bind(root.widthProperty().multiply(WIDTH_RATIO));
        prefHeightProperty().bind(root.heightProperty().multiply(HEIGHT_RATIO));
        getChildren().add(box);
    }

    /**
     * Focus the power up list.
     */
    @Override
    public void requestFocus() {
        listView.requestFocus();
    }

}
