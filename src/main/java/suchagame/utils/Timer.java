package suchagame.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Timer {

    private final Timeline timeline;
    public Timer(long duration, Runnable callback) {
        timeline = new Timeline(new KeyFrame(Duration.millis(duration), event -> {
            if (callback != null) {
                callback.run();
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void stop() {
        timeline.stop();
    }
}
