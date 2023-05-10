package suchagame.ecs.entity;

import javafx.beans.property.SimpleFloatProperty;
import javafx.geometry.BoundingBox;
import suchagame.ecs.component.*;
import suchagame.ecs.component.AnimationComponent.ACTION;
import suchagame.ui.Game;
import suchagame.utils.Vector2f;

import java.util.HashMap;
import java.util.Map;

public class Mob extends Entity {
    private static final Map<String, BoundingBox> spawnAreas = initSpawnAreas();
    private final Vector2f spawnOrigin;
    public Mob() {
        super();
        BoundingBox spawnArea = spawnAreas.get((Math.random() > 0.5f) ? "northeast" : "southeast");
        this.spawnOrigin = new Vector2f((float) spawnArea.getCenterX(), (float) spawnArea.getCenterY());
        this.addComponent(new TransformComponent(spawnArea));

        this.addComponent(new GraphicComponent("slime.png"));

        this.addComponent(new AnimationComponent(
                this.getComponent(GraphicComponent.class),14,
                ACTION.IDLE, -1,
                new int[]{4, 18, 11}
        ));

        this.addComponent(new PhysicComponent(
                22, 13, -10, 7,
                1));

        this.addComponent(new StatsComponent(new HashMap<>(Map.of(
                "hp_max", 100f,
                "mp_max", 100f,
                "atk", 5f,
                "def", 10f,
                "spd", 1f
            )), new HashMap<>(Map.of(
                "hp", new SimpleFloatProperty(100),
                "mp", new SimpleFloatProperty(100)
            ))
        ));

        this.addComponent(new InventoryComponent(new HashMap<>(Map.of(
                Game.em.getItem("slimeDrop"), (int) (Math.random() * 25)
            ))
        ));
    }

    private static Map<String, BoundingBox> initSpawnAreas() {
        return new HashMap<>(Map.of(
                "northeast",
                new BoundingBox(1150, 25,400, 250),
                "southeast",
                new BoundingBox(1500, 750,400, 250)
        ));
    }

    public Vector2f getSpawnOrigin() {
        return spawnOrigin;
    }
}
