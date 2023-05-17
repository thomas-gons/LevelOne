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
        BoundingBox spawnArea = spawnAreas.get("all"); // (Math.random() > 0.5f) ? "northeast" : "southeast");
        this.spawnOrigin = new Vector2f((float) spawnArea.getCenterX(), (float) spawnArea.getCenterY());
    }

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

    public Vector2f getSpawnOrigin() {
        return spawnOrigin;
    }
}
