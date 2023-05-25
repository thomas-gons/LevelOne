package suchagame.ecs.component;

import suchagame.ecs.entity.Item;
import suchagame.ui.Game;

import java.util.HashMap;
import java.util.Map;

/**
 * Component for managing inventory-related data and actions.
 */
public class InventoryComponent extends Component {
    // map of items to the amount of that item in the inventory
    private final Map<Item, Integer> inventory;

    /**
     * Constructs an InventoryComponent object with the specified inventory.
     * @param inventory the initial inventory to set for the component
     */
    public InventoryComponent(Map<String, Integer> inventory) {
        super();
        this.inventory = new HashMap<>();
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            Item item = Game.em.getItem(entry.getKey());
            this.inventory.put(item, entry.getValue());
        }
    }

    public Map<Item, Integer> getInventory() {
        return this.inventory;
    }

    // returns the amount of the specified item in the inventory
    public int getItemAmount(String tag) {
        Item item = Game.em.getItem(tag);
        return this.inventory.getOrDefault(item, 0);
    }

    // returns the slime drop amount (i.e. money) in the inventory
    public int getSlimeDropAmount() {
        return getItemAmount("slime_drop");
    }
}
