package it.unibo.wildenc.mvc.view.api;

import java.util.Collection;

import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Region;

public interface ViewRenderer {
    void setCanvas(Canvas c);

    void setStyleToContainer(Region cont, String css);

    void renderAll(Collection<MapObjViewData> objectDatas);

    void clean();

    void updateCamera(MapObjViewData playerObj);
}