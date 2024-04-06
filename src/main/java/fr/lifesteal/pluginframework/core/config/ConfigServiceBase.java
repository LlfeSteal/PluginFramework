package fr.lifesteal.pluginframework.core.config;

import fr.lifesteal.pluginframework.api.config.ConfigRepository;
import fr.lifesteal.pluginframework.api.config.ConfigService;
import fr.lifesteal.pluginframework.core.utils.ColorUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ConfigServiceBase implements ConfigService {

    private final ConfigRepository configRepository;

    protected ConfigServiceBase(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    public void initConfig() {
        for (var field : getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(ConfigParam.class)) {
                setFieldValue(field);
            }
            if (field.isAnnotationPresent(Colorized.class)) {
                colorizeValue(field);
            }
            field.setAccessible(false);
        }
    }

    private void setFieldValue(Field field) {
        var annotation = field.getAnnotation(ConfigParam.class);
        var key = annotation.paramKey();

        try {
            var defaultValue = field.get(this);
            var value = this.configRepository.getConfigValue(key, defaultValue);
            field.set(this, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void colorizeValue(Field field) {
        try {
            var currentValue = field.get(this);
            field.set(this, getColorizedValue(currentValue));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T getColorizedValue(T value) {
        if (value instanceof String) {
            ColorUtils.colorize((String) value);
        } else if (value instanceof List) {
            var values = (List<?>) value;

            if(!values.isEmpty() && values.get(0) instanceof String) {
                var stringValues = (List<String>) values;
                return (T) stringValues.stream().map(element -> ColorUtils.colorize(element)).collect(Collectors.toList());
            }

            return value;
        }

        return value;
    }
}
