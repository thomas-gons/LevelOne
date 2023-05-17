package suchagame.ecs.component;

import suchagame.ecs.entity.Item;
import suchagame.ui.Game;

import java.util.HashMap;
import java.util.Map;

public class GameplayComponent extends Component {
    private Map<Item.ItemType, Item> handItems = new HashMap<>();
    public GameplayComponent(Map<String, String> handItems) {
        super();
        initHandItems();
        for (Map.Entry<String, String> entry : handItems.entrySet()) {
            Item.ItemType type = Item.ItemType.valueOf(entry.getKey().toUpperCase());
            Item item = Game.em.getItem(entry.getValue());
            this.handItems.put(type, item);
        }
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
