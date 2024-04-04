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
        var defaultValue = annotation.defaultValue();
        var type = field.getType();
        field.setAccessible(true);

        try {
            var typedValue = GetTypedValue(defaultValue, type);
            var value = this.configRepository.getConfigValue(key, typedValue);
            field.set(this, value);
        } catch (Exception ex) {
            try {
                field.set(this, defaultValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private static <T> T GetTypedValue(String value, Class<T> type) throws OperationNotSupportedException {
        var typeName = type.getTypeName();
        if (typeName.equalsIgnoreCase(String.class.getTypeName())) {
            return (T) value;
        } else if (typeName.equalsIgnoreCase(Integer.class.getTypeName()) || typeName.equalsIgnoreCase(int.class.getTypeName())) {
            return (T) (Object)Integer.parseInt(value);
        }  else if (typeName.equalsIgnoreCase(Double.class.getTypeName()) || typeName.equalsIgnoreCase(double.class.getTypeName())) {
            return (T) (Object)Double.parseDouble(value);
        }  else if (typeName.equalsIgnoreCase(Boolean.class.getTypeName()) || typeName.equalsIgnoreCase(boolean.class.getTypeName())) {
            return (T) (Object)Boolean.parseBoolean(value);
        }  else if (typeName.equalsIgnoreCase(Long.class.getTypeName()) || typeName.equalsIgnoreCase(long.class.getTypeName())) {
            return (T) (Object)Long.parseLong(value);
        }  else if (typeName.equalsIgnoreCase(Float.class.getTypeName()) || typeName.equalsIgnoreCase(float.class.getTypeName())) {
            return (T) (Object)Float.parseFloat(value);
        }  else if (typeName.equalsIgnoreCase(Character.class.getTypeName()) || typeName.equalsIgnoreCase(char.class.getTypeName())) {
            return (T) (Object)value.charAt(0);
        }  else if (typeName.equalsIgnoreCase(Short.class.getTypeName()) || typeName.equalsIgnoreCase(short.class.getTypeName())) {
            return (T) (Object)Short.parseShort(value);
        }  else if (typeName.equalsIgnoreCase(Byte.class.getTypeName()) || typeName.equalsIgnoreCase(byte.class.getTypeName())) {
            return (T) (Object)Byte.parseByte(value);
        } else {
            throw new OperationNotSupportedException("No type defined : (" + typeName + ") " + value );
        }
    }
}
