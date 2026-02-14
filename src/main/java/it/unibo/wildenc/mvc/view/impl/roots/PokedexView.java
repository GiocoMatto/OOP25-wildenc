package it.unibo.wildenc.mvc.view.impl.roots;

import java.util.Map;

import it.unibo.wildenc.mvc.controller.api.Engine;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.util.Pair;

/**
 * View of the Pokedex.
 */
public final class PokedexView extends StackPane {
    private static final Rectangle2D SCREEN = Screen.getPrimary().getVisualBounds();
    private static final String PATH = "/images/menu/";
    private static final double WIDTH_RATIO = 0.35;
    private static final int CELLSIZE = 26;
    private static final int PADDING = 15;

    /**
     * Create the view Pokedex.
     * 
     * @param engine The controller
     * @param pokedexView The data loeded by file
     */
    public PokedexView(final Engine engine, final Map<String, Integer> pokedexView) {
        final Button goToMenu = new Button("Torna al menu" + (pokedexView.isEmpty() ? " (Pokedex vuoto)" : ""));
        goToMenu.setOnAction(e -> engine.menu(engine.getPlayerTypeChoise()));
        goToMenu.setMaxWidth(Double.MAX_VALUE);
        final ListView<Pair<String, Integer>> listView = new ListView<>();
        listView.getItems().addAll(
            pokedexView.entrySet().stream()
                .map(ent -> new Pair<>(ent.getKey(), ent.getValue()))
                .toList()
        );
        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(final Pair<String, Integer> entry, final boolean empty) {
                super.updateItem(entry, empty);
                if (empty || entry == null) {
                    setGraphic(null);
                    return;
                }
                final Label img = new Label("Nome: " + entry.getKey().split(":")[1]);
                final Label kills = new Label("Uccisioni: " + entry.getValue());
                final HBox row = new HBox(15, img, kills);
                row.setAlignment(Pos.CENTER_LEFT);
                setGraphic(row);
            }
        });
        final VBox box = new VBox(goToMenu, listView);
        getChildren().add(box);
        listView.setFixedCellSize(CELLSIZE);
        listView.setFocusTraversable(false);
        listView.setPrefHeight(pokedexView.size() * CELLSIZE + 2);
        box.setPadding(new Insets(PADDING));
        box.setStyle("-fx-background-color: #AEC6CF;");
        box.setMaxWidth(SCREEN.getWidth() * WIDTH_RATIO);
        box.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        box.prefWidthProperty().bind(widthProperty().multiply(WIDTH_RATIO));
        VBox.setVgrow(listView, Priority.ALWAYS);
        box.setMaxWidth(SCREEN.getWidth() * WIDTH_RATIO);
        final Image img = new Image(
            getClass().getResource(PATH + "backgroundReapeted.jpg").toExternalForm(), 
            250, 
            250, 
            true, 
            true
        );
        final BackgroundImage bgImg = new BackgroundImage(
            img, 
            BackgroundRepeat.REPEAT, 
            BackgroundRepeat.REPEAT, 
            BackgroundPosition.DEFAULT, 
            new BackgroundSize(
                BackgroundSize.AUTO, 
                BackgroundSize.AUTO, 
                false, 
                false, 
                false, 
                false
            )
        );
        setBackground(new Background(bgImg));
    }
}
