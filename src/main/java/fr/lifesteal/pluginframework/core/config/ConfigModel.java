package fr.lifesteal.pluginframework.core.config;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public abstract class ConfigModel implements Cloneable, ConfigurationSerializable {

    public ConfigModel() {
    }

    public ConfigModel(Map<String, Object> args) {
        var fields = this.getClass().getDeclaredFields();

        for (var field : fields) {
            field.setAccessible(true);
            try {
                field.set(this, args.get(field.getName()));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Map<String, Object> serialize() {
        var fields = this.getClass().getDeclaredFields();

        var serializedObject = new HashMap<String, Object>();
        for (var field : fields) {
            field.setAccessible(true);
            try {
                serializedObject.put(field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return serializedObject;
    }

    @Override
    public abstract ConfigModel clone();
}
