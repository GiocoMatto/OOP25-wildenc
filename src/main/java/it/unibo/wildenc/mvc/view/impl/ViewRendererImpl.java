package it.unibo.wildenc.mvc.view.impl;

import java.util.Collection;

import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import it.unibo.wildenc.mvc.view.api.SpriteManager;
import it.unibo.wildenc.mvc.view.api.SpriteManager.Sprite;
import it.unibo.wildenc.mvc.view.api.ViewRenderer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;



public class ViewRendererImpl implements ViewRenderer {

    private static final int SPRITE_SIZE = 64;
    private static final int SPRITE_PADDING = 2;
    private static final int INITIAL_CANVAS_WIDTH = 1600;

    private final SpriteManager spriteManager;
    private Canvas canvas;
    private int frameCount;
    private double cameraX;
    private double cameraY;

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

        drawGrassTiles(draw, scale);

        objectDatas.stream()
            .forEach(objectData -> {
                final Sprite currentSprite = spriteManager.getSprite(frameCount, objectData);
                final double radius = objectData.hbRad();
                final double renderSize = radius * 2 * SPRITE_PADDING;

                draw.drawImage(
                    currentSprite.spriteImage(), 
                    currentSprite.currentFrame(), 
                    SPRITE_SIZE * currentSprite.rotationFrame(), 
                    SPRITE_SIZE, SPRITE_SIZE,
                    objectData.x() - this.cameraX - (renderSize / 2), 
                    objectData.y() - this.cameraY - (renderSize / 2), 
                    renderSize, 
                    renderSize
                );
                /*
                draw.setStroke(javafx.scene.paint.Color.LIME);
                draw.setLineWidth(1);
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

    private void drawGrassTiles(GraphicsContext draw, double scale) {
        Image grassTile = spriteManager.getGrassTile();

        if (grassTile != null) {
            double startX = -this.cameraX % SPRITE_SIZE;
            double startY = -this.cameraY % SPRITE_SIZE;

            // For managing little changes.
            if (startX > 0) {
                startX -= SPRITE_SIZE;
            }
            if (startY > 0) {
                startY -= SPRITE_SIZE;
            }

            double viewportWidth = INITIAL_CANVAS_WIDTH;
            double viewportHeight = canvas.getHeight() / scale;

            for (double x = startX; x < viewportWidth + SPRITE_SIZE; x += SPRITE_SIZE) {
                for (double y = startY; y < viewportHeight + SPRITE_SIZE; y += SPRITE_SIZE) {
                    draw.drawImage(grassTile, x, y, SPRITE_SIZE, SPRITE_SIZE);
                }
            }
        }
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

    public void setStyleToContainer(Region container, String css) {
        container.getStylesheets().add(ClassLoader.getSystemResource(css).toExternalForm());
    }
}
