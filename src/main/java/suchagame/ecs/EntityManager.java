package suchagame.ecs;

import suchagame.ecs.component.Component;
import suchagame.ecs.component.StatsComponent;
import suchagame.ecs.entity.*;
import suchagame.ecs.system.StatsSystem;

import java.util.*;

public class EntityManager {
    public List<Entity> entities = new ArrayList<>();
    private int itemsCount;

    public EntityManager() {}

    public void initEntities() {
        int mobCount = 256;
        addItems();
        this.addEntity(new MapEntity());
        this.addEntity(new Player());
        this.addEntity(new NPC());
        for (int i = 0; i < mobCount; i++) {
            this.addEntity(new Mob());
        }
    }

    private void addItems() {
        this.addEntity(new Item("slime_drop", Item.ItemType.MISC, 1));
        this.addEntity(new Item("fireball", Item.ItemType.SPELL, 250));
        this.addEntity(new Item("heal_potion", Item.ItemType.CONSUMABLE, 100,
                Map.entry("hp", 25f)
        ));
        this.addEntity(new Item("mana_potion", Item.ItemType.CONSUMABLE, 10,
                Map.entry("mp", 25f)
        ));
        this.addEntity(new Item("speed_potion", Item.ItemType.CONSUMABLE, 10,
                Map.entry("spd", 1f)
        ));
        itemsCount = Entity.globalID;
    }

    public <T extends Component> List<Entity> getAllWithComponent(Class<T> componentClass) {
        List<Entity> entitiesWithComponent = new ArrayList<>();
        for (Entity entity : this.entities) {
            if (entity.hasComponent(componentClass))
                entitiesWithComponent.add(entity);
        }
        return entitiesWithComponent;
    }

    public Item[] getAllItems() {
        Item[] items = new Item[itemsCount];
        for (int i = 0; i < itemsCount; i++) {
            items[i] = (Item) this.entities.get(i);
        }
        return items;
    }

    public MapEntity getMap() {
        return (MapEntity) this.entities.get(itemsCount);
    }
    public Player getPlayer() {
        return (Player) this.entities.get(itemsCount + 1);
    }
    public NPC getNPC() {
        return (NPC) this.entities.get(itemsCount + 2);
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
        if (entity.hasComponent(StatsComponent.class)) {
            StatsSystem.addObserver(entity);
        }
}

    public void removeEntity(Entity entity) {
        this.entities.remove(entity);
    }

    public int getEntityCount() {
        return this.entities.size();
    }

    public Item getItem(String tag) {
        for (Entity entity : this.entities) {
            if (entity instanceof Item item) {
                if (Objects.equals(item.getTag(), tag)) {
                    return item;
                }
            } else {
                break;
            }
        }
        return null;
    }
}
