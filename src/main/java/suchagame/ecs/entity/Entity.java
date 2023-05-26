package suchagame.ecs.entity;

import suchagame.ecs.component.Component;

import java.lang.reflect.Constructor;
import java.util.*;

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

    public static class EntityModel {
        private Constructor<?> entityConstructor;
        private Map<String, Object> entityConstructorArgs;
        private List<Component.ComponentModel> components = new ArrayList<>();
        public Constructor<?> getEntityConstructor() {
            return entityConstructor;
        }

        public Set<Class<? extends Component>> dynamicComponents = new HashSet<>();

        public void setEntityConstructor(Constructor<?> entityConstructor) {
            this.entityConstructor = entityConstructor;
        }

        public Map<String, Object> getEntityConstructorArgs() {
            return entityConstructorArgs;
        }

        public void setEntityConstructorArgs(Map<String, Object> entityConstructorArgs) {
            this.entityConstructorArgs = entityConstructorArgs;
        }

        public List<Component.ComponentModel> getComponents() {
            return components;
        }

        public Component.ComponentModel getComponent(Class<? extends Component> componentClass) {
            for (Component.ComponentModel component : this.components) {
                if (component.getComponentClass().equals(componentClass)) {
                    return component;
                }
            }
            return null;
        }

        public void setComponents(List<Component.ComponentModel> components) {
            this.components = components;
        }

        public void addComponent(Component.ComponentModel component) {
            this.components.add(component);
        }

        public void addDynamicComponent(Class<? extends Component> componentClass) {
            this.dynamicComponents.add(componentClass);
        }
        public HashSet<Class<? extends Component>> getDynamicComponents() {
            return (HashSet<Class<? extends Component>>) this.dynamicComponents;
        }

        public boolean hasDynamicComponent(Class<? extends Component> componentClass) {
            return this.dynamicComponents.contains(componentClass);
        }
    }
}


