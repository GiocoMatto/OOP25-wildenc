package it.unibo.wildenc.mvc.view.api;

import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import javafx.scene.image.Image;

public interface SpriteManager {
    record Sprite(Image spriteImage, int rotationFrame, int currentFrame) {}

    /**
     * Method for getting a Sprite object for the current frame based
     * off its MapObjViewData.
     * 
     * @param frameCount the current frame
     * @param objData the MapObjViewData corresponding to the entity to load.
     * @return a Sprite object corresponding to the e
     */
    Sprite getSprite(int frameCount, MapObjViewData objData);

    /**
     * Methods for getting the grass tile. This is isolated as it isn't an
     * entity to handle.
     * 
     * @return the image corresponding to the grass tile.
     */
    Image getGrassTile();
}
