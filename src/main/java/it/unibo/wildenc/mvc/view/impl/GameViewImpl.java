package it.unibo.wildenc.mvc.view.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import it.unibo.wildenc.mvc.model.Game;
import it.unibo.wildenc.mvc.view.api.GameView;
import javafx.geometry.Pos;
import it.unibo.wildenc.mvc.view.api.ViewRenderer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameViewImpl implements GameView {
    private Engine eg; // TODO: should be final?
    private final ViewRenderer renderer;
    private Stage gameStage = new Stage(StageStyle.DECORATED);
    private final Canvas canvas = new Canvas(1600, 900);
    private Collection<MapObjViewData> backupColl = List.of();
    private boolean gameStarted = false;

    public GameViewImpl() {
        renderer = new ViewRendererImpl();        
        renderer.setCanvas(canvas);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEngine(Engine e) {
        this.eg = e;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        gameStage = new Stage();
        gameStage.setTitle("Wild Encounter");
        gameStage.setX(1600);
        gameStage.setY(600);

        final VBox root = new VBox();
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());

        root.getChildren().add(canvas);
        gameStage.setScene(new Scene(root, 1600, 900));
        gameStage.centerOnScreen();
        gameStage.show();
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
        renderer.clean();
        renderer.renderAll(mObj);
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
    public void pokedexView(Map<String, Integer> pokedexView) {
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
        Scene scene = new Scene(listView, 500, 500);
        this.gameStage.setScene(scene);
    }

    @Override
    public void menu() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'menu'");
    }

    @Override
    public void shop() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'shop'");
    }

}
