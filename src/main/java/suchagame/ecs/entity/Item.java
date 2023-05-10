package suchagame.ecs.entity;

import suchagame.ecs.component.StatsComponent;

import java.util.HashMap;
import java.util.Map;

public class Item extends Entity {
    private final String tag;
    private final int slimeDropValue;
    @SafeVarargs
    public Item(String tag, int slimeDropValue, Map.Entry<String, Float>... stats) {
        super();
        this.tag = tag;
        this.slimeDropValue = slimeDropValue;
        addComponent(new StatsComponent(
                new HashMap<>(
                        Map.ofEntries(stats)
                ),
                null
        ));
    }

    public String getTag() {
        return this.tag;
    }

    public int getSlimeDropValue() {
        return this.slimeDropValue;
    }
}
