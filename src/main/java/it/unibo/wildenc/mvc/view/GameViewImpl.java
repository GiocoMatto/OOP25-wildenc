package it.unibo.wildenc.mvc.view;

import java.util.Collection;
import javax.swing.JOptionPane;
import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.view.api.GameView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameViewImpl implements GameView{
    private Engine eg;
    private final Stage stage = new Stage();

    @Override
    public void setEngine(Engine e) {
        this.eg = e;
    }

    @Override
    public void updateSprites(Collection<MapObject> mObj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateSprites'");
    }

    @Override
    public void won() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'won'");
    }

    @Override
    public void lost() {
        JOptionPane.showMessageDialog(frame, "You lost!");
        System.exit(0);
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pause'");
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'start'");
    }

    private class DrawingPanel extends Pane {
        
        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);
            // personalizzazioni
        }
    }

}
