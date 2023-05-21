package suchagame.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * A simple timer class that runs a callback after a specified duration.
 */
public class Timer {
    private final Timeline timeline;

    /**
     * Creates a new timer.
     * @param duration The duration of the timer in milliseconds.
     * @param callback The callback to run after the timer is finished.
     */
    public Timer(long duration, Runnable callback) {
        timeline = new Timeline(new KeyFrame(Duration.millis(duration), event -> {
            if (callback != null) {
                callback.run();
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * Stops the timer.
     */
    public void stop() {
        timeline.stop();
    }
}
