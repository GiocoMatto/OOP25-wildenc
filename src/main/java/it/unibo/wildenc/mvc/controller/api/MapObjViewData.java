package it.unibo.wildenc.mvc.controller.api;

import java.util.Optional;

public record MapObjViewData(
    String name, 
    double x, 
    double y, 
    double hbRad,
    Optional<Double> directionX, 
    Optional<Double> directionY
) { }
