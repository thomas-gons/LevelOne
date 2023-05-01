package suchagame.utils;


import java.util.Objects;

public class Utils {
    public static String getPathResource(Class<?> startPoint, String path) {
        return Objects.requireNonNull(startPoint.getResource(path)).toExternalForm();
    }
}
