package suchagame.ecs.entity;

import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.input.KeyCode;
import suchagame.ecs.component.*;
import suchagame.ecs.component.AnimationComponent.ACTION;
import suchagame.ui.Game;

import java.util.HashMap;
import java.util.Map;

public class Player extends Entity {
    public Player() {
        super();
        this.addComponent(new InputComponent(
                new KeyCode[]{KeyCode.Z, KeyCode.Q, KeyCode.S, KeyCode.D},
                new KeyCode[]{
                        KeyCode.AMPERSAND, KeyCode.DEAD_ACUTE, KeyCode.QUOTEDBL,
                        KeyCode.B, KeyCode.E, KeyCode.A
                }
        ));
    }
}
