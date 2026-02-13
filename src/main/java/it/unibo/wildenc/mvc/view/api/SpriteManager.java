package it.unibo.wildenc.mvc.view.api;

import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import javafx.scene.image.Image;

/**
 * Interface for managing Sprites.
 */
public interface SpriteManager {
    /**
     * Method for getting a Sprite object for the current frame based
     * off its MapObjViewData.
     * 
     * @param frameCount the current frame
     * @param objData the MapObjViewData corresponding to the entity to load.
     * @return a Sprite object corresponding to the e
     */
    Sprite getSprite(int frameCount, MapObjViewData objData);

    Image getGrassTile();

    /**
     * Record for a Sprite. A Sprite is an image which contains
     * information about its rotation and its current frame on the
     * spritesheet.
     * 
     * @param spriteImage the {@link Image} for the sprite to use.
     * @param rotationFrame the rotation frame which the sprite is currently referring.
     * @param currentFrame the current frame for the animation.
     */
    record Sprite(Image spriteImage, int rotationFrame, int currentFrame) { }
}
