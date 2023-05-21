package suchagame.ecs.component;

import suchagame.ecs.entity.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * Component for managing gameplay-related data and actions.
 */
public class GameplayComponent extends Component {

    /** only one item type can be in hand at a time.
     * @see Item.ItemType
    */
    private final Map<Item.ItemType, Item> handItems = new HashMap<>();

    /**
     * Constructs a GameplayComponent object with the specified hand items.
     *
     * @param handItems the initial hand items to set for the component
     */
    public GameplayComponent(Map<Item.ItemType, Item> handItems) {
        super();
        initHandItems();
        this.handItems.putAll(handItems);
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
}

