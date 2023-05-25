package suchagame.ecs;

import org.yaml.snakeyaml.Yaml;
import suchagame.Main;
import suchagame.ecs.component.Component;
import suchagame.ecs.component.Dependency;
import suchagame.ecs.entity.Entity;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.util.*;

public class Model {
    Map<Class<? extends Entity>, Map<String, Entity.EntityModel>> models = new HashMap<>();

    @SafeVarargs
    @SuppressWarnings("rawtypes")
    public Model(Class<? extends Entity> ... entitiesClass) {
        // define yaml parser
        Yaml yaml = new Yaml();

        for (Class<? extends Entity> entityClass : entitiesClass) {
            // only get the first word of the class name (e.g. for EnemyProjectile, only get enemy)
            String rawClassName = entityClass.getSimpleName().split("(?=[A-Z][a-z])")[0].toLowerCase();
            try (InputStream inputStream = Main.class.getResourceAsStream("config/" + rawClassName + ".yml")) {
                // load yaml file data as it was defined in the file (same order)
                Map<String, Map> data = yaml.load(inputStream);

                initModel(data, entityClass);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void initModel(Map<String, Map> yamlData, Class<? extends Entity> baseClass) {
        Map<String, Map> entityData;
        HashMap<String, Entity.EntityModel> entityModels = new HashMap<>();

        // iterate over entities type (e.g. for each type of enemy, projectile, etc.)
        for (Map.Entry<String, Map> entry : yamlData.entrySet()) {
            entityData = entry.getValue();

            Entity.EntityModel entityModel = new Entity.EntityModel();
            entityModel.setEntityConstructor(baseClass.getConstructors()[0]);
            entityModel.setEntityConstructorArgs(new Object[entityModel.getEntityConstructor().getParameterCount()]);

            // metadata are the arguments for the entity constructor
            if (entityData.containsKey("metadata")) {
                initMetaData(entityData.get("metadata"), entityModel);
                entityData.remove("metadata");
            }

            // iterate over entity components
            for (Map.Entry<String, Map> componentEntry : entityData.entrySet()) {

                String componentRawClass = "suchagame.ecs.component." + componentEntry.getKey() + "Component";
                Class<? extends Component> componentClass = null;
                try {
                    componentClass = (Class<? extends Component>) Class.forName(componentRawClass);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                initComponent(componentEntry.getValue(), componentClass, entityModel);
            }
            entityModels.put(entry.getKey(), entityModel);
        }
        models.put(baseClass, entityModels);
    }

    private void initMetaData(Map<String, Object> metaData, Entity.EntityModel entityModel) {
        int i = 0;
        Object[] entityConstructorArgs = entityModel.getEntityConstructorArgs();
        Type[] genericTypes = entityModel.getEntityConstructor().getGenericParameterTypes();
        for (Map.Entry<String, Object> arg: metaData.entrySet()) {
            try {
                ParameterizedType genericType = (ParameterizedType) genericTypes[i];
                Type[] typeArguments = genericType.getActualTypeArguments();
                entityConstructorArgs[i] = castValueGeneric(arg.getValue(), typeArguments);
                i++;
                continue;
            } catch (ClassCastException ignore) {}
            entityConstructorArgs[i] = castValue(arg.getValue(), entityModel.getEntityConstructor().getParameterTypes()[i]);
            i++;
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponent(
            Map<String, Object> componentData,
            Class<? extends Component> componentClass,
            Entity.EntityModel entityModel)
    {

        List<Class<?>> componentDataTypes = new ArrayList<>();
        for (Map.Entry<String, Object> entry : componentData.entrySet()) {
            componentDataTypes.add(entry.getValue().getClass());
        }
        List<Component.ComponentModel> dependencies = new ArrayList<>();
        Constructor<?> componentConstructor = findConstructor(componentClass, dependencies, componentDataTypes, entityModel);
        assert componentConstructor != null;
        Object[] componentConstructorArgs = new Object[componentConstructor.getParameterCount()];

        int i = 0;
        for (; i < dependencies.size(); i++)
            componentConstructorArgs[i] = dependencies.get(i);

        Type[] genericTypes = componentConstructor.getGenericParameterTypes();
        for (Map.Entry<String, Object> entry : componentData.entrySet()) {
            try {
                ParameterizedType genericType = (ParameterizedType) genericTypes[i];
                Type[] typeArguments = genericType.getActualTypeArguments();
                componentConstructorArgs[i] = castValueGeneric(entry.getValue(), typeArguments);
                i++;
                continue;
            } catch (ClassCastException ignore) {}
            componentConstructorArgs[i] = castValue(entry.getValue(), componentConstructor.getParameterTypes()[i]);
            i++;
        }

        entityModel.addComponent(new Component.ComponentModel(
                componentClass,
                (Constructor<? extends Component>) componentConstructor,
                componentConstructorArgs,
                dependencies.size())
        );
    }

    @SuppressWarnings("unchecked")
    private Constructor<?> findConstructor(
            Class<? extends Component> componentClass,
            List<Component.ComponentModel> dependencies,
            List<Class<?>> componentDataTypes,
            Entity.EntityModel entityModel)
    {

        Constructor<?>[] constructors = componentClass.getConstructors();

        for (Constructor<?> constructor : constructors) {
            dependencies.clear();
            Class<?>[] dependenciesClasses;
            if (constructor.isAnnotationPresent(Dependency.class)) {
                dependenciesClasses = constructor.getAnnotation(Dependency.class).value();

                for (Class<?> dependenciesClass : dependenciesClasses)
                    dependencies.add(entityModel.getComponent((Class<? extends Component>) dependenciesClass));
            }
            int dependenciesCount = dependencies.size();
            if (constructor.getParameterCount() == componentDataTypes.size() + dependenciesCount) {
                boolean isRightConstructor = true;
                for (int i = 0; i < componentDataTypes.size(); i++) {
                    Class<?> expectedType = constructor.getParameterTypes()[i + dependenciesCount];
                    Class<?> currentType = componentDataTypes.get(i);

                    if (!currentType.equals(expectedType)) {
                        if (!currentType.isPrimitive()) {
                            try {
                                Object currentTypePrimitive = currentType.getField("TYPE").get(null);
                                if (currentTypePrimitive.equals(expectedType)) {
                                    continue;
                                }
                            } catch (NoSuchFieldException | IllegalAccessException ignored) {}
                        }

                        if (currentType.isAssignableFrom(Double.class) && expectedType.isAssignableFrom(float.class)) {
                            continue;
                        } else if (currentType.isAssignableFrom(String.class) && expectedType.isEnum()) {
                            continue;
                        } else if (currentType.isAssignableFrom(ArrayList.class) && expectedType.isArray()) {
                            continue;
                        } else if (currentType.isAssignableFrom(LinkedHashMap.class) && expectedType.isAssignableFrom(Map.class))
                            continue;

                        isRightConstructor = false;
                        break;
                    }
                }
                if (isRightConstructor) {
                    return constructor;
                }
            }
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private <T> T castValue(Object value, Class<T> targetType) {
        if (targetType.isInstance(value) && !targetType.isInterface()) {
            return targetType.cast(value);
        } else if (targetType == int.class && value instanceof Number) {
            return (T) Integer.valueOf(((Number) value).intValue());
        } else if ((targetType == float.class || targetType == Float.class) && value instanceof Number) {
            return (T) Float.valueOf(((Number) value).floatValue());
        } else if (targetType == double.class && value instanceof Number) {
            return (T) Double.valueOf(((Number) value).doubleValue());
        } else if (targetType.isEnum() && value instanceof String) {
            value = ((String) value).toUpperCase();
            return (T) Enum.valueOf((Class<? extends Enum>) targetType, (String) value);
        } else if (targetType.isArray() && value instanceof ArrayList<?> list) {
            Class<?> componentType = targetType.getComponentType();
            Object array = Array.newInstance(componentType, list.size());
            for (int i = 0; i < list.size(); i++) {
                Array.set(array, i, castValue(list.get(i), componentType));
            }
            return (T) array;
        }
        throw new IllegalArgumentException("Cannot cast " + value + " to " + targetType);
    }

    private Map<?, ?> castValueGeneric(Object value, Type[] typeArguments) {
        HashMap<Object, Object> map = new HashMap<>();
        for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
            Object key = castValue(entry.getKey(), (Class<?>) typeArguments[0]);
            Object val = castValue(entry.getValue(), (Class<?>) typeArguments[1]);
            map.put(key, val);
        }
        return map;
    }


    @SuppressWarnings("unchecked")
    public Entity loadModel(Class<? extends Entity> entityClass, String tag) {
        if (tag == null) {
            // if no tag is specified, use the first one
            tag = models.get(entityClass).keySet().iterator().next();
        }

        Entity.EntityModel entityModel = models.get(entityClass).get(tag);
        if (entityModel == null) {
            throw new IllegalArgumentException("No model for " + entityClass);
        }
        try {
            Entity entity = (Entity) entityModel.getEntityConstructor().newInstance(entityModel.getEntityConstructorArgs());
            for (Component.ComponentModel componentModel : entityModel.getComponents()) {
                Object[] componentConstructorArgs = componentModel.getComponentConstructorArgs();
                // load eventual component dependencies
                for (int i = 0; i < componentModel.getDependenciesCount(); i++) {
                    Class<?> dependencyClass = componentModel.getComponentConstructor().getParameterTypes()[i];
                    componentConstructorArgs[i] = entity.getComponent((Class<? extends Component>) dependencyClass);
                }
                Component component = componentModel.getComponentConstructor().newInstance(componentConstructorArgs);
                entity.addComponent(component);
            }
            return entity;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] getTags(Class<? extends Entity> entityClass) {
        return models.get(entityClass).keySet().toArray(new String[0]);
    }
}
