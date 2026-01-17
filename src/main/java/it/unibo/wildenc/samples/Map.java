package it.unibo.wildenc.samples;

import java.util.List;

public interface Map {

    void addObject(MapObject mObj);

    boolean removeObject(MapObject mObj);

    List<MapObject> getAllObjects();

    void updateEntities();

}
