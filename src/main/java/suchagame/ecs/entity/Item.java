package suchagame.ecs.entity;

import suchagame.ecs.component.StatsComponent;

import java.util.HashMap;
import java.util.Map;

public class Item extends Entity {
    private final String tag;
    private final ItemType type;
    private final int slimeDropValue;

    public Item(String tag, String type, int slimeDropValue) {
        super();
        this.tag = tag;
        this.type = ItemType.valueOf(type.toUpperCase());
        this.slimeDropValue = slimeDropValue;
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
