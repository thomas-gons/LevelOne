package suchagame.ecs.component;

import suchagame.ecs.entity.Item;

import java.util.HashMap;
import java.util.Map;

public class InventoryComponent extends Component {
    Map<Integer, Item> items = new HashMap<>();

    public void addItem(Item item) {
        items.put(item.getId(), item);
    }

    public void removeItem(Item item) {
        items.remove(item.getId());
    }
}
