package it.unibo.wildenc.mvc.view.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.joml.Vector2d;
import org.joml.Vector2dc;
import java.util.Map;
import java.util.Objects;

import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import it.unibo.wildenc.mvc.controller.api.InputHandler.MovementInput;
import it.unibo.wildenc.mvc.model.Game;
import it.unibo.wildenc.mvc.model.Lobby;
import it.unibo.wildenc.mvc.view.api.GamePointerView;
import it.unibo.wildenc.mvc.view.api.GameView;
import it.unibo.wildenc.mvc.view.api.SoundManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import it.unibo.wildenc.mvc.view.api.ViewRenderer;
import it.unibo.wildenc.mvc.view.impl.components.LoseStackPane;
import it.unibo.wildenc.mvc.view.impl.components.PauseBox;
import it.unibo.wildenc.mvc.view.impl.components.PowerUpStackPane;
import it.unibo.wildenc.mvc.view.impl.roots.MenuView;
import it.unibo.wildenc.mvc.view.impl.roots.PokedexView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Basic view implementation with pointing system.
 */
public class GameViewImpl implements GameView, GamePointerView {
    private static final String PATH = "/images/menu/";
    private Engine engine;
    private final ViewRenderer renderer;
    private final ProgressBar experienceBar = new ProgressBar(0);
    private ProgressBar hpBar;
    private final Text levelText = new Text("LV 1");
    private StackPane powerUpWrapper = new StackPane();
    private VBox pauseMenu = new VBox();
    private Stage gameStage = new Stage(StageStyle.DECORATED);
    private Collection<MapObjViewData> backupColl = List.of();
    private boolean gameStarted;
    private Rectangle2D rec = Screen.getPrimary().getVisualBounds();
    private Region backgroundRegion;
    private final Canvas canvas = new Canvas(rec.getWidth(), rec.getHeight());
    private final SoundManager soundManager;

    //mappa associa wasd ai comandi MovementInput
    private final Map<KeyCode, MovementInput> keyToInputMap = Map.of(
        KeyCode.W, MovementInput.GO_UP,
        KeyCode.A, MovementInput.GO_LEFT,
        KeyCode.S, MovementInput.GO_DOWN,
        KeyCode.D, MovementInput.GO_RIGHT
    );

    private volatile double mouseX; // NOPMD: volatile is needed due to usign these variables in two separated threads.
    private volatile double mouseY; // NOPMD: volatile is needed due to usign these variables in two separated threads.

    /**
     * Creates a new view initializes audio manager and the sprite renderer.
     */
    public GameViewImpl() {
        renderer = new ViewRendererImpl();
        this.soundManager = new SoundManagerImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(final Lobby.PlayerType pt) {
        gameStage = new Stage();
        gameStage.setTitle("Wild Encounter");
        gameStage.setHeight(rec.getHeight() * Proportions.EightyFive.getPercent());
        gameStage.setWidth(rec.getWidth() * Proportions.EightyFive.getPercent());
        experienceBar.setPrefWidth(rec.getWidth() * Proportions.Half.getPercent());
        final Image icon = new Image(
            Objects.requireNonNull(
                getClass().getResource(PATH + "icon.png")
            ).toExternalForm()
        );
        gameStage.getIcons().add(icon);
        final Scene scene = new Scene(new StackPane());
        gameStage.setScene(scene);
        gameStage.setOnCloseRequest(e -> {
            soundManager.stopMusic();
            engine.unregisterView(this);
            gameStage.close();
        });
        this.gameStage.show();
        gameStage.toFront();
        gameStage.centerOnScreen();
        menu(pt);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setEngine(final Engine e) {
        this.engine = e;
    }

    private void switchRoot(final Parent root) {
        root.requestFocus();
        this.gameStage.getScene().setRoot(root);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showGame() {
        /* defining layout */
        final StackPane root = new StackPane();
        
        this.backgroundRegion = new Region();
        backgroundRegion.getStyleClass().add("game-background");
        backgroundRegion.setMouseTransparent(true);
        backgroundRegion.prefWidthProperty().bind(root.widthProperty());
        backgroundRegion.prefHeightProperty().bind(root.prefHeightProperty());
        this.renderer.setStyleToContainer(backgroundRegion, "css/style.css");
        root.getChildren().add(backgroundRegion);

        renderer.setCanvas(canvas);
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());
        canvas.setManaged(false);
        root.getChildren().add(canvas);

        final BorderPane ui = new BorderPane();
        ui.setPickOnBounds(false);
        ui.setMouseTransparent(true);
        root.getChildren().add(ui);

        // Experience Box
        final HBox expBox = new HBox(10);
        expBox.setAlignment(Pos.TOP_CENTER);
        expBox.getChildren().addAll(levelText, experienceBar);

        // HP Bar
        hpBar = new javafx.scene.control.ProgressBar(1.0);
        hpBar.setStyle("-fx-accent: red;");
        hpBar.setPrefWidth(200);
        hpBar.setFocusTraversable(false);

        final VBox hud = new VBox(5);
        hud.setAlignment(Pos.TOP_CENTER);
        hud.setPadding(new Insets(15));
        hud.setPickOnBounds(false);
        hud.getChildren().addAll(expBox, hpBar);

        ui.setTop(hud);

        /*
        * Action listeners 
        */
        canvas.setFocusTraversable(true);
        canvas.requestFocus();

        canvas.setOnMouseMoved(e -> {
            // Calcolo del mouse relativo al centro del Canvas (corretto per resize/fullscreen)
            double scale = canvas.getWidth() / 1600.0; 
            mouseX = (e.getX() / scale) - (1600.0 / 2.0);
            mouseY = (e.getY() / scale) - ((canvas.getHeight() / scale) / 2.0);
        });

        canvas.setOnKeyPressed(event -> {
            if (keyToInputMap.containsKey(event.getCode())) {
                engine.addInput(keyToInputMap.get(event.getCode()));
            }
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                engine.openViewPause();
            }
        });

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
        
        Platform.runLater(() -> switchRoot(root));
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
        final StackPane root = (StackPane) gameStage.getScene().getRoot();
        final var gameOverScreen = new LoseStackPane(engine, lostInfo);
        renderer.setStyleToContainer(gameOverScreen, "css/game_over.css");
        Platform.runLater(() -> {
            soundManager.stopMusic();
            root.getChildren().add(gameOverScreen);
            gameOverScreen.requestFocus();
            gameOverScreen.toFront();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void openPowerUp(final Set<Game.WeaponChoice> powerUps) {
        final StackPane root = (StackPane) gameStage.getScene().getRoot();
        powerUpWrapper = new PowerUpStackPane(root, powerUps, this::levelupHandler);
        powerUpWrapper.getStyleClass().add("powerup");
        renderer.setStyleToContainer(powerUpWrapper, "css/powerup.css");
        Platform.runLater(() -> {
            root.getChildren().add(powerUpWrapper);
            canvas.setFocusTraversable(false);
            powerUpWrapper.requestFocus();
            powerUpWrapper.toFront();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closePowerUp() {
        final StackPane root = (StackPane) gameStage.getScene().getRoot();
        Platform.runLater(() -> {
            root.getChildren().remove(powerUpWrapper);
            canvas.setFocusTraversable(true);
            renderer.setStyleToContainer(backgroundRegion, "css/style.css");
        });
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod") // Method is not unused.
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
    public void pokedexView(final Map<String, Integer> pokedexView) {
        Platform.runLater(() -> switchRoot(new PokedexView(engine, pokedexView)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void menu(final Lobby.PlayerType pt) {
        Platform.runLater(() -> switchRoot(new MenuView(engine, pt)));
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

    @Override
    public final void playSound(final String soundName) {
        soundManager.play(soundName);
    }

    @Override
    public final void pause() {
        Platform.runLater(() -> {
            final StackPane root = (StackPane) gameStage.getScene().getRoot();
            pauseMenu = new PauseBox(engine, soundManager);
            renderer.setStyleToContainer(pauseMenu, "css/pause.css");
            canvas.setFocusTraversable(false);
            root.getChildren().add(pauseMenu);
            pauseMenu.requestFocus();
        });
    }

    @Override
    public final void closePause() {
        final StackPane root = (StackPane) gameStage.getScene().getRoot();
        Platform.runLater(() -> {
            root.getChildren().remove(pauseMenu);
            canvas.setFocusTraversable(true);
            renderer.setStyleToContainer(backgroundRegion, "css/style.css");
        });
    }

    @Override
    public final void pauseMusic() {
        soundManager.pauseMusic();
    }

    @Override
    public final void resumeMusic() {
        soundManager.resumeMusic();
    }

    @Override
    public final void updateHealthBar(final double currentHealth, final double maxHealth) {
        if (hpBar != null && maxHealth > 0) {
            Platform.runLater(() -> hpBar.setProgress(currentHealth / maxHealth));
        }
    }

    private enum Proportions {
        EightyFive(0.85),
        Half(0.5);

        private final double percent;

        Proportions(final double p) {
            this.percent = p;
        }

        public double getPercent() {
            return percent;
        }
    }
}
