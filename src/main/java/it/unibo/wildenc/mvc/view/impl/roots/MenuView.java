package it.unibo.wildenc.mvc.view.impl.roots;

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

public class MenuView extends StackPane {

    private final static String PATH = "/images/menu/";
    private final static double HEIGHT_RATIO = 0.6;
    private final static double WIDTH_RATIO = 0.35;

    private final static Rectangle2D SCREEN = Screen.getPrimary().getVisualBounds();

    public MenuView(Engine engine, PlayerType pt) {
        final ImageView title = new ImageView(new Image(getClass().getResource(PATH + "title.png").toExternalForm()));
        title.setPreserveRatio(true);
        title.setFitWidth(400);
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
            .getResource(PATH + pt.name().toLowerCase() + ".png").toExternalForm()
        ));
        avatar.setFitWidth(175);
        avatar.setFitHeight(175);
        final HBox infoBar = new HBox(10);
        infoBar.setAlignment(Pos.CENTER);
        infoBar.setPadding(new Insets(30));
        infoBar.setStyle("-fx-background-color: #AEC6CF;");
        for (final var e : engine.getSelectablePlayers()) {
            final Button btnPoke = new Button(e.name());
            btnPoke.setOnAction(btn -> {
                engine.menu(e);
            });
            infoBar.getChildren().add(btnPoke);
        }
        final Button playBtn = new Button("Gioca");
        playBtn.setPrefHeight(50);
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
