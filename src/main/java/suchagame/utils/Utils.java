package suchagame.utils;


import javafx.geometry.BoundingBox;

import java.util.Objects;

/**
 * A class containing utility methods.
 */
public class Utils {
    /**
     * Gets the relative path of a resource in target directory.
     * @param startPoint The class to start the search from.
     * @param path The path of the resource.
     * @return The relative path of the resource.
     */
    public static String getPathResource(Class<?> startPoint, String path) {
        return Objects.requireNonNull(startPoint.getResource(path)).toExternalForm();
    }

    /**
     * Translates a hit box by a given offset.
     * @param offset The offset to translate by.
     * @param hitBox The hit box to translate.
     * @return The translated hit box.
     */
    public static BoundingBox translateHitBox(Vector2f offset, BoundingBox hitBox) {
        return new BoundingBox(
                hitBox.getMinX() + offset.getX(),
                hitBox.getMinY() + offset.getY(),
                hitBox.getWidth(),
                hitBox.getHeight()
        );
    }


    public static Class<?> getPrimitiveType(Class<?> currentType) {
        if (!currentType.isPrimitive()) {
            try {
                Object currentTypePrimitive = currentType.getField("TYPE").get(null);
                return (Class<?>) currentTypePrimitive;
            } catch (NoSuchFieldException | IllegalAccessException ignored) {}
        }
        return currentType;
    }
}
