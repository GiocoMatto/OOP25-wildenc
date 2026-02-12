package it.unibo.wildenc.mvc.view.impl;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import java.util.Map;
import it.unibo.wildenc.mvc.model.Game;
import it.unibo.wildenc.mvc.model.controller.api.Engine;
import it.unibo.wildenc.mvc.model.controller.api.MapObjViewData;
import it.unibo.wildenc.mvc.model.controller.api.InputHandler.MovementInput;
import it.unibo.wildenc.mvc.view.api.GamePointerView;
import it.unibo.wildenc.mvc.view.api.GameView;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import it.unibo.wildenc.mvc.view.api.ViewRenderer;
import javafx.scene.Parent;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameViewImpl implements GameView, GamePointerView {
    private static final String PATH = "/images/menu/";
    private Engine engine; // TODO: should be final?
    private final ViewRenderer renderer;
    private final Canvas canvas = new Canvas(1600, 900);
    private final ProgressBar experienceBar = new ProgressBar(0);
    private final Text levelText = new Text("LV 1");
    private StackPane powerUpWrapper = new StackPane();
    private Stage gameStage = new Stage(StageStyle.DECORATED);
    private Collection<MapObjViewData> backupColl = List.of();
    private boolean gameStarted = false;
    private Rectangle2D rec = Screen.getPrimary().getVisualBounds();

    //mappa associa wasd ai comandi MovementInput
    private final Map<KeyCode, MovementInput> keyToInputMap = Map.of(
        KeyCode.W, MovementInput.GO_UP,
        KeyCode.A, MovementInput.GO_LEFT,
        KeyCode.S, MovementInput.GO_DOWN,
        KeyCode.D, MovementInput.GO_RIGHT
    );
    private volatile double mouseX;
    private volatile double mouseY;

    public GameViewImpl() {
        renderer = new ViewRendererImpl();        
    }

    @Override
    public void start(final Game.PlayerType pt) {
        gameStage = new Stage();
        gameStage.setTitle("Wild Encounter");
        gameStage.setHeight(rec.getHeight() * 0.85);
        gameStage.setWidth(rec.getWidth() * 0.85);
        experienceBar.setPrefWidth(rec.getWidth() * 0.5);
        final Scene scene = new Scene(new StackPane());
        gameStage.setScene(scene);
        gameStage.setOnCloseRequest((e) -> {
            engine.unregisterView(this);
            gameStage.close();
        });
        this.gameStage.show();
        gameStage.toFront();
        gameStage.centerOnScreen();
        switchRoot(menu(pt));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setEngine(final Engine e) {
        this.engine = e;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void switchRoot(final Parent root) {
        root.requestFocus();
        this.gameStage.getScene().setRoot(root);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parent game() {
        /* defining layout */
        renderer.setCanvas(canvas);
        final StackPane root = new StackPane();
        this.renderer.setContainer(root);
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());
        root.getChildren().add(canvas);
        final BorderPane ui = new BorderPane();
        final HBox expBox = new HBox();
        root.getChildren().add(ui);
        ui.setPickOnBounds(false);
        expBox.setAlignment(Pos.TOP_CENTER);
        expBox.setPadding(new Insets(15));
        expBox.getChildren().add(levelText);
        expBox.getChildren().add(experienceBar);
        ui.setTop(expBox);
        canvas.setManaged(false); // canvas should be indipendent
        canvas.setFocusTraversable(true);
        canvas.requestFocus();

        /*
         * Action listeners 
         */
        canvas.setOnMouseMoved(e -> {
            mouseX = e.getSceneX() - (gameStage.getWidth() / 2);
            mouseY = e.getSceneY() - (gameStage.getHeight() / 2);
        });        
        canvas.setOnKeyPressed(event -> {
            if (keyToInputMap.containsKey(event.getCode())) {
                engine.addInput(keyToInputMap.get(event.getCode()));
            }
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                engine.setPause(true);
            }
            if (event.getCode().equals(KeyCode.ENTER)) {
                engine.setPause(false);
            }
        });
        //listener tasto rilasciato
        canvas.setOnKeyReleased(event -> {
            if (keyToInputMap.containsKey(event.getCode())) {
                engine.removeInput(keyToInputMap.get(event.getCode()));
            }
        });
        canvas.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                engine.removeAllInput();
            }
        });
        return root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSprites(final Collection<MapObjViewData> mObj) {
        if (!gameStarted) {
            canvas.widthProperty().addListener((obs, oldVal, newVal) -> updateSprites(backupColl));
            canvas.heightProperty().addListener((obs, oldVal, newVal) -> updateSprites(backupColl));
            this.gameStarted = true;
        }
        this.backupColl = mObj;
        Platform.runLater(() -> {
            renderer.clean();
            renderer.renderAll(mObj);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2dc getMousePointerInfo() {
        return new Vector2d(mouseX, mouseY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void lost(final Map<String, Integer> lostInfo) {
        // JOptionPane.showMessageDialog(frame, "You lost!");
        // System.exit(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pause() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pause'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void openPowerUp(final Set<Game.WeaponChoice> powerUps) {
        final StackPane root = (StackPane) gameStage.getScene().getRoot();
        final Label text = new Label("Sblocca arma o potenziamento:");
        final ListView<String> listView = new ListView<>();
        final VBox box = new VBox(5, text, listView);
        box.setAlignment(Pos.CENTER);
        powerUpWrapper = new StackPane(box);
        powerUpWrapper.setPadding(new Insets(15));
        powerUpWrapper.setStyle("-fx-background-color: #AEC6CF;");
        Platform.runLater(() -> {
            root.getChildren().add(powerUpWrapper);
            powerUpWrapper.toFront();
            listView.requestFocus();
        });
        listView.getItems().addAll(powerUps.stream().map(e -> e.toString()).toList());
        listView.getSelectionModel().selectFirst();
        listView.setFixedCellSize(26);
        listView.setPrefHeight(powerUps.size() * 26 + 2);
        listView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                levelupHandler(powerUps, listView);
            }
        });
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                levelupHandler(powerUps, listView);
            }
        });
        VBox.setVgrow(listView, Priority.ALWAYS);

        powerUpWrapper.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        powerUpWrapper.prefWidthProperty().bind(root.widthProperty().multiply(0.15));
        powerUpWrapper.setMaxWidth(rec.getWidth() * 0.15);
    }

    private void levelupHandler(final Set<Game.WeaponChoice> powerUps, final ListView<String> listView) {
        engine.onLeveUpChoise(
            powerUps.stream()
                .filter(wc -> wc.toString().equals(
                    listView.getSelectionModel().getSelectedItem()
                ))
                .findFirst()
                .get()
                .name()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parent pokedexView(final Map<String, Integer> pokedexView) {
        final Button goToMenu = new Button("Torna al menu" + (pokedexView.size() == 0 ? " (Pokedex vuoto)" : ""));
        goToMenu.setOnAction(e -> engine.menu(engine.getPlayerTypeChoise()));
        goToMenu.setMaxWidth(Double.MAX_VALUE);
        final ListView<Map.Entry<String, Integer>> listView = new ListView<>();
        listView.getItems().addAll(pokedexView.entrySet());
        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(final Map.Entry<String, Integer> entry, final boolean empty) {
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
        final StackPane root = new StackPane(box);
        listView.setFixedCellSize(26);
        listView.setPrefHeight(pokedexView.size() * 26 + 2);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: #AEC6CF;");
        box.setMaxWidth(rec.getWidth() * 0.35);
        box.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        box.prefWidthProperty().bind(root.widthProperty().multiply(0.35));
        VBox.setVgrow(listView, Priority.ALWAYS);
        box.setMaxWidth(rec.getWidth() * 0.35);
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
        root.setBackground(new Background(bgImg));
        return root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parent menu(final Game.PlayerType pt) {
        final StackPane root = new StackPane();
        final VBox box = new VBox();
        root.getChildren().add(box);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: lightblue;");
        box.setAlignment(Pos.CENTER);
        box.setMaxHeight(rec.getHeight() * 0.6);
        box.setMaxWidth(rec.getWidth() * 0.35);
        box.prefWidthProperty().bind(root.widthProperty().multiply(0.35));
        box.prefHeightProperty().bind(root.heightProperty().multiply(0.6));
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
        for (final var e : engine.getPlayerType()) {
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
        root.setBackground(new Background(bgImg));
        final Region spacer1 = new Region();
        final Region spacer2 = new Region();
        VBox.setVgrow(spacer1, Priority.ALWAYS);
        VBox.setVgrow(spacer2, Priority.ALWAYS);
        centerBox.setFillWidth(true);
        box.getChildren().addAll(spacer1, centerBox, spacer2, downMenu);
        return root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shop() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'shop'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closePowerUp() {
        final StackPane root = (StackPane) gameStage.getScene().getRoot();
        Platform.runLater(() -> root.getChildren().remove(powerUpWrapper));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateExpBar(
        final int exp, 
        final int level, 
        final int neededExp
    ) {
        experienceBar.setProgress((double) exp / (double) neededExp);
        levelText.setText("LV "
            .concat(Integer.toString(level))
            .concat(" xp ")
            .concat(Integer.toString(exp))
            .concat(" / ")
            .concat(Integer.toString(neededExp)));
    }

}
