package it.unibo.wildenc.mvc.model.weaponary;

import org.joml.Vector2d;

public record AttackMovementInfo(Vector2d atkDirection, double deltaTime, double atkVelocity) {}
