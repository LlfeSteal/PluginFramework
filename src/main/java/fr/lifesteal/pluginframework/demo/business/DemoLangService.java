package fr.lifesteal.pluginframework.demo.business;

import fr.lifesteal.pluginframework.api.config.ConfigRepository;
import fr.lifesteal.pluginframework.core.config.Colorized;
import fr.lifesteal.pluginframework.core.config.ConfigParam;
import fr.lifesteal.pluginframework.core.config.ConfigServiceBase;

public class DemoLangService extends ConfigServiceBase {

    @Colorized
    @ConfigParam(paramKey = "messages.info.pong")
    private String pongMessage = "&aPong !.";

    public DemoLangService(ConfigRepository configRepository) {
        super(configRepository);
    }

    public String getPongMessage() {
        return pongMessage;
    }
}
