package it.unibo.wildenc.mvc.view.api;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import it.unibo.wildenc.mvc.controller.api.Engine;
import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import it.unibo.wildenc.mvc.model.Game;
import it.unibo.wildenc.mvc.model.Lobby;

/** 
 * Game view
 */
public interface GameView {

    /**
     * Initialize and show the view. It can be for example a main menu or directly the game.
     * 
     * @param pt the default player informations, such as max hp, 
     *           start weapons and other that could be showed in the main menu. 
     */
    void start(Lobby.PlayerType pt);

    /**
     * Show the main gameplay view.
     */
    void showGame();

    /**
     * Set an Engine (Controller) for this view.
     * 
     * @param e the Engine that will control the view.
     */
    void setEngine(Engine e);

    /**
     * Update all the sprites on the screen.
     * 
     * @param mObjs a {@link Collection} of {@link MapObjViewData}s that will 
     *              have the infos necessary to be rendered.
     */
    void updateSprites(Collection<MapObjViewData> mObjs);

    /**
     * Update experience bar and level during the game.
     * 
     * @param exp the current player's experience
     * @param level the player's level
     * @param neededExp the player's needed exp to level up.
     */
    void updateExpBar(int exp, int level, int neededExp);

    /**
     * Update health bar during the game.
     * 
     * @param currentHealth player's current health.
     * @param maxHealth player's max healt.
     */
    void updateHealthBar(double currentHealth, double maxHealth);

    /**
     * Display the loss screen.
     * 
     * @param lostInfo the killed enemies information.
     */
    void lost(Map<String, Integer> lostInfo);

    /**
     * Display the pause screen.
     */
    void pause();

    /**
     * Display the menu.
     * 
     * @param pt The player to 
     */
    void menu(Lobby.PlayerType pt);

    /**
     * Display the shop.
     */
    void shop();

    /**
     * Display the list of power up.
     * 
     * @param powerUps List of power up.
     */
    void openPowerUp(Set<Game.WeaponChoice> powerUps);

    /**
     * Close the power up screen.
     */
    void closePowerUp();

    /**
     * Display the list of pokemok killed.
     * 
     * @param pokedexView the pokemon killed.
     */
    void pokedexView(Map<String, Integer> pokedexView);

    /**
     * Plays a sound effect.
     * 
     * @param soundName the name of the sound to play
     */
    void playSound(String soundName);

    /**
     * Close the pause screen.
     */
    void closePause();

    /**
     * Pauses background music
     */
    void pauseMusic();

    /**
     * resumes background music
     */
    void resumeMusic();

}
