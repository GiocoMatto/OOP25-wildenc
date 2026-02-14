package it.unibo.wildenc.mvc.controller.impl;

import java.util.Set;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.controller.api.InputHandler;
import it.unibo.wildenc.util.Utilities;

/**
 * Class for handling different types of inputs.
 */
public class InputHandlerImpl implements InputHandler {

    private volatile boolean isGamePaused;
    private volatile boolean isGameClosable;

    /**
     * Checks wheter the game is paused.
     * 
     * @return true if the game is paused, false otherwise.
     */
    @Override
    public boolean isPaused() {
        return this.isGamePaused;
    }

    /**
     * Check whether the game is in a state which can
     * be closed.
     * 
     * @return true if the game can be closed, false otherwise.
     */
    @Override
    public boolean isClosable() {
        return this.isGameClosable;
    }

    /**
     * Method for handling movements based off a {@link Set}
     * of {@link MovementInput}. The result is a {@link Vector2dc}
     * which is obtained by summing the singular movement vectors
     * corresponding to each MovementInput.
     * 
     * @return a {@link Vector2dc} representing the effective direction, or
     *      an empty one if no direction was processed.
     */
    @Override
    public Vector2dc handleMovement(final Set<MovementInput> movementCommands) {
        final Vector2d effectiveMovementVersor = new Vector2d(0, 0);
        synchronized (movementCommands) {
            movementCommands.stream()
                .forEach(movInput -> effectiveMovementVersor.add(new Vector2d(movInput.getVector())));
        }
        return Utilities.normalizeVector(effectiveMovementVersor);
    }

    /**
     * Method for handling commands. Commands are a special type of
     * Input which tells the engine to do something, such as pausing the game.
     */
    @Override
    public void handleCommand(final CommandInput cmd) {
        switch (cmd) {
            case PAUSE -> this.isGamePaused = true;
            case RESUME -> this.isGamePaused = false;
            case QUIT -> this.isGameClosable = true;
        }
    }

    /**
     * Method for handling attack directions which are moving
     * to a specific target.
     * 
     * @return a normalized {@link Vector2dc} which represents
     *      the direction for the attack to follow.
     */
    @Override
    public Vector2dc handleAttackDirection(final Vector2dc target) {
       return Utilities.normalizeVector(target);
    }
}
