package it.unibo.wildenc.mvc.view.api;

import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import javafx.scene.canvas.Canvas;

public interface SpriteRender {

    void setCanvas(Canvas c);

    void render(MapObjViewData objectData);

    void clean();
}