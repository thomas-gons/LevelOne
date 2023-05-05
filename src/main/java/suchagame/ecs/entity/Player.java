package suchagame.ecs.entity;

import javafx.scene.input.KeyCode;
import suchagame.ecs.component.*;
import suchagame.ecs.component.AnimationComponent.ACTION;

import java.util.HashMap;
import java.util.Map;

public class Player extends Entity {
    public static Player player = new Player();
    public Player() {
        super();
        this.addComponent(new TransformComponent(700, 550));
        this.addComponent(new InputComponent(new KeyCode[]{KeyCode.Z, KeyCode.Q, KeyCode.S, KeyCode.D}));

        this.addComponent(new GraphicComponent("flame.png"));
        this.addComponent(new AnimationComponent(
                this.getComponent(GraphicComponent.class),
                12, ACTION.IDLE, 0,
                new int[]{8}
        ));

        this.addComponent(new PhysicComponent(
                25, 35, -12, -10,
                20, 0.5f, 0.5f, 0.5f, 0.5f));

        this.addComponent(new StatsComponent(new HashMap<>(Map.of(
                "hp", 100f,
                "hp_max", 100f,
                "mp", 100f,
                "mp_max", 100f,
                "atk", 10f,
                "def", 10f,
                "spd", 3f
        ))));
    }
}
