package it.unibo.wildenc.mvc.controller.api;

import java.util.List;

import it.unibo.wildenc.mvc.controller.api.InputHandler.MovementInput;
import it.unibo.wildenc.mvc.model.Game;
import it.unibo.wildenc.mvc.model.Lobby;
import it.unibo.wildenc.mvc.view.api.GameView;

/**
 * Responsible of handling the main GameLoop and communicate with Views and the Model.
 */
public interface Engine {

    /**
     * Starts the engine for a specific player type.
     */
    void start();

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
     * Start the game loop.
     */
    void startGameLoop();

    /**
     * Show the menu.
     * 
     * @param player the base player type selected in the lobby.
     */
    void menu(Lobby.PlayerType player);

    /**
     * Show the shop.
     */
    void shop();

    /**
     * Show the Pokedex.
     */
    void pokedex();

    /**
     * Stops the engine.
     */
    void stopEngine();

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
     * Gets a list of selectable players.
     * 
     * @return A {@link List} of selectable players as {@link Game.PlayerType}.
     */
    List<Lobby.PlayerType> getSelectablePlayers();

    /**
     * Method for selecting a player in the main menu.
     * 
     * @return the selected player in form of a PlayerType.
     */
    Lobby.PlayerType getPlayerTypeChoise();

    /**
     * Show the view for set game in pause.
     */
    void openViewPause();

    /**
     * Restart the game by the pause.
     */
    void closeViewPause();
}
