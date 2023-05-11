package suchagame.ecs.entity;

import suchagame.ecs.component.StatsComponent;

import java.util.HashMap;
import java.util.Map;

public class Item extends Entity {
    private final String tag;
    private final ItemType type;
    private final int slimeDropValue;

    @SafeVarargs
    public Item(String tag, ItemType type, int slimeDropValue, Map.Entry<String, Float>... stats) {
        super();
        this.tag = tag;
        this.type = type;
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

    public ItemType getType() {
        return this.type;
    }

    public int getSlimeDropValue() {
        return this.slimeDropValue;
    }

    public enum ItemType {
        ARTEFACT,
        CONSUMABLE,
        SPELL,
        MISC
    }
}
