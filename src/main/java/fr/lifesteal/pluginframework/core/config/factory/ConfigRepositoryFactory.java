package fr.lifesteal.pluginframework.core.config.factory;

import fr.lifesteal.pluginframework.core.config.repository.YamlConfigRepository;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class ConfigRepositoryFactory {
    private final Logger logger;
    private final String basePath;

    public ConfigRepositoryFactory(Logger logger, String basePath) {
        this.logger = logger;
        this.basePath = basePath;
    }

    public YamlConfigRepository getNewYamlConfigFactory(String subFolder, String fileName) {
        try {
            return new YamlConfigRepository(createNewConfigFile(getFilePath(subFolder), fileName));
        } catch (IOException e) {
            logger.severe("Création du fichier impossible : " + fileName);
            throw new RuntimeException(e);
        }
    }

    private String getFilePath(String subFolder) {
        return subFolder != null ? basePath + File.separator + subFolder + File.separator : basePath;
    }

    private File createNewConfigFile(String filePath, String fileName) throws IOException {
        logger.info("New ConfigFile : " + filePath + fileName);
        var file = new File(filePath, fileName);
        logger.info("File : " + file.getAbsolutePath());
        if (!file.exists()) {
            logger.info("Création du fichié");
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        return file;
    }
}
