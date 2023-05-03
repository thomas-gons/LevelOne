package suchagame.ecs.entity;

import javafx.scene.input.KeyCode;
import suchagame.ecs.component.*;
import suchagame.utils.Vector2;

public class Player extends Entity {
    public static Player player = new Player();
    public Player() {
        super();
        this.addComponent(new TransformComponent(200, 200));
        this.addComponent(new InputComponent(new KeyCode[]{KeyCode.Z, KeyCode.Q, KeyCode.S, KeyCode.D}));
        this.addComponent(new GraphicComponent("images/flame.png"));
        this.addComponent(new AnimationComponent(this.getComponent(GraphicComponent.class),
                8, 1, 0, 14));
        this.addComponent(new MovementComponent(3));
        this.addComponent(new PhysicComponent(
                25, 40, -12, -15,
                20, 0.5f, 0.5f, 0.5f, 0.5f));
    }
}
