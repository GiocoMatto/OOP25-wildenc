package it.unibo.wildenc.mvc.view.api;

import java.util.Collection;
import java.util.List;

import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.mvc.controller.api.MapObjViewData;

public interface GameView {

    /**
     * Start the view.
     */
    void start();

    /**
     * Set an Engine (Controller) for this view.
     * 
     * @param e the Engine that will control the view.
     */
    void setEngine(Engine e);

    /**
     * Update all the sprites on the screen.
     * 
     * @param mObjs a {@link Collection} of {@link MapObjViewData}s that will have the infos necessary to be rendered.
     */
    void updateSprites(Collection<MapObjViewData> mObjs);

    /**
     * Display the win screen.
     */
    void won();

    /**
     * Display the loss screen.
     */
    void lost();

    /**
     * Display the pause screen.
     */
    void pause();

    /**
     * Display the list of power up.
     * @param powerUps List of power up.
     * @return the player chose.
     */
    String powerUp(final List<String> powerUps);

}
