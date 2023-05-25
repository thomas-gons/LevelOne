package suchagame.ecs.entity;

import javafx.geometry.BoundingBox;
import java.util.HashMap;
import java.util.Map;

public class Mob extends Entity {
    private static final Map<String, BoundingBox> spawnAreas = initSpawnAreas();
     private long lastAttack = System.currentTimeMillis();
    private final String tag;
    public Mob(String tag) {
        super();
        this.tag = tag;
    }

    public static Map<String, BoundingBox> initSpawnAreas() {
        return new HashMap<>(Map.of(
                "northeast",
                new BoundingBox(1150, 25,400, 250),
                "southeast",
                new BoundingBox(1500, 750,400, 250),
                "all",
                new BoundingBox(0, 0, 1920, 1080)
        ));
    }

    /**
     * get a random spawn area from the list of spawn areas
     * @param eventualSpawnAreas list of spawn areas
     */
    public static BoundingBox getRandomSpawnArea(String[] eventualSpawnAreas) {
        return spawnAreas.get(eventualSpawnAreas[(int) (Math.random() * eventualSpawnAreas.length)]);
    }

    public long getLastAttack() {
        return lastAttack;
    }

    public void setLastAttack(long lastAttack) {
        this.lastAttack = lastAttack;
    }
}