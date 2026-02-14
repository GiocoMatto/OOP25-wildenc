package it.unibo.wildenc.mvc.view.api;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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

    /**
     * Gets the grass tile from the loaded file. This was made
     * to easily separate sprites to the background.
     * 
     * @return an {@link Image} corresponding to the grass tile.
     */
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
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP", 
        justification = "There is no need to create defensive copy of an Image, as it is heavy and useless in this case."
    )
    record Sprite(Image spriteImage, int rotationFrame, int currentFrame) { }
}
