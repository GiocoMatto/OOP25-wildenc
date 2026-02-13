package it.unibo.wildenc.mvc.view.api;

import java.util.Collection;

import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Region;

/**
 * Interface for a ViewRenderer. A ViewRenderer
 * is needed when something has to be drawn on the view.
 */
public interface ViewRenderer {
    /**
     * Sets the canvas which the renderer has to work on.
     * 
     * @param c the canvas to be set.
     */
    void setCanvas(Canvas c);

    /**
     * Sets the container which the renderer has to work on.
     * 
     * @param cont the container to be set.
     */
    void setContainer(Region cont);

    /**
     * Renders all sprites which needs to be loaded sequentially.
     * 
     * @param objectDatas a {@link Collection} of {@link MapObjectViewData} containing
     *      the data of every entity to be drawn on the view.
     */
    void renderAll(Collection<MapObjViewData> objectDatas);

    /**
     * Utility for cleaning the view while passing to the next frame.
     */
    void clean();
}
