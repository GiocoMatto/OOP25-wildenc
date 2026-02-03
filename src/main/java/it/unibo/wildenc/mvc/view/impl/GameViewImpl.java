package it.unibo.wildenc.mvc.view.impl;

import java.util.Collection;

import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.mvc.controller.api.MapObjViewData;

import it.unibo.wildenc.mvc.view.api.GameView;
import it.unibo.wildenc.mvc.view.api.SpriteRender;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameViewImpl implements GameView {
    private Engine eg; // TODO: should be final?
    private final SpriteRender renderer;
    private Stage gameStage = new Stage(StageStyle.DECORATED);
    final Canvas canvas = new Canvas(1600, 900);

    public GameViewImpl() {
        renderer = new AnotherSimpleCircleRender();        
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

        final VBox vbox = new VBox();
        vbox.getChildren().add(canvas);
        gameStage.setScene(new Scene(vbox, 1600, 900));
        gameStage.centerOnScreen();
        gameStage.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSprites(Collection<MapObjViewData> mObj) {
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
    public void lost() {
        // JOptionPane.showMessageDialog(frame, "You lost!");
        System.exit(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pause() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pause'");
    }

}
