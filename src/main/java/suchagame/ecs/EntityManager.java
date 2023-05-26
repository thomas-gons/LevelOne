package suchagame.ecs;

import org.jetbrains.annotations.Nullable;
import suchagame.ecs.component.Component;
import suchagame.ecs.component.StatsComponent;
import suchagame.ecs.entity.*;

import java.util.*;

import suchagame.ecs.system.StatsSystem;


public class EntityManager {
    private final Model model;
    public List<Entity> entities = new ArrayList<>();
    private int itemsCount;

    public EntityManager() {
        model = new Model(
                Item.class,
                MapEntity.class,
                Player.class,
                Projectile.class,
                NPC.class,
                Mob.class
        );
    }

    /**
     * Initializes the entities in the game.
     */
    public void initEntities() {
        addEntity(Item.class, "*");
        addEntity(MapEntity.class);
        addEntity(Player.class);
        addEntity(NPC.class, "blacksmith");
        for (int i = 0; i < 10; i++) {
            addEntity(Mob.class, "slime");
        }
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
     */
    public void addEntity(Class<? extends Entity> entityClass) {
        addEntity(entityClass, null);
    }

    public void addEntity(Class<? extends Entity> entityClass, @Nullable String tag) {
        if (tag != null && tag.equals("*")) {
            for (String newTag: model.getTags(entityClass)) {
                addEntity(entityClass, newTag);
            }
            return;
        }

        Entity entity = model.loadModel(entityClass, tag);
        this.entities.add(entity);
        if (entity.hasComponent(StatsComponent.class)) {
            StatsSystem.addObserver(entity);
        }

        if (entity instanceof Item) {
            itemsCount++;
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

    public List<Entity> getEntities() {
        return entities;
    }

    /**
     * Removes all entities from the entity manager.
     */
    public void removeAllEntities() {
        this.entities.clear();
    }


    public Object getMetaDataInModel(Class<? extends Entity> entityClass, String tag, String key) {
        return model.getMetadata(entityClass, tag, key);
    }

    public void toggleFlagInModel(Class<? extends Entity> entityClass, String tag, String key) {
        model.toggleFlag(entityClass, tag, key);
    }
}


