package it.unibo.wildenc.samples;

import java.util.Optional;

public interface Weapon {

    Projectile attack(Point2D from, Optional<Vector2D> dir);

    void upgrade();

}
