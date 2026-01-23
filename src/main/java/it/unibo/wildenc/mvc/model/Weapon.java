package it.unibo.wildenc.mvc.model;

import java.util.Optional;

import org.joml.Vector2f;

public interface Weapon {

    Projectile attack(Point2D from, Optional<Vector2f> dir);

    void upgrade();

}
