package suchagame.ecs.entity;

import javafx.beans.property.SimpleFloatProperty;
import javafx.geometry.BoundingBox;
import suchagame.ecs.component.*;
import suchagame.ecs.component.AnimationComponent.ACTION;
import suchagame.ui.Game;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for all mobs.
 */
public class Mob extends Entity {

    // spawn areas for mobs
    private static final Map<String, BoundingBox> spawnAreas = initSpawnAreas();

    // cool down between attacks
    private long lastAttack = System.currentTimeMillis();

    /**
     * Constructs a Mob object with all the necessary components.
     * hard coded for now
     */
    public Mob() {
        super();
        BoundingBox spawnArea = spawnAreas.get("all"); // (Math.random() > 0.5f) ? "northeast" : "southeast");
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
                "atk", 2f,
                "def", 10f,
                "spd", 0.7f
            )), new HashMap<>(Map.of(
                "hp", new SimpleFloatProperty(100f),
                "mp", new SimpleFloatProperty(100f)
            ))
        ));

        this.addComponent(new InventoryComponent(new HashMap<>(Map.of(
                Game.em.getItem("slime_drop"), (int) (Math.random() * 25)
            ))
        ));
    }

    /**
     * Initializes the spawn areas for mobs.
     * @return a map of spawn areas
     */
    private static Map<String, BoundingBox> initSpawnAreas() {
        return new HashMap<>(Map.of(
                "northeast",
                new BoundingBox(1150, 25,400, 250),
                "southeast",
                new BoundingBox(1500, 750,400, 250),
                "all",
                new BoundingBox(0, 0, 1920, 1080)
        ));
    }

    public long getLastAttack() {
        return lastAttack;
    }

    public void setLastAttack(long lastAttack) {
        this.lastAttack = lastAttack;
    }
}
