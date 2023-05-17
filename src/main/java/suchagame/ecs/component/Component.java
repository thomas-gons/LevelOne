package suchagame.ecs.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import suchagame.ecs.entity.Entity;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class Component {
    public static void instantiateComponent(
            Entity entity,
            Class<? extends Component> componentClass,
            JsonNode componentJson
    ) {

        Constructor<? extends Component> rightConstructor = null;
        int dependenciesCount = 0;
        try {
            for (Constructor<?> constructor: componentClass.getConstructors()) {
                dependenciesCount = (int) Arrays.stream(constructor.getParameters())
                    .filter(parameter -> Component.class.isAssignableFrom(parameter.getType()))
                    .count();
                if ((constructor.getParameterCount() - dependenciesCount) == componentJson.size()) {
                    rightConstructor = (Constructor<? extends Component>) constructor;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        assert rightConstructor != null;
        Class<?>[] parameterTypes = rightConstructor.getParameterTypes();
        Object[] arguments = new Object[componentJson.size() + dependenciesCount];
        ObjectMapper mapper = new ObjectMapper();

        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = componentJson.fields();
        int i = 0;
        for (; i < dependenciesCount; i++)
            arguments[i] = entity.getComponent((Class<? extends Component>) parameterTypes[i]);

        while (fieldsIterator.hasNext()) {
            JsonNode value = fieldsIterator.next().getValue();
            if (value.isInt()) {
                arguments[i] = value.intValue();
            } else if (value.isFloat()) {
                arguments[i] = value.floatValue();
            } else if (value.isDouble()) {
                arguments[i] = value.floatValue();
            } else if (value.isTextual()) {
                arguments[i] = (parameterTypes[i].isEnum()) ?
                        Enum.valueOf((Class<? extends Enum>) parameterTypes[i], value.asText()):
                        value.asText();

            } else if (value.isArray()) {
                arguments[i] = mapper.convertValue(value, parameterTypes[i]);
            } else {
                Map<?, ?> map = mapper.convertValue(value, HashMap.class);
                JsonNode mapValue = value.fields().next().getValue();
                if (mapValue.isInt() || mapValue.isDouble()) {
                    arguments[i] = (Map<String, Float>) mapper.convertValue(map, HashMap.class);
                } else {
                    arguments[i] = (Map<String, String>) mapper.convertValue(map, HashMap.class);
                }
            }
            i++;
        }
        try {
            entity.addComponent(rightConstructor.newInstance(arguments));
        } catch (InstantiationException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
