package suchagame.ecs.entity;

import suchagame.ecs.component.*;

public class Mob extends Entity {
    public Mob() {
        super();
        this.addComponent(new TransformComponent(300, 200));
        this.addComponent(new GraphicComponent("images/slime_idle.png"));
        this.addComponent(new AnimationComponent(this.getComponent(GraphicComponent.class),
                        4, 1, (int) (Math.random() * 4), 10));
        this.addComponent(new MovementComponent(5));
        this.addComponent(new PhysicComponent(
                32, 32, 0, 0,
                20, 0.5f, 0.5f, 0.5f, 0.5f));
    }
}
