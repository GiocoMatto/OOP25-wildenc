package it.unibo.wildenc.mvc.controller.api;

import java.util.Set;

import org.joml.Vector2d;
import org.joml.Vector2dc;

public interface InputHandler {
    public enum MovementInput {
        GO_UP(new Vector2d(0, 1)),
        GO_RIGHT(new Vector2d(1, 0)),
        GO_DOWN(new Vector2d(0, -1)),
        GO_LEFT(new Vector2d(-1, 0));

        private final Vector2dc vect;
        
        private MovementInput(final Vector2dc v) {
            this.vect = v;
        }
 
        public Vector2dc getVector() {
            return this.vect;
        }
    }

    public enum CommandInput {
        PAUSE,
        RESUME,
        QUIT;
    }

    boolean isPaused();

    Vector2dc handleMovement(Set<MovementInput> d);

    void handleCommand(CommandInput d);

    Vector2dc handleAttackDirection(Vector2dc target); // FIXME: non è un vettore: capire cosa manda la view

}
