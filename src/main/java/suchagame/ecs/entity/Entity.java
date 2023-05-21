package suchagame.ecs.entity;

import suchagame.ecs.component.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for all entities.
 */
public abstract class Entity {
    public static int entitiesCount = 0;

    // map of components for each entity
    private final Map<Class<? extends Component>, Component> components = new HashMap<>();

    /**
     * Default constructor.
     */
    public Entity() {
        entitiesCount++;
    }

    /**
     * Checks if entity has component of given class.
     * @param componentClass class of component to check
     * @return true if entity has component of given class, false otherwise
     * @param <T> component class
     */
    public <T extends Component> boolean hasComponent(Class<T> componentClass) {
        return this.getComponent(componentClass) != null;
    }

    /**
     * Adds component to entity.
     * @param component component to add
     */
    public void addComponent(Component component) {
        if (!hasComponent(component.getClass())) {
            this.components.put(component.getClass(), component);
        }
    }

    /**
     * get component of given class
     * @param componentClass class of component to get
     * @return component of given class
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> componentClass) {
        return (T) this.components.get(componentClass);
    }
}
