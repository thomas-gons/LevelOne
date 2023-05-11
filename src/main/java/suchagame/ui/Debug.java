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

public class Debug {
    private final AnchorPane debugView;
    private final Timeline debugLoop;

    private HashMap<String, Label> debugLabels;
    private boolean debugViewVisible = false;


    public Debug(AnchorPane debugView) {
        this.debugView = debugView;
        this.debugLabels = new HashMap<>();
        initDebugView();
        this.debugLoop = new Timeline(new KeyFrame(Duration.millis(GameLoop.frameDuration * GameLoop.sampleFrameSize), event -> {
            this.debugLabels.get("fps").setText(String.format("FPS: %.2f", GameLoop.fps));
            this.debugLabels.get("scale").setText("Scale: " + Camera.scale);
            this.debugLabels.get("entities").setText("Entity Count: " + Game.em.getEntityCount());
            this.debugLabels.get("position").setText("Player Position: " + Game.em.getPlayer().getComponent(TransformComponent.class).getPosition());
            this.debugLabels.get("health").setText("Player Health: " + Game.em.getPlayer().getComponent(StatsComponent.class).getObservableStat("hp"));
            this.debugLabels.get("mana").setText("Player Mana: " + Game.em.getPlayer().getComponent(StatsComponent.class).getObservableStat("mp"));
        }));

        this.debugLoop.setCycleCount(Timeline.INDEFINITE);

        toggleDebugView();
    }

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

    private Label initLabel() {
        Label label = new Label();
        label.setTextFill(Color.LIGHTGRAY);
        label.setFont(Game.hud.getCustomFont());
        return label;
    }

    private void toggleDebugView() {
        Game.scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F3) {
                this.debugViewVisible = !this.debugViewVisible;
                if (this.debugViewVisible) {
                    this.debugLoop.play();
                    this.debugView.visibleProperty().setValue(true);
                } else {
                    this.debugLoop.pause();
                    this.debugView.visibleProperty().setValue(false);
                }
            }
        });
    }
}
