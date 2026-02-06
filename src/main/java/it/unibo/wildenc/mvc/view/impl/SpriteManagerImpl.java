package it.unibo.wildenc.mvc.view.impl;

import java.io.FileInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import it.unibo.wildenc.mvc.view.api.SpriteManager;
import javafx.scene.image.Image;

public class SpriteManagerImpl implements SpriteManager {

    private static final int SPRITE_SIZE = 64;
    private static final int TOTAL_FRAMES = 3;
    private static final int DOT_PNG_PREFIX_LENGHT = 4;
    private static final String SPRITES_LOCATION = "sprites";

    private static final List<Integer> SPRITE_MAP = List.of(2, 1, 0, 7, 6, 5, 4, 3);

    private Map<String, Image> loadedSpriteMap = new LinkedHashMap<>();
    private double playerPosX; // Needed for calculating versor when idle.
    private double playerPosY;

    public SpriteManagerImpl() {
        loadAllResources();
    }

    @Override
    public Sprite getSprite(int frameCount, MapObjViewData objData) {
        if (objData.name().contains("Proj")) {
            return new Sprite(
                this.loadedSpriteMap.get(objData.name().toLowerCase()),
                0, 0
            );
        }
        return new Sprite(
            loadedSpriteMap.get(objData.name().toLowerCase().split(":")[0]),
            convertVersorToDominant(objData),
            ((frameCount / 24) % TOTAL_FRAMES) * SPRITE_SIZE
        );
    }

    @Override
    public Image getGrassTile() {
        return this.loadedSpriteMap.get("grasstile");
    }   

    private int convertVersorToDominant(MapObjViewData data) {
        double dx = data.directionX().get();
        double dy = data.directionY().get();

        if (data.name().contains("player")) {
            this.playerPosX = data.x();
            this.playerPosY = data.y();
        }
        
        if (Math.abs(dx) < 0.01 && Math.abs(dy) < 0.01) {
            dx = playerPosX - data.x();
            dy = playerPosY - data.y();
        }
        double effectiveAngle = Math.toDegrees(Math.atan2(dy, dx));
        if (effectiveAngle < 0) {
            effectiveAngle += 360;
        };
        int slice = (int) Math.floor((effectiveAngle + 22.5) / 45) % 8;
        return SPRITE_MAP.get(slice);
    }

    public void loadAllResources() {
        URL resourceFolder = getClass().getClassLoader().getResource(SPRITES_LOCATION);

        if (resourceFolder.getProtocol().equals("file")) {
            try (Stream<Path> paths = Files.list(Paths.get(resourceFolder.toURI()))) {
                paths.filter(p -> p.toString().endsWith(".png"))
                    .forEach(p -> {
                        String key = p.getFileName().toString().replace(".png", "");
                        loadedSpriteMap.put(key, new Image(p.toUri().toString()));
                    });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resourceFolder.getProtocol().equals("jar")) {
            try {
                String jarPath = resourceFolder.getPath().substring(5, resourceFolder.getPath().indexOf("!"));
                try (ZipInputStream zip = new ZipInputStream(new FileInputStream(jarPath))) {
                    ZipEntry entry;
                    while ((entry = zip.getNextEntry()) != null) {
                        String name = entry.getName();
                        if (name.startsWith(SPRITES_LOCATION + "/") && name.endsWith(".png")) {
                            String key = name.substring(SPRITES_LOCATION.length() + 1, name.length() - DOT_PNG_PREFIX_LENGHT);
                            Image img = new Image(getClass().getResourceAsStream("/" + name));
                            loadedSpriteMap.put(key, img);
                        }
                    }
                }
            } catch (Exception e) { 
                e.printStackTrace(); 
            }
        }
    }
}

