package suchagame.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import suchagame.ecs.system.GraphicSystem;
import suchagame.ecs.system.MovementSystem;

public class GameLoop {
    Timeline gameLoop;
    static final int framerate = 60;
    public GameLoop() {
        this.gameLoop =  new Timeline(new KeyFrame(Duration.millis((double) 1000 / framerate), event -> {
            MovementSystem.update();
            if (Game.lightEnabled)
                Light.shimmeringLight();
            this.render();
        }));
        this.gameLoop.setCycleCount(Timeline.INDEFINITE);
        this.gameLoop.play();
    }

    public void render() {
        Game.gc.clearRect(0, 0, Game.width, Game.height);
        Game.camera.render(Game.gc);
        GraphicSystem.render(Game.gc);
    }
}
