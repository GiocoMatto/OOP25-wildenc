package it.unibo.wildenc.mvc.model;

import org.joml.Vector2d;

public interface Player {

    void levelUp();
    void getExp();
    void setDirection(Vector2d dir);
}
