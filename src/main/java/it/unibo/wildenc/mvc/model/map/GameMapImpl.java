package it.unibo.wildenc.mvc.model.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.unibo.wildenc.mvc.model.GameMap;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.Movable;

/**
 * Basic {@link Map} implementation
 * 
 * 
 */
public class GameMapImpl implements GameMap {

    private static final double NANO_TO_SECOND_FACTOR = 1_000_000_000.0;

    private final List<MapObject> mapObjects = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void addObject(final MapObject mObj) {
        mapObjects.add(mObj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeObject(final MapObject mObj) {
        return mapObjects.remove(mObj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MapObject> getAllObjects() {
        return Collections.unmodifiableList(mapObjects);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEntities(final long deltaTime) {
        final double deltaSeconds = deltaTime / NANO_TO_SECOND_FACTOR;

        mapObjects.stream()
            .filter(e -> e instanceof Movable)
            .map(o -> (Movable)o)
            .forEach(o -> o.updatePosition(deltaSeconds));
    }
}
