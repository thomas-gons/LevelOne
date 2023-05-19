package suchagame.ecs.component;

import suchagame.ecs.entity.Entity;
import suchagame.utils.Utils;

import com.fasterxml.jackson.databind.JsonNode;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;


public abstract class Component {


    public static void addNeededComponents(
            Entity entity, Iterator<Map.Entry<String,
            JsonNode>> fieldsIterator, Map.Entry<String,
            JsonNode> field)
    {

        while (true) {
            String rawClassName = String.format("suchagame.ecs.component.%sComponent", field.getKey());
            try {
                Class<? extends Component> componentClass = (Class<? extends Component>) Class.forName(rawClassName);
                Component.instantiateComponent(entity, componentClass, field.getValue());

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (!fieldsIterator.hasNext()) {
                break;
            }

            field = fieldsIterator.next();
        }
    }


    private static void instantiateComponent(
            Entity entity,
            Class<? extends Component> componentClass,
            JsonNode componentJson
    ) {

        Constructor<? extends Component> rightConstructor = Utils.findRightConstructor(componentClass, componentJson);
        int dependenciesCount = Arrays.stream(rightConstructor.getParameters())
                .filter(parameter -> Component.class.isAssignableFrom(parameter.getType()))
                .mapToInt(parameter -> 1)
                .sum();

        Class<?>[] parameterTypes = rightConstructor.getParameterTypes();
        Object[] arguments = new Object[componentJson.size() + dependenciesCount];

        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = componentJson.fields();
        int i = 0;
        for (; i < dependenciesCount; i++)
            arguments[i] = entity.getComponent((Class<? extends Component>) parameterTypes[i]);

        arguments = Utils.getConstructorArguments(
                fieldsIterator, rightConstructor,
                parameterTypes, arguments, dependenciesCount
        );

        try {
            entity.addComponent(rightConstructor.newInstance(arguments));
        } catch (InstantiationException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
