/*
package it.unibo.wildenc.mvc.view;

import java.util.stream.Stream;

import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import it.unibo.wildenc.mvc.view.api.SpriteRender;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class SimpleCircleRender implements SpriteRender {

    private DrawInfos drawInfos;
    private Canvas canvas;

    public SimpleCircleRender() {

    }

    @Override
    public void render(MapObjViewData objectData) {
        final var draw = canvas.getGraphicsContext2D();
cc
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
}
*/
