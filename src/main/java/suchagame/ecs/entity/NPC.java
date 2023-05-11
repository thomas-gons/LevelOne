package suchagame.ecs.entity;

import javafx.beans.property.SimpleFloatProperty;
import suchagame.ecs.component.*;

import java.util.HashMap;
import java.util.Map;

public class NPC extends Entity {
    public NPC() {
        super();
        addComponent(new TransformComponent(700, 700));
        addComponent(new GraphicComponent("blacksmith.png"));
        addComponent(new AnimationComponent(
                getComponent(GraphicComponent.class),
                10, AnimationComponent.ACTION.IDLE, 0,
                new int[]{6}
        ));
        addComponent(new PhysicComponent(
                25, 35, -12, -8,
                100));
           this.addComponent(new StatsComponent(
                new HashMap<>(Map.of(
                    "hp_max", 10000000f,
                    "mp_max", 100f,
                    "atk", 5f,
                    "def", 10f,
                    "spd", 2f
                )), new HashMap<>(Map.of(
                    "hp", new SimpleFloatProperty(1000000f),
                    "mp", new SimpleFloatProperty(100f)
                ))
        ));
    }
}
