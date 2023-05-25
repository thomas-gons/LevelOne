package suchagame.ecs.component;

import java.lang.reflect.Constructor;

/**
 * Abstract class for components.
 */
public abstract class Component {
    public static class ComponentModel {
        private Class<? extends Component> componentClass;
        private Constructor<? extends Component> componentConstructor;
        private Object[] componentConstructorArgs;
        private int dependenciesCount;

        public ComponentModel(
                Class<? extends Component> componentClass,
                Constructor<? extends Component> componentConstructor,
                Object[] componentConstructorArgs,
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

        public void setComponentClass(Class<? extends Component> componentClass) {
            this.componentClass = componentClass;
        }

        public Constructor<? extends Component> getComponentConstructor() {
            return componentConstructor;
        }

        public void setComponentConstructor(Constructor<? extends Component> componentConstructor) {
            this.componentConstructor = componentConstructor;
        }

        public Object[] getComponentConstructorArgs() {
            return componentConstructorArgs;
        }

        public void setComponentConstructorArgs(Object[] componentConstructorArgs) {
            this.componentConstructorArgs = componentConstructorArgs;
        }

        public int getDependenciesCount() {
            return dependenciesCount;
        }

        public void setDependenciesCount(int dependenciesCount) {
            this.dependenciesCount = dependenciesCount;
        }
    }
}
