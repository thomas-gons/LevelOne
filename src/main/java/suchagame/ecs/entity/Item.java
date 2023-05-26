package suchagame.ecs.entity;

import suchagame.ecs.component.StatsComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for all items.
 */
public class Item extends Entity {
    private final String tag;
    private final ItemType type;
    private final int slimeDropValue;
    private final long duration;

    /**
     * Constructs an Item object with the specified tag, type, slime drop value, stats, and duration.
     * @param tag the tag of the item
     * @param type the type of the item
     * @param slimeDropValue the amount of slime that the item drops when destroyed
     * @param duration the duration of the item
     */
    public Item(String tag, ItemType type, int slimeDropValue, long duration) {
        super();
        this.tag = tag;
        this.type = type;
        this.slimeDropValue = slimeDropValue;
        this.duration = duration;
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

    /**
     * Enum for item types.
     */
    public enum ItemType {
        ARTEFACT,   // permanent stats boost items
        CONSUMABLE, // temporary stats boost items or simple consumables
        SPELL,      // items that can be cast
        MISC        // miscellaneous items
    }

    public long getDuration() {
        return this.duration;
    }
}
