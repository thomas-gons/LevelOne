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
 * The Debug class provides a debugging view for the game.
 */
public class Debug {
    private final AnchorPane debugView;
    private final Timeline debugLoop;

    private HashMap<String, Label> debugLabels;
    private boolean debugViewVisible = false;

    /**
     * Initializes a new instance of the Debug class.
     *
     * @param debugView The AnchorPane representing the debug view.
     */
    public Debug(AnchorPane debugView) {
        this.debugView = debugView;
        this.debugLabels = new HashMap<>();
        initDebugView();
        this.debugLoop = new Timeline(new KeyFrame(Duration.millis(GameLoop.frameDuration * GameLoop.sampleFrameSize), event -> {
            updateDebugLabels();
        }));

        this.debugLoop.setCycleCount(Timeline.INDEFINITE);

        toggleDebugView();
    }

    /**
     * Initializes the debug view by creating and positioning the debug labels.
     */
    private void initDebugView() {
        String[] debugLabels = {"fps", "entities", "scale", "position", "health", "mana"};
        for (int i = 0; i < debugLabels.length; i++) {
            Label label = initLabel();
            label.setLayoutX(10);
            label.setLayoutY(10 + 40 * i);
            this.debugLabels.put(debugLabels[i], label);
        }

        this.debugView.getChildren().addAll(this.debugLabels.values());
    }

    /**
     * Initializes a new debug label with the default settings.
     *
     * @return The initialized Label object.
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
     * Updates the debug labels with the current game statistics.
     */
    private void updateDebugLabels() {
        this.debugLabels.get("fps").setText(String.format("FPS: %.2f", GameLoop.fps));
        this.debugLabels.get("scale").setText("Scale: " + Camera.scale);
        this.debugLabels.get("entities").setText("Entity Count: " + Game.em.getEntityCount());
        this.debugLabels.get("position").setText("Player Position: " + Game.em.getPlayer().getComponent(TransformComponent.class).getPosition());
        this.debugLabels.get("health").setText("Player Health: " + Game.em.getPlayer().getComponent(StatsComponent.class).getObservableStat("hp"));
        this.debugLabels.get("mana").setText("Player Mana: " + Game.em.getPlayer().getComponent(StatsComponent.class).getObservableStat("mp"));
    }
}
