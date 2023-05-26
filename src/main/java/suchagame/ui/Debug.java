package suchagame.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import suchagame.ecs.component.StatsComponent;
import suchagame.ecs.component.TransformComponent;

import java.util.HashMap;

/**
 * Represents the debug overlay used to display game information during debugging.
 */
public class Debug {
    private final AnchorPane debugView;
    private final Timeline debugLoop;

    private final HashMap<String, Label> debugLabels;
    private boolean debugViewVisible = false;

    /**
     * Initializes the Debug overlay with the provided AnchorPane for displaying debug information.
     *
     * @param debugView The AnchorPane for displaying debug information.
     */
    public Debug(AnchorPane debugView) {
        this.debugView = debugView;
        this.debugLabels = new HashMap<>();
        initDebugView();
        this.debugLoop = new Timeline(new KeyFrame(Duration.millis(GameLoop.frameDuration * GameLoop.sampleFrameSize), event -> updateDebugLabels()));

        this.debugLoop.setCycleCount(Timeline.INDEFINITE);

        toggleDebugView();
    }

    /**
     * Initializes the debug view by creating and positioning the debug labels.
     */
    private void initDebugView() {
        String[] debugLabels = {"fps", "entities", "scale", "game mode", "position", "health", "mana"};
        for (int i = 0; i < debugLabels.length; i++) {
            Label label = initLabel();
            label.setLayoutX(10);
            label.setLayoutY(10 + 40 * i);
            this.debugLabels.put(debugLabels[i], label);
        }

        this.debugView.getChildren().addAll(this.debugLabels.values());
    }

    /**
     * Initializes a debug label with common properties.
     *
     * @return The initialized Label.
     */
    private Label initLabel() {
        Label label = new Label();
        label.setTextFill(Color.LIGHTGRAY);
        label.setFont(Game.hud.getCustomFont());
        return label;
    }

    /**
     * Toggles the visibility of the debug view when the F3 key is pressed.
     */
    private void toggleDebugView() {
        debugView.toBack();
        Game.scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F3) {
                this.debugViewVisible = !this.debugViewVisible;
                if (this.debugViewVisible) {
                    this.debugLoop.play();
                    debugView.toFront();
                    this.debugView.visibleProperty().setValue(true);
                } else {
                    this.debugLoop.pause();
                    debugView.toBack();
                    this.debugView.visibleProperty().setValue(false);
                }
            }
        });
    }

    /**
     * Updates the debug labels with the latest game information.
     */
    private void updateDebugLabels() {
        this.debugLabels.get("fps").setText(String.format("FPS: %.2f", GameLoop.fps));
        this.debugLabels.get("scale").setText("Scale: " + Camera.scale);
        this.debugLabels.get("entities").setText("Entity Count: " + Game.em.getEntityCount());
        this.debugLabels.get("position").setText("Player Position: " + Game.em.getPlayer().getComponent(TransformComponent.class).getPosition());
        this.debugLabels.get("health").setText("Player Health: " + Game.em.getPlayer().getComponent(StatsComponent.class).getObservableStat("hp"));
        this.debugLabels.get("mana").setText("Player Mana: " + Game.em.getPlayer().getComponent(StatsComponent.class).getObservableStat("mp"));
        this.debugLabels.get("game mode").setText("Game Mode: " + Game.gameMode.toString().toLowerCase());
    }

    /**
     * Stops the debug loop.
     */
    public void stop() {
        this.debugLoop.stop();
    }
}
