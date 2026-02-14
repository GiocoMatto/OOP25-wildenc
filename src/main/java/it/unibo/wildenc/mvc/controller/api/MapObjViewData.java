package it.unibo.wildenc.mvc.controller.api;

import java.util.Optional;

import it.unibo.wildenc.mvc.model.MapObject;

/**
 * Utility record for translating a {@link MapObject}
 * to something which is easily usable by the views.
 * 
 * @param name the name of the {@link MapObject}
 * @param x the x position
 * @param y the y position
 * @param hbRad the hitbox radius
 * @param directionX its x-direction in form of a {@link Optional}
 * @param directionY its y-direction in form of a {@link Optional}
 */
public record MapObjViewData(
    String name, 
    double x, 
    double y, 
    double hbRad,
    Optional<Double> directionX, 
    Optional<Double> directionY
) { }
