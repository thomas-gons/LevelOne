package suchagame.ecs.entity;

import suchagame.ecs.component.*;
import suchagame.ecs.component.AnimationComponent.ACTION;
import suchagame.utils.Vector2;

import java.util.HashMap;
import java.util.Map;

public class Projectile extends Entity {
    public Projectile() {
        super();
        Vector2<Float> position = Player.player.getComponent(TransformComponent.class).getPosition();
        addComponent(new TransformComponent(position.getX(), position.getY()));
        addComponent(new GraphicComponent("fireball.png"));
        addComponent(new AnimationComponent(
                getComponent(GraphicComponent.class),
                12, ACTION.IDLE, 0,
                new int[]{5, 7}));

        addComponent(new PhysicComponent(
                25, 35, -12, -10,
                20, 0.5f, 0.5f, 0.5f, 0.5f
        ));
        addComponent(new StatsComponent(new HashMap<>(Map.of(
                "spd", 5f
            ))
        ));
    }
}
