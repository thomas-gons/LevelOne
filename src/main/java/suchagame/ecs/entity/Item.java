package suchagame.ecs.entity;

import suchagame.ecs.component.StatsComponent;

import java.util.HashMap;
import java.util.Map;

public class Item extends Entity {
    private static int nextId = 0;
    private final int id;
    @SafeVarargs
    public Item(Map.Entry<String, Float>... stats) {
        super();
        this.id = nextId++;
        addComponent(new StatsComponent(new HashMap<>(Map.ofEntries(stats))));
    }

    public int getId() {
        return id;
    }
}
