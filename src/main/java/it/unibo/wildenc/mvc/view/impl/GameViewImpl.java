package it.unibo.wildenc.mvc.view.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.mvc.controller.api.InputHandler.MovementInput;
import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import java.util.Map;
import it.unibo.wildenc.mvc.model.Game;
import it.unibo.wildenc.mvc.view.api.GamePointerView;
import it.unibo.wildenc.mvc.view.api.GameView;
import javafx.application.Platform;
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
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameViewImpl implements GameView, GamePointerView {
    private Engine engine; // TODO: should be final?

    private static final int PROPORTION = 5;

    private final ViewRenderer renderer;
    private Stage gameStage = new Stage(StageStyle.DECORATED);
    private final Canvas canvas = new Canvas(1600, 900);
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
        //ngine.menu(Game.PlayerType.CHARMANDER);
        Scene scene = new Scene(new StackPane());
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
    public void setEngine(Engine e) {
        this.engine = e;
    }

    public void switchRoot(final Parent root) {
        root.requestFocus();
        this.gameStage.getScene().setRoot(root);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parent game() {
        renderer.setCanvas(canvas);
        final VBox root = new VBox();
        this.renderer.setContainer(root);
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());

        canvas.setOnMouseMoved(e -> {
            mouseX = e.getSceneX() - (gameStage.getWidth() / 2);
            mouseY = e.getSceneY() - (gameStage.getHeight() / 2);
        });

        root.getChildren().add(canvas);

        //final Scene scene = new Scene(root, 1600, 900);
        //listener tasto premuto
        canvas.setFocusTraversable(true);
        canvas.requestFocus();
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
    public void updateSprites(Collection<MapObjViewData> mObj) {
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

    @Override
    public Vector2dc getMousePointerInfo() {
        return new Vector2d(mouseX, mouseY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void won() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'won'");
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

    @Override
    public String powerUp(final Set<Game.WeaponChoice> powerUps) {
        final Stage powerUpStage = new Stage();
        powerUpStage.setTitle("Scegli un arma nuova o un Potenziamento");
        powerUpStage.initModality(Modality.APPLICATION_MODAL);
        powerUpStage.initOwner(this.gameStage);
        ListView<String> listView = new ListView<>();
        listView.getItems().addAll(powerUps.stream().map(e -> e.name()).toList());
        listView.getSelectionModel().selectFirst();
        listView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                confirmSelection(powerUpStage, listView);
            }
        });
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                confirmSelection(powerUpStage, listView);
            }
        });
        VBox root = new VBox(listView);
        Scene scene = new Scene(root, 250, 200);
        powerUpStage.setScene(scene);
        powerUpStage.showAndWait();
        return listView.getSelectionModel().getSelectedItem();
    }

    private void confirmSelection(final Stage stage, final ListView<String> listView) {
        final String selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("Hai scelto: " + selected);
            stage.close();
        }
    }

    @Override
    public Parent pokedexView(Map<String, Integer> pokedexView) {
        Button goToMenu = new Button("Torna al menù");
        goToMenu.setOnAction(e -> engine.menu(engine.getPlayerTypeChoise()));
        ListView<Map.Entry<String, Integer>> listView = new ListView<>();
        listView.getItems().addAll(pokedexView.entrySet());
        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Map.Entry<String, Integer> entry, boolean empty) {
                super.updateItem(entry, empty);
                if (empty || entry == null) {
                    setGraphic(null);
                    return;
                }
                Label img = new Label("Immagine: " + entry.getKey());
                Label kills = new Label("Uccisioni: " + entry.getValue());
                HBox row = new HBox(15, img, kills);
                row.setAlignment(Pos.CENTER_LEFT);
                setGraphic(row);
            }
        });
        VBox root = new VBox(goToMenu, listView);
        return root;
    }

    @Override
    public Parent menu(final Game.PlayerType pt) {
        StackPane root = new StackPane();
        VBox box = new VBox();
        root.getChildren().add(box);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: lightblue;");
        box.setAlignment(Pos.CENTER);
        box.setMaxHeight(rec.getHeight() * 0.6);
        box.setMaxWidth(rec.getWidth() * 0.35);
        box.prefWidthProperty().bind(root.widthProperty().multiply(0.35));
        box.prefHeightProperty().bind(root.heightProperty().multiply(0.6));
        /* top bar statistic */
        Label xp = new Label("XP");
        Label level = new Label("LVL");
        Label wc = new Label("WC");
        HBox topBar = new HBox(20, xp, level, wc);
        topBar.setAlignment(Pos.CENTER);
        /* start game play */
        Label avatar = new Label(pt.name());
        avatar.setMinSize(120, 120);
        avatar.setStyle("-fx-border-color: black");
        HBox infoBar = new HBox(10);
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
        Button playBtn = new Button("Gioca");
        playBtn.setPrefHeight(50);
        playBtn.setOnAction(e -> engine.startGameLoop());
        VBox centerBox = new VBox(15, avatar, infoBar, playBtn);
        centerBox.setAlignment(Pos.CENTER);
        /* oter buttons */
        Button boxBtn = new Button("POKEDEX");
        boxBtn.setOnAction(e -> engine.pokedex());
        Button shopBtn = new Button("SHOP");
        HBox downMenu = new HBox(10, boxBtn, shopBtn);
        boxBtn.setMaxWidth(Double.MAX_VALUE);
        shopBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(boxBtn, Priority.ALWAYS);
        HBox.setHgrow(shopBtn, Priority.ALWAYS);
        playBtn.setMaxWidth(Double.MAX_VALUE);
        Image img = new Image(getClass().getResource("/sprites/background.jpg").toExternalForm(), 300, 300, true, true);
        BackgroundImage bgImg = new BackgroundImage(
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
        Region spacer1 = new Region();
        Region spacer2 = new Region();
        VBox.setVgrow(spacer1, Priority.ALWAYS);
        VBox.setVgrow(spacer2, Priority.ALWAYS);
        centerBox.setFillWidth(true);
        box.getChildren().addAll(topBar, spacer1, centerBox, spacer2, downMenu);
        return root;
    }

    @Override
    public void shop() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'shop'");
    }

}
