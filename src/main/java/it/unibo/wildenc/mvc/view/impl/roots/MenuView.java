package it.unibo.wildenc.mvc.view.impl.roots;

import java.util.Locale;

import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.mvc.model.Lobby.PlayerType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * View of menu.
 */
public final class MenuView extends StackPane {
    private static final Rectangle2D SCREEN = Screen.getPrimary().getVisualBounds();
    private static final String PATH = "/images/menu/";
    private static final double HEIGHT_RATIO = 0.6;
    private static final double WIDTH_RATIO = 0.35;
    private static final int FIT_WIDTH = 400;
    private static final int PADDING = 30;
    private static final int IMAGE_SIZE = 175;
    private static final int HEIGHT_PLAY = 50;

    /**
     * Create the view of menu.
     * 
     * @param engine The controller
     * @param pt The player selected by default or by the user.
     */
    public MenuView(final Engine engine, final PlayerType pt) {
        final ImageView title = new ImageView(new Image(getClass().getResource(PATH + "title.png").toExternalForm()));
        title.setPreserveRatio(true);
        title.setFitWidth(FIT_WIDTH);
        final VBox box = new VBox();
        getChildren().add(box);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: lightblue;");
        box.setAlignment(Pos.CENTER);
        box.setMaxHeight(SCREEN.getHeight() * HEIGHT_RATIO);
        box.setMaxWidth(SCREEN.getWidth() * WIDTH_RATIO);
        box.prefWidthProperty().bind(widthProperty().multiply(WIDTH_RATIO));
        box.prefHeightProperty().bind(heightProperty().multiply(HEIGHT_RATIO));
        /* start game play */
        final ImageView avatar = new ImageView(new Image(getClass()
            .getResource(PATH + pt.name().toLowerCase(Locale.ENGLISH) + ".png").toExternalForm()
        ));
        avatar.setFitWidth(IMAGE_SIZE);
        avatar.setFitHeight(IMAGE_SIZE);
        final HBox infoBar = new HBox(10);
        infoBar.setAlignment(Pos.CENTER);
        infoBar.setPadding(new Insets(PADDING));
        infoBar.setStyle("-fx-background-color: #AEC6CF;");
        for (final var e : engine.getSelectablePlayers()) {
            final Button btnPoke = new Button(e.name());
            btnPoke.setOnAction(btn -> {
                engine.menu(e);
            });
            infoBar.getChildren().add(btnPoke);
        }
        final Button playBtn = new Button("Gioca");
        playBtn.setPrefHeight(HEIGHT_PLAY);
        playBtn.setOnAction(e -> engine.startGameLoop());
        final VBox centerBox = new VBox(15, avatar, infoBar, playBtn);
        centerBox.setAlignment(Pos.CENTER);
        /* oter buttons */
        final Button boxBtn = new Button("POKEDEX");
        boxBtn.setOnAction(e -> engine.pokedex());
        final HBox downMenu = new HBox(boxBtn);
        boxBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(boxBtn, Priority.ALWAYS);
        playBtn.setMaxWidth(Double.MAX_VALUE);
        final Image img = new Image(getClass().getResource(PATH + "background.jpg").toExternalForm());
        final BackgroundImage bgImg = new BackgroundImage(
            img, 
            BackgroundRepeat.NO_REPEAT, 
            BackgroundRepeat.NO_REPEAT, 
            BackgroundPosition.CENTER, 
            new BackgroundSize(
                BackgroundSize.AUTO, 
                BackgroundSize.AUTO, 
                false, 
                false, 
                true, 
                true
            )
        );
        setBackground(new Background(bgImg));
        final Region spacer1 = new Region();
        final Region spacer2 = new Region();
        VBox.setVgrow(spacer1, Priority.ALWAYS);
        VBox.setVgrow(spacer2, Priority.ALWAYS);
        centerBox.setFillWidth(true);
        box.getChildren().addAll(title, spacer1, centerBox, spacer2, downMenu);
    }

}
