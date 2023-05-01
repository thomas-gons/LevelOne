package suchagame.ecs;

import suchagame.ecs.component.Component;
import suchagame.ecs.entity.Entity;
import suchagame.ecs.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {
    public static EntityManager instance = new EntityManager();
    public List<Entity> entities = new ArrayList<>();

    public <T extends Component> List<Entity> getAllWithComponent(Class<T> componentClass) {
        List<Entity> entitiesWithComponent = new ArrayList<>();
        for (Entity entity : this.entities) {
            if (entity.hasComponent(componentClass))
                entitiesWithComponent.add(entity);
        }
        return entitiesWithComponent;
    }

    public Player getPlayer() {
        for (Entity entity : this.entities) {
            if (entity instanceof Player)
                return (Player) entity;
        }
        return null;
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }
}
