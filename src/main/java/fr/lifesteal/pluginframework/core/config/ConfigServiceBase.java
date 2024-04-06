package fr.lifesteal.pluginframework.core.config;

import fr.lifesteal.pluginframework.api.config.ConfigRepository;
import fr.lifesteal.pluginframework.api.config.ConfigService;
import fr.lifesteal.pluginframework.core.utils.ColorUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class ConfigServiceBase implements ConfigService {

    private final Logger logger;
    private final ConfigRepository configRepository;

    protected ConfigServiceBase(Logger logger, ConfigRepository configRepository) {
        this.logger = logger;
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
            logger.log(Level.SEVERE, "Error was thrown while attempting to get config value for parameter : " + field.getName(), e);
        }
    }

    private void colorizeValue(Field field) {
        try {
            var currentValue = field.get(this);
            field.set(this, getColorizedValue(currentValue, field.getName()));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error was thrown while attempting to colorize parameter : " + field.getName(), e);
        }
    }

    private <T> T getColorizedValue(T value, String fieldName) {
        if (value instanceof String) {
            return (T) ColorUtils.colorize((String) value);
        } else if (value instanceof List && !((List<?>) value).isEmpty() && ((List<?>) value).get(0) instanceof String) {
            return (T) ((List<String>)value).stream().map(ColorUtils::colorize).collect(Collectors.toList());
        }
        else {
            this.logger.warning("Colorized parameter not supported : " + fieldName);
        }

        return value;
    }
}
