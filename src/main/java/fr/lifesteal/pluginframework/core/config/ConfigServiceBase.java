package fr.lifesteal.pluginframework.core.config;

import fr.lifesteal.pluginframework.api.config.ConfigRepository;
import fr.lifesteal.pluginframework.api.config.ConfigService;

import javax.naming.OperationNotSupportedException;
import java.lang.reflect.Field;

public abstract class ConfigServiceBase implements ConfigService {

    private final ConfigRepository configRepository;

    protected ConfigServiceBase(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    public void initConfig() {
        for (var field : getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ConfigParam.class)) {
                setFieldValue(field);
            }
        }
    }

    private void setFieldValue(Field field) {
        var annotation = field.getAnnotation(ConfigParam.class);
        var key = annotation.paramKey();

        try {
            field.setAccessible(true);
            var defaultValue = field.get(this);
            var value = this.configRepository.getConfigValue(key, defaultValue);
            field.set(this, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
