package suchagame.ecs.component;

import suchagame.ecs.entity.Item;

import java.util.HashMap;
import java.util.Map;

public class GameplayComponent extends Component {
    private Map<Item.ItemType, Item> handItems = new HashMap<>();
    public GameplayComponent(Map<Item.ItemType, Item> handItems) {
        super();
        initHandItems();
        this.handItems.putAll(handItems);
    }

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
