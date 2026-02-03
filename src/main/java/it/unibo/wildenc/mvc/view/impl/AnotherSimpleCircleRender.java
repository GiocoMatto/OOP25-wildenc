package it.unibo.wildenc.mvc.view.impl;

import java.io.File;
import java.util.Collection;
import java.util.stream.Stream;

import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import it.unibo.wildenc.mvc.view.api.SpriteRender;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class AnotherSimpleCircleRender implements SpriteRender {

    private static final int TOTAL_FRAMES = 3;
    private static final int SPRITE_SIZE = 32;

    private Canvas canvas;
    private Image playerSheet;
    private DrawInfos drawInfos;
    private int frameCount;
    private int rotationCounter;
    private double cameraX;
    private double cameraY;

    public AnotherSimpleCircleRender() {
        var spriteLoad = getClass().getResource("/sprites/testidle.png");
        if (spriteLoad == null) {
            System.err.println("Errore nell'apertura del file! :(");
        } else {
            this.playerSheet = new Image(spriteLoad.toExternalForm());
        }
    }

    @Override
    public void renderAll(Collection<MapObjViewData> objectDatas) {
        final GraphicsContext draw = canvas.getGraphicsContext2D();
        final int currentFrame = ((frameCount / 24) % TOTAL_FRAMES) * SPRITE_SIZE;
        final int currentRotation = (rotationCounter / 3) % 8;
        updateCamera(
            objectDatas.stream()
                .filter(e -> e.name().contains("player"))
                .findFirst()
                .orElse(null)
        );
        objectDatas.stream()
            .forEach(objectData -> {
                if(objectData.name().contains("player")) {
                    draw.drawImage(
                        playerSheet, 0, 40 * currentRotation, SPRITE_SIZE, SPRITE_SIZE,
                        objectData.x() - this.cameraX - (SPRITE_SIZE), 
                        objectData.y() - this.cameraY - (SPRITE_SIZE), 
                        64, 64
                    );
                } else {
                    draw.setFill(getColor(objectData));
                    draw.fillOval(objectData.x() - this.cameraX, objectData.y() - this.cameraY, 10, 10);
                }
        });

        frameCount++;
        rotationCounter++;
    }

    @Override
    public void setCanvas(Canvas c) {
        canvas = c;
    }

    @Override
    public void clean() {
        final var draw = canvas.getGraphicsContext2D();
        draw.clearRect(0, 0, canvas.widthProperty().get(), canvas.heightProperty().get());
    }

    public void setRenderInfos(DrawInfos draw) {
        drawInfos = draw;
    }

    private Color getColor(MapObjViewData objd) {
        return Stream.of(DrawInfos.values())
            .filter(i -> {
                return objd.name().split(":")[0].equals(i.name().toLowerCase());
            })
            .map(i -> i.getColor())
            .findFirst()
            .orElse(Color.BLACK);
    }
     
    public enum DrawInfos {
        PLAYER("player", Color.GREEN),
        ENEMY("enemy", Color.RED),
        PROJECTILE("projectile", Color.GRAY);

        private final Color color;
        
        DrawInfos(String id, Color c) {
            color = c;
        }

        public Color getColor() {
            return color;
        }

    }

    @Override
    public void updateCamera(MapObjViewData playerObj) {
        this.cameraX = playerObj.x() - canvas.widthProperty().get() / 2;
        this.cameraY = playerObj.y() - canvas.heightProperty().get() / 2;
    }
}
