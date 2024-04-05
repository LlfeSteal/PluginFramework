package fr.lifesteal.pluginframework.core.config;

import fr.lifesteal.pluginframework.api.config.ConfigRepository;
import fr.lifesteal.pluginframework.api.config.ConfigService;
import fr.lifesteal.pluginframework.core.utils.ColorUtils;

import javax.naming.OperationNotSupportedException;
import java.lang.reflect.Field;

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
            field.set(this, ColorUtils.colorize(currentValue.toString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
