package suchagame.ecs.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for the player.
 */
public class Player extends Entity {

    /** only one item type can be in hand at a time.
     * @see Item.ItemType
    */
    private final Map<Item.ItemType, Item> handItems = new HashMap<>();
    private static String deathCause;

    public Player(Map<Item.ItemType, String> handItems) {
        super();
        initHandItems();
        for (Map.Entry<Item.ItemType, String> entry : handItems.entrySet()) {
            Item item = new Item(entry.getValue(), entry.getKey(), 0);
            this.handItems.put(entry.getKey(), item);
        }
    }

    /**
     * Initializes the hand items map with null values for each item type
     * because some item types may not be present in the map.
     */
    private void initHandItems() {
        for (Item.ItemType type : Item.ItemType.values()) {
            this.handItems.put(type, null);
        }
    }

     public Map<Item.ItemType, Item> getHandItems() {
        return handItems;
    }

    public Item getHandItem(Item.ItemType type) {
        return handItems.get(type);
    }

    public void setHandItem(Item item) {
        handItems.put(item.getType(), item);
    }

    public static String getDeathCause() {
        return deathCause;
    }

    public static void setDeathCause(String deathCause) {
        Player.deathCause = deathCause;
    }
}
