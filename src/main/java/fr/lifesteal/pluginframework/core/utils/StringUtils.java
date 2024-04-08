package fr.lifesteal.pluginframework.core.utils;

public final class StringUtils {
    public static Integer tryParseInteger(String string) {

        if (string == null) {
            return null;
        }

        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }
}
