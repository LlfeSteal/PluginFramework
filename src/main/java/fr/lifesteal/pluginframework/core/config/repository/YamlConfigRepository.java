package fr.lifesteal.pluginframework.core.config.repository;

import com.sun.jdi.InvalidTypeException;
import fr.lifesteal.pluginframework.api.config.ConfigRepository;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YamlConfigRepository implements ConfigRepository {
    private final File file;
    private final YamlConfiguration configFile;

    public YamlConfigRepository(File file) {
        this.file = file;
        this.configFile = YamlConfiguration.loadConfiguration(file);
        this.configFile.options().copyDefaults(true);
    }

    @Override
    public <T> T getConfigValue(String key, T defaultValue) {
        try {
            var value = configFile.getObject(key, (Class<T>) defaultValue.getClass());

            if (value == null) {
                value = defaultValue;
                this.configFile.addDefault(key, value);
                this.configFile.save(this.file);
            }

            return value;
        } catch (IOException e) {
            throw new RuntimeException(e);
            // Todo : Peut-être log warn et retourner la valeur par defaut ?
        }
    }
}
