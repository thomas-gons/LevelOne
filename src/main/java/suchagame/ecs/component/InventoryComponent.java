package suchagame.ecs.component;

import suchagame.ecs.entity.Item;
import suchagame.ui.Game;

import java.util.Map;

public class InventoryComponent extends Component {
    private Map<Item, Integer> inventory;

    public InventoryComponent(Map<Item, Integer> inventory) {
        super();
        this.inventory = inventory;
    }

    public Map<Item, Integer> getInventory() {
        return this.inventory;
    }

    public int getItemAmount(String tag) {
        Item item = Game.em.getItem(tag);
        return this.inventory.getOrDefault(item, 0);
    }
}
