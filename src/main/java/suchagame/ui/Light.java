package suchagame.ui;

import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import suchagame.ecs.EntityManager;
import suchagame.ecs.component.TransformComponent;
import suchagame.ecs.entity.Player;
import suchagame.utils.Vector2;

public class Light {
    private static final int shimmeringRadius = 7;
    private static final int shimmeringDelta = 80;
    private static final Stop[] keyStops = new Stop[] {
            new Stop(0.75, Color.rgb(255, 235, 133, 0.12)),
            new Stop(1.0, Color.BLACK)
    };
    public static int defaultRadius = 350;
    private static int radius = defaultRadius;
    private static long lastTimeShimmering = System.currentTimeMillis();
    public static void shimmeringLight() {
        if (System.currentTimeMillis() -  lastTimeShimmering > shimmeringDelta) {
            radius = defaultRadius + (int) (Math.random() * shimmeringRadius - shimmeringRadius / 2);
            lastTimeShimmering = System.currentTimeMillis();
        }
        Vector2<Float> virtualPosition = Player.player.getComponent(TransformComponent.class).getVirtualPosition();
        RadialGradient gradient = new RadialGradient(
                0,
                0,
                virtualPosition.getX() + 110,
                virtualPosition.getY() + 200,
                radius,
                false,
                CycleMethod.NO_CYCLE,
                keyStops
        );
        Blend blend = new Blend(
                BlendMode.SRC_ATOP,
                null,
                new ColorInput(
                        0, 0,
                        Game.width, Game.height,
                        gradient
                )
        );
        Game.root.setEffect(blend);
    }
}
