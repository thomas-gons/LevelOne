package suchagame.ecs.entity;

import javafx.geometry.Rectangle2D;
import suchagame.ecs.component.*;
import suchagame.ecs.component.AnimationComponent.ACTION;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mob extends Entity {

    private static final Map<String, Rectangle2D> spawnAreas = new HashMap<>();
    public Mob() {
        super();
        initSpawnAreas();
        this.addComponent(new TransformComponent(spawnAreas.get("northeast")));

        this.addComponent(new GraphicComponent("slime.png"));

        this.addComponent(new AnimationComponent(
                this.getComponent(GraphicComponent.class),16,
                ACTION.ATTACK, -1,
                new int[]{4, 18, 11}
        ));

        this.addComponent(new PhysicComponent(
                18, 12, -9, 10,
                20, 0.5f, 0.5f, 0.5f, 0.5f));

        this.addComponent(new StatsComponent(new HashMap<>(Map.of(
                "hp", 100f,
                "hp_max", 100f,
                "mp", 100f,
                "mp_max", 100f,
                "atk", 10f,
                "def", 10f,
                "spd", 5f
        ))));
    }

    private void initSpawnAreas() {
        spawnAreas.put(
                "northeast",
                new Rectangle2D(1150, 25,400, 250)
        );
        spawnAreas.put(
                "southeast",
                new Rectangle2D(1500, 750,400, 250)
        );
    }
}
