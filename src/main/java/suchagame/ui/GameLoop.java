package suchagame.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicLong;

public class GameLoop {
    Timeline gameLoop;
    static final int framerate = 60;
    static final long frameDuration = 1000 / framerate;
    static final int sampleFrameSize = 16;
    static final int samplePeriod = 1000 * sampleFrameSize;

    static float fps;
    private int frameCount = 0;

    public GameLoop() {
        AtomicLong currentTime = new AtomicLong();
        AtomicLong lastTime = new AtomicLong();
        lastTime.set(System.currentTimeMillis());
        this.gameLoop =  new Timeline(new KeyFrame(Duration.millis(frameDuration), event -> {
            if (Game.lightEnabled)
                Light.shimmeringLight();

            Game.gc.clearRect(0, 0, Game.width, Game.height);
            Game.camera.render(Game.gc);
            Game.sm.update();
            frameCount++;

            if (frameCount % sampleFrameSize == 0) {
                currentTime.set(System.currentTimeMillis());
                long timeElapsed = currentTime.get() - lastTime.get();
                lastTime.set(currentTime.get());
                GameLoop.fps = (float) samplePeriod / timeElapsed;
            }
        }));
        this.gameLoop.setCycleCount(Timeline.INDEFINITE);
        this.gameLoop.play();
    }
}
