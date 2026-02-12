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
import javafx.application.Application;
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
import javafx.scene.layout.HBox;
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
    private final SoundManager soundManager;

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
        this.soundManager = new SoundManager();    
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
            soundManager.stopMusic();
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
        final StackPane root = new StackPane();
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
                engine.openViewPause();
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
        soundManager.playMusic("theme.mp3");
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
        
        Platform.runLater(() -> {
            soundManager.stopMusic();
            
            VBox root = new VBox(20); //per il layout
            root.setAlignment(Pos.CENTER);
            root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");

            Label title = new Label("GAME OVER"); //testo
            title.setStyle("-fx-text-fill: red; -fx-font-size: 60px; -fx-font-weight: bold; -fx-font-family: 'Arial';");

            VBox statsBox = new VBox(5);
            statsBox.setAlignment(Pos.CENTER);
            Label subTitle = new Label("Statistiche Partita");
            subTitle.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-underline: true;");
            statsBox.getChildren().add(subTitle);

            // Itera sulla mappa per creare le label
            if (lostInfo != null && !lostInfo.isEmpty()) {
                lostInfo.forEach((key, value) -> {
                    String labelText = key.split(":")[1]+ ": " + value;
                    Label statLabel = new Label(labelText);
                    statLabel.setStyle("-fx-text-fill: lightgray; -fx-font-size: 16px;");
                    statsBox.getChildren().add(statLabel);
                });
            } else {
                Label noStats = new Label("Nessuna statistica disponibile.");
                noStats.setStyle("-fx-text-fill: gray;");
                statsBox.getChildren().add(noStats);
            }

            //Pulsanti
            Button btnMenu = new Button("Torna al Menu");
            btnMenu.setStyle("-fx-font-size: 18px; -fx-padding: 10 20 10 20;");
            btnMenu.setOnAction(e -> {
                //riapre menu usando l'ultimo personaggio scelto
                engine.menu(engine.getPlayerTypeChoise());
            });

            Button btnExit = new Button("Esci dal Gioco");
            btnExit.setStyle("-fx-font-size: 18px; -fx-padding: 10 20 10 20;");
            btnExit.setOnAction(e -> Platform.exit());

            //aggiungo al root
            root.getChildren().addAll(title, statsBox, btnMenu, btnExit);

            switchRoot(root);

        });
    }

    @Override
    public void openPowerUp(final Set<Game.WeaponChoice> powerUps) {
        StackPane root = (StackPane) gameStage.getScene().getRoot();

        Label text = new Label("Scegli un arma nuova o un Potenziamento");
        ListView<String> listView = new ListView<>();
        VBox box = new VBox(10, text, listView);
        StackPane wrapper = new StackPane(box);
        
        Platform.runLater(() -> {
            root.getChildren().add(wrapper);
            wrapper.toFront();
            listView.requestFocus();
        });

        listView.getItems().addAll(powerUps.stream().map(e -> e.name()).toList());
        listView.getSelectionModel().selectFirst();
        
        listView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                engine.onLeveUpChoise(listView.getSelectionModel().getSelectedItem());
            }
        });
        // listView.setOnMouseClicked(event -> {
        //     if (event.getClickCount() == 2) {
        //         engine.onLeveUpChoise(listView.getSelectionModel().getSelectedItem());
        //     }
        // });
        box.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        wrapper.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        wrapper.prefWidthProperty().bind(root.widthProperty().multiply(0.35));
        wrapper.prefHeightProperty().bind(root.heightProperty().multiply(0.6));
    }

    // private void confirmSelection(final Stage stage, final ListView<String> listView) {
    //     final String selected = listView.getSelectionModel().getSelectedItem();
    //     if (selected != null) {
    //         System.out.println("Hai scelto: " + selected);
    //         stage.close();
    //     }
    // }

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
                Label img = new Label("Immagine: " + entry.getKey().split(":")[1]);
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

    @Override
    public void closePowerUp() {
        StackPane root = (StackPane) gameStage.getScene().getRoot();
        Platform.runLater(() -> root.getChildren().remove(1));
    }

    @Override
    public void playSound(String soundName) {
        soundManager.play(soundName);
    }

    @Override
    public void pause() {
        Platform.runLater(() ->{
            StackPane root = (StackPane)gameStage.getScene().getRoot();

            VBox pauseMenu = new VBox(20);
            pauseMenu.setAlignment(Pos.CENTER);
            pauseMenu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);"); //nero 70% trasp

            Label title = new Label("PAUSA");
            title.setStyle("-fx-text-fill: white; -fx-font-size: 50px; -fx-font-weight: bold;");

            //pulsanti riprendi e torna al menu
            Button resumeBtn = new Button("Riprendi");
            resumeBtn.setStyle("-fx-font-size: 20px; -fx-padding: 10 20;");
            resumeBtn.setOnAction(e -> {
                engine.closeViewPause();
                //root.getChildren().remove(pauseMenu);
                //engine.setPause(false);//toglie pausa dall'engine
            });

            Button exitBtn = new Button("Torna al Menu");
            exitBtn.setStyle("-fx-font-size: 20px; -fx-padding: 10 20;");
            exitBtn.setOnAction(e -> {
                soundManager.stopMusic(); //ferma musica background
                engine.close();
                engine.menu(engine.getPlayerTypeChoise()); //torna al menu principale
            });

            pauseMenu.getChildren().addAll(title, resumeBtn, exitBtn);
            root.getChildren().add(pauseMenu);
            pauseMenu.requestFocus();//da il focus al menu di pausa

        });
    }

    @Override
    public void closePause() {
        StackPane root = (StackPane)gameStage.getScene().getRoot();

        Platform.runLater(() -> root.getChildren().remove(1));
    }

    @Override
    public void pauseMusic() {
        soundManager.pauseMusic();
    }

    @Override
    public void resumeMusic() {
        soundManager.resumeMusic();
    }

}
