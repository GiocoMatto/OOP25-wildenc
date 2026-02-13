package it.unibo.wildenc.mvc.controller.api;

import java.util.List;

import it.unibo.wildenc.mvc.controller.api.InputHandler.MovementInput;
import it.unibo.wildenc.mvc.model.Game;
import it.unibo.wildenc.mvc.view.api.GameView;

/**
 * Responsible of handling the main GameLoop and communicate with Views and the Model.
 */
public interface Engine {

    /**
     * Starts the engine for a specific player type.
     * 
     * @param pt the selected player type.
     */
    void start(Game.PlayerType pt);

    /**
     * Accept the movement of the player to add.
     * 
     * @param movement the movemente of type {@link MovementInput}.
     */
    void addInput(MovementInput movement);

    /**
     * Accept the movemet of the player to remove.
     * 
     * @param movement the movemente of type {@link MovementInput}.
     */
    void removeInput(MovementInput movement);

    /**
     * Remove all input.
     */
    void removeAllInput();

    /**
     * Select the weapon to unlock or levelup.
     * 
     * @param choise the choise of the player.
     */
    void onLeveUpChoise(String choise);

    /**
     * Start the game loopl.
     */
    void startGameLoop();

    /**
     * Show the menu.
     * 
     * @param pt the player type.
     */
    void menu(Game.PlayerType pt);

    /**
     * Show the shop.
     */
    void shop();

    /**
     * Show the Pokedex.
     */
    void pokedex();

    /**
     * Close the game and save the data.
     */
    void close();

    /**
     * Register the views.
     * 
     * @param gv view to register.
     */
    void registerView(GameView gv);

    /**
     * Remove the view from the views handled by this engine.
     * 
     * @param gv the view to remove.
     */
    void unregisterView(GameView gv);

    /**
     * Lists all of the available player types.
     * 
     * @return a {@link List} of available PlayerTypes.
     */
    List<Game.PlayerType> getPlayerType();

    /**
     * Returns the chosen player type.
     * 
     * @return the chosen player type.
     */
    Game.PlayerType getPlayerTypeChoise();

    /**
     * Show the view for set game in pause.
     */
    void openViewPause();

    /**
     * Restart the game by the pause.
     */
    void closeViewPause();
}
