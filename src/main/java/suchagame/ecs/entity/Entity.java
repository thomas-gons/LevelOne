package suchagame.ecs.entity;

import suchagame.ecs.component.Component;

import java.util.HashMap;
import java.util.Map;

public abstract class Entity {
    private Map<Class<? extends Component>, Component> components = new HashMap<>();

    public <T extends Component> boolean hasComponent(Class<T> componentClass) {
        return this.getComponent(componentClass) != null;
    }

    public void addComponent(Component component) {
        if (!hasComponent(component.getClass())) {
            this.components.put(component.getClass(), component);
        }
    }
    public void addAllComponents(Component ... components) {
        for (Component component: components)
            this.addComponent(component);
    }
    public <T extends Component> T getComponent(Class<T> componentClass) {
        return (T) this.components.get(componentClass);
    }
}
