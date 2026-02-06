package it.unibo.wildenc.mvc.controller.api;

import java.util.Optional;

public record MapObjViewData(
    String name, 
    double x, 
    double y, 
    Optional<Double> directionX, 
    Optional<Double> directionY
) { }
