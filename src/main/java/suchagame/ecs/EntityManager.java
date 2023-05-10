package suchagame.ecs;

import suchagame.ecs.component.Component;
import suchagame.ecs.component.StatsComponent;
import suchagame.ecs.entity.*;
import suchagame.ecs.system.StatsSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EntityManager {
    public List<Entity> entities = new ArrayList<>();

    public EntityManager() {}

    public void init(int mobCount) {
        addItems();
        this.addEntity(new MapEntity());
        this.addEntity(new Player());
        for (int i = 0; i < mobCount; i++) {
            this.addEntity(new Mob());
        }
    }

    private void addItems() {
        this.addEntity(new Item("slimeDrop", 1));
    }

    public <T extends Component> List<Entity> getAllWithComponent(Class<T> componentClass) {
        List<Entity> entitiesWithComponent = new ArrayList<>();
        for (Entity entity : this.entities) {
            if (entity.hasComponent(componentClass))
                entitiesWithComponent.add(entity);
        }
        return entitiesWithComponent;
    }

    public MapEntity getMap() {
        return (MapEntity) this.entities.get(1);
    }
    public Player getPlayer() {
        return (Player) this.entities.get(2);
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
