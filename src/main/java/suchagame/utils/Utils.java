package suchagame.utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import suchagame.ecs.component.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class Utils {

    private static final Map<Class<?>, Class<?>> primitiveToWrapper = new HashMap<>(Map.of(
            String.class, TextNode.class,
            int.class, IntNode.class,
            float.class, DoubleNode.class,
            int[].class, ArrayNode.class,
            float[].class, ArrayNode.class,
            String[].class, ArrayNode.class,
            Map.class, ObjectNode.class
    ));



    public static String getPathResource(Class<?> startPoint, String path) {
        return Objects.requireNonNull(startPoint.getResource(path)).toExternalForm();
    }
    @SuppressWarnings({"unchecked"})
    public static Constructor<? extends Component> findRightConstructor(
        Class<? extends Component> componentClass,
        JsonNode componentJson) {

        for (Constructor<?> constructor : componentClass.getConstructors()) {
            int dependenciesCount = (int) Arrays.stream(constructor.getParameters())
                    .filter(parameter -> Component.class.isAssignableFrom(parameter.getType()))
                    .count();

            if ((constructor.getParameterCount() - dependenciesCount) == componentJson.size()) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Iterator<Map.Entry<String, JsonNode>> fieldsIterator = componentJson.fields();
                int i = dependenciesCount;
                boolean isRightConstructor = true;

                while (fieldsIterator.hasNext()) {
                    JsonNode field = fieldsIterator.next().getValue();
                    if (primitiveToWrapper.get(parameterTypes[i]) != field.getClass() &&
                            (!parameterTypes[i].isEnum() || !field.isTextual())) {
                        isRightConstructor = false;
                        break;
                    }
                    i++;
                }

                if (isRightConstructor) {
                    return (Constructor<? extends Component>) constructor;
                }
            }
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Object[] getConstructorArguments(
            Iterator<Map.Entry<String, JsonNode>> fieldsIterator,
            Constructor<?> rightConstructor,
            Class<?>[] parameterTypes,
            Object[] arguments,
            int dependenciesCount) {

        int i = dependenciesCount;
        ObjectMapper mapper = new ObjectMapper();

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
                        Enum.valueOf((Class<? extends Enum>) parameterTypes[i], value.asText().toUpperCase()) :
                        value.asText();

            } else if (value.isArray()) {
                arguments[i] = mapper.convertValue(value, parameterTypes[i]);
            } else {
                ParameterizedType parameterizedType = (ParameterizedType) rightConstructor.getGenericParameterTypes()[i];

                // Get the type arguments
                Type[] typeArguments = parameterizedType.getActualTypeArguments();

                // Create a new instance of ParameterizedType with the type arguments
                ParameterizedType mapType = new ParameterizedType() {
                    @Override
                    public Type[] getActualTypeArguments() {
                        return typeArguments;
                    }

                    @Override
                    public Type getRawType() {
                        return HashMap.class;
                    }

                    @Override
                    public Type getOwnerType() {
                        return parameterizedType.getOwnerType();
                    }
                };
                arguments[i] = mapper.convertValue(value, mapper.getTypeFactory().constructType(mapType));
            }
            i++;
        }
        return arguments;
    }
}
