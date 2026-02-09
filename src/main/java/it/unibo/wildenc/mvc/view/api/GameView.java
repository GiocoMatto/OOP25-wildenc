package it.unibo.wildenc.mvc.view.api;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import it.unibo.wildenc.mvc.model.Game;

public interface GameView {

    /**
     * Start the view.
     */
    void game();

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
    void lost(Map<String, Integer> lostInfo);

    /**
     * Display the pause screen.
     */
    void pause();

    /**
     * Display the menu.
     */
    void menu(Game.PlayerType pt);

    /**
     * Display the shop.
     */
    void shop();

    /**
     * Display the list of power up.
     * @param powerUps List of power up.
     * @return the player chose.
     */
    String powerUp(Set<Game.WeaponChoice> powerUps);

    /**
     * Display the list of pokemok killed.
     * 
     * @param pokedexView the pokemon killed.
     */
    void pokedexView(Map<String, Integer> pokedexView);
}
