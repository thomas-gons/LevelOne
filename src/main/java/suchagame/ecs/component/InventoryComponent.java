package suchagame.ecs.component;

import com.fasterxml.jackson.annotation.JsonCreator;
import suchagame.ecs.entity.Item;
import suchagame.ui.Game;

import java.util.HashMap;
import java.util.Map;

public class InventoryComponent extends Component {
    private Map<Item, Integer> inventory;

    @JsonCreator
    public InventoryComponent(Map<String, Integer> inventory) {
        super();
        this.inventory = new HashMap<>();
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            this.inventory.put(Game.em.getItem(entry.getKey()), entry.getValue());
        }
    }

    public Map<Item, Integer> getInventory() {
        return this.inventory;
    }

    public int getItemAmount(String tag) {
        Item item = Game.em.getItem(tag);
        return this.inventory.getOrDefault(item, 0);
    }
}
