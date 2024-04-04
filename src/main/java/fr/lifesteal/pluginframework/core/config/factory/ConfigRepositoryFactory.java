package fr.lifesteal.pluginframework.core.config.factory;

import fr.lifesteal.pluginframework.core.config.repository.YamlConfigRepository;

import java.io.File;
import java.io.IOException;

public class ConfigRepositoryFactory {
    private final String basePath;

    public ConfigRepositoryFactory(String basePath) {
        this.basePath = basePath;
    }

    public YamlConfigRepository getNewYamlConfigFactory(String subFolder, String fileName) {
        try {
            return new YamlConfigRepository(createNewConfigFile(getFilePath(subFolder), fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFilePath(String subFolder) {
        return subFolder != null ? basePath + "/" + subFolder : basePath;
    }

    private File createNewConfigFile(String filePath, String fileName) throws IOException {
        var file = new File(filePath, fileName);

        if (!file.exists()) {
            file.createNewFile();
        }

        return file;
    }
}
