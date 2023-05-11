package suchagame.ecs.component;

import javafx.beans.property.SimpleMapProperty;
import javafx.beans.value.ObservableMapValue;
import suchagame.ecs.entity.Item;

import java.util.Map;

public class CharacterComponent extends Component {
    private ObservableMapValue<Item.ItemType, Item> handItems = new SimpleMapProperty<>();
    public CharacterComponent(Map<Item.ItemType, Item> handItems) {
        super();
        initHandItems();
        this.handItems.putAll(handItems);
    }

    private void initHandItems() {
        for (Item.ItemType type : Item.ItemType.values()) {
            this.handItems.put(type, null);
        }
    }
}
