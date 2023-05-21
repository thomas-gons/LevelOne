package suchagame.ecs;

import suchagame.ecs.component.Component;
import suchagame.ecs.component.StatsComponent;
import suchagame.ecs.entity.*;
import suchagame.ecs.system.StatsSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EntityManager {
    public List<Entity> entities = new ArrayList<>();
    private int itemsCount;

    public EntityManager() {}

    /**
     * Initializes the entities in the game.
     */
    public void initEntities() {
        int mobCount = 25;
        addItems();
        this.addEntity(new MapEntity());
        this.addEntity(new Player());
        this.addEntity(new NPC());
        for (int i = 0; i < mobCount; i++) {
            this.addEntity(new Mob());
        }
    }

    /**
     * Adds the predefined items to the entity manager.
     */
    private void addItems() {
        this.addEntity(new Item("slime_drop", Item.ItemType.MISC, 1));
        this.addEntity(new Item("fireball", Item.ItemType.SPELL, 250));
        this.addEntity(new Item("heal_potion", Item.ItemType.CONSUMABLE, 50,
                Map.entry("hp", 25f)
        ));
        this.addEntity(new Item("mana_potion", Item.ItemType.CONSUMABLE, 10,
                Map.entry("mp", 25f)
        ));
        this.addEntity(new Item("speed_potion", Item.ItemType.CONSUMABLE, 10,
                Map.entry("spd", 1f)
        ));
        itemsCount = Entity.entitiesCount;
    }

    /**
     * Retrieves all entities that have the specified component.
     *
     * @param componentClass the class of the component
     * @param <T>            the type of the component
     * @return a list of entities with the specified component
     */
    public <T extends Component> List<Entity> getAllWithComponent(Class<T> componentClass) {
        List<Entity> entitiesWithComponent = new ArrayList<>();
        for (Entity entity : this.entities) {
            if (entity.hasComponent(componentClass))
                entitiesWithComponent.add(entity);
        }
        return entitiesWithComponent;
    }

    /**
     * Retrieves all items in the entity manager.
     *
     * @return an array of all items
     */
    public Item[] getAllItems() {
        Item[] items = new Item[itemsCount];
        for (int i = 0; i < itemsCount; i++) {
            items[i] = (Item) this.entities.get(i);
        }
        return items;
    }

    /**
     * Retrieves the map entity from the entity manager.
     *
     * @return the map entity
     */
    public MapEntity getMap() {
        return (MapEntity) this.entities.get(itemsCount);
    }

    /**
     * Retrieves the player entity from the entity manager.
     *
     * @return the player entity
     */
    public Player getPlayer() {
        return (Player) this.entities.get(itemsCount + 1);
    }

    /**
     * Retrieves the NPC entity from the entity manager.
     *
     * @return the NPC entity
     */
    public NPC getNPC() {
        return (NPC) this.entities.get(itemsCount + 2);
    }

    /**
     * Adds an entity to the entity manager. If the entity has a StatsComponent, it adds an observer to it.
     *
     * @param entity the entity to add
     */
    public void addEntity(Entity entity) {
        this.entities.add(entity);
        if (entity.hasComponent(StatsComponent.class)) {
            StatsSystem.addObserver(entity);
        }
    }

    /**
     * Removes an entity from the entity manager.
     *
     * @param entity the entity to remove
     */
    public void removeEntity(Entity entity) {
        this.entities.remove(entity);
    }

    /**
     * Retrieves the number of entities in the entity manager.
     *
     * @return the number of entities
     */
    public int getEntityCount() {
        return this.entities.size();
    }

    /**
     * Retrieves the number of items in the entity manager.
     *
     * @return the number of items
     */
    public int getItemsCount() {
        return itemsCount;
    }

    /**
     * Retrieves an item from the entity manager based on its tag.
     *
     * @param tag the tag of the item
     * @return the item with the specified tag, or null if not found
     */
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

    /**
     * Removes all entities from the entity manager.
     */
    public void removeAllEntities() {
        this.entities.clear();
    }
}

