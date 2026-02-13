package it.unibo.wildenc.mvc.controller.api;

import java.util.Set;
import org.joml.Vector2d;
import org.joml.Vector2dc;

/**
 * Responsible of handling the input from the View and translating in such way that Model understands.
 */
public interface InputHandler {
    /**
     * Whether the game is paused.
     * 
     * @return true if the game is paused, false otherwise.
     */
    boolean isPaused();

    /**
     * Whether the game is ready to be closed.
     * 
     * @return true if the game is ready to be closed, false otherwise.
     */
    boolean isClosable();

    /**
     * Translates {@link MovementInput} commands into a {@link Vector2dc} that the Model can understand.
     * 
     * @param movementCommands A {@link Set} containing all the commands;
     * @return A Vector which sums all Movement commands.
     */
    Vector2dc handleMovement(Set<MovementInput> movementCommands);

    /**
     * Handles a command.
     * 
     * @param d the command to handle.
     */
    void handleCommand(CommandInput d);

    /**
     * Translates the pointer position sent by the View in such way Model can understand.
     * 
     * @param target pointer position
     * @return pointer position translated as a {@link Vector2dc}.
     */
    Vector2dc handleAttackDirection(Vector2dc target);

    /**
     * Interface for grouping all types of inputs.
     */
    interface Input { }

    /**
     * Movement commands the Engine knows.
     * GO_UP decreases Y and GO_DOWN increases Y.
     */
    enum MovementInput implements Input {
        GO_UP(new Vector2d(0, -1)),
        GO_RIGHT(new Vector2d(1, 0)),
        GO_DOWN(new Vector2d(0, 1)),
        GO_LEFT(new Vector2d(-1, 0));

        private final Vector2dc vect;

        MovementInput(final Vector2dc v) {
            this.vect = v;
        }

        /**
         * Method for getting the specific vector associated
         * with a direction.
         * 
         * @return the {@link Vector2d} associated with the direction.
         */
        public Vector2dc getVector() {
            return this.vect;
        }
    }

    /**
     * Commands the Engine knows.
     */
    enum CommandInput implements Input {
        PAUSE,
        RESUME,
        QUIT
    }
}
