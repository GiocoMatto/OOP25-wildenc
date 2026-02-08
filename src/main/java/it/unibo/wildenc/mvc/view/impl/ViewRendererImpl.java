package it.unibo.wildenc.mvc.view.impl;

import java.util.Collection;

import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import it.unibo.wildenc.mvc.view.api.SpriteManager;
import it.unibo.wildenc.mvc.view.api.SpriteManager.Sprite;
import it.unibo.wildenc.mvc.view.api.ViewRenderer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;



public class ViewRendererImpl implements ViewRenderer {

    private static final int SPRITE_SIZE = 64;
    private static final int INITIAL_CANVAS_WIDTH = 1600;

    private final SpriteManager spriteManager;
    private Canvas canvas;
    private int frameCount;
    private double cameraX;
    private double cameraY;

    private Region backgroundContainer;

    public ViewRendererImpl () {
        this.spriteManager = new SpriteManagerImpl();
    }

    @Override
    public void renderAll(Collection<MapObjViewData> objectDatas) {
        final GraphicsContext draw = canvas.getGraphicsContext2D();
        final double scale = canvas.getWidth() / INITIAL_CANVAS_WIDTH;

        draw.save();
        draw.scale(scale, scale);

        updateCamera(
            objectDatas.stream()
                .filter(e -> e.name().contains("player"))
                .findFirst()
                .orElse(null)
        );

        final double bgX = -this.cameraX % SPRITE_SIZE;
        final double bgY = -this.cameraY % SPRITE_SIZE;
        backgroundContainer.setStyle("-fx-background-position: " + bgX + "px " + bgY + "px;");

        objectDatas.stream()
            .forEach(objectData -> {
                Sprite currentSprite = spriteManager.getSprite(frameCount, objectData);

                draw.drawImage(
                    currentSprite.spriteImage(), 
                    currentSprite.currentFrame(), 
                    SPRITE_SIZE * currentSprite.rotationFrame(), 
                    SPRITE_SIZE, SPRITE_SIZE,
                    objectData.x() - this.cameraX - (SPRITE_SIZE / 2), 
                    objectData.y() - this.cameraY - (SPRITE_SIZE / 2), 
                    SPRITE_SIZE, SPRITE_SIZE
                );

                /* HITBOX VISUALIZATION
                draw.setStroke(javafx.scene.paint.Color.LIME);
                draw.setLineWidth(1);
                double radius = objectData.hbRad();
                draw.strokeOval(
                    objectData.x() - this.cameraX - radius, 
                    objectData.y() - this.cameraY - radius, 
                    radius * 2, 
                    radius * 2
                );
                */
        });
        draw.restore();

        frameCount++;
    }

    @Override
    public void setCanvas(Canvas c) {
        canvas = c;
        this.canvas.getGraphicsContext2D().setImageSmoothing(false);
    }

    @Override
    public void clean() {
        final var draw = canvas.getGraphicsContext2D();
        draw.clearRect(0, 0, canvas.widthProperty().get(), canvas.heightProperty().get());
    }

    @Override
    public void updateCamera(MapObjViewData playerObj) {
        double effectiveWidth = INITIAL_CANVAS_WIDTH;
        double effectiveHeight = canvas.getHeight() / (canvas.getWidth() / INITIAL_CANVAS_WIDTH);

        this.cameraX = playerObj.x() - effectiveWidth / 2;
        this.cameraY = playerObj.y() - effectiveHeight / 2;
    }

    public void setContainer(Region container) {
        this.backgroundContainer = container;
        backgroundContainer.getStylesheets().add(ClassLoader.getSystemResource("css/style.css").toExternalForm());
    }
}
