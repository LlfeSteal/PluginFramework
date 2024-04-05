package fr.lifesteal.pluginframework.core.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Todo: revoir cette classe.
public final class ColorUtils {
    public static String colorize(String message) {
        message = matchHexaReplace("&#", message);
        message = matchHexaReplace("#", message);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private static String matchHexaReplace(String match, String message) {
        final Pattern hexPattern = Pattern.compile(match + "([A-Fa-f0-9]{6})");
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find()) {
            matcher.appendReplacement(buffer, ChatColor.of("#" + matcher.group(1)).toString());
        }
        return matcher.appendTail(buffer).toString();
    }
}
