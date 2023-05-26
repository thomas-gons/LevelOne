package suchagame.ecs.component;

import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * Abstract class for components.
 */
public abstract class Component {
    public static class ComponentModel {
        private Class<? extends Component> componentClass;
        private Constructor<? extends Component> componentConstructor;
        private Map<String, Object> componentConstructorArgs;
        private int dependenciesCount;

        public ComponentModel(
                Class<? extends Component> componentClass,
                Constructor<? extends Component> componentConstructor,
                Map<String, Object> componentConstructorArgs,
                int dependenciesCount)
        {

            this.componentClass = componentClass;
            this.componentConstructor = componentConstructor;
            this.componentConstructorArgs = componentConstructorArgs;
            this.dependenciesCount = dependenciesCount;
        }

        public Class<? extends Component> getComponentClass() {
            return componentClass;
        }

        public Constructor<? extends Component> getComponentConstructor() {
            return componentConstructor;
        }

        public Map<String, Object> getComponentConstructorArgs() {
            return componentConstructorArgs;
        }

        public int getDependenciesCount() {
            return dependenciesCount;
        }
    }
}
