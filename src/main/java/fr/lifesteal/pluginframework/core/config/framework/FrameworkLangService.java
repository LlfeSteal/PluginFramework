package fr.lifesteal.pluginframework.core.config.framework;

import fr.lifesteal.pluginframework.api.config.ConfigRepository;
import fr.lifesteal.pluginframework.core.config.ConfigParam;
import fr.lifesteal.pluginframework.core.config.ConfigServiceBase;

public class FrameworkLangService extends ConfigServiceBase {

    @ConfigParam(paramKey="truc.truc", defaultValue = "default")
    private String test;

    public FrameworkLangService(ConfigRepository configRepository) {
        super(configRepository);
    }
}
