package unit.config.utils;

import fr.lifesteal.pluginframework.api.config.ConfigRepository;
import fr.lifesteal.pluginframework.core.config.Colorized;
import fr.lifesteal.pluginframework.core.config.ConfigParam;
import fr.lifesteal.pluginframework.core.config.ConfigServiceBase;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class FakeConfigService extends ConfigServiceBase {

    @Colorized
    @ConfigParam(paramKey = "message")
    private String message;

    @Colorized
    @ConfigParam(paramKey = "messages")
    private List<String> messages;

    @ConfigParam(paramKey = "data")
    private Map<String, String> data;

    public FakeConfigService(Logger logger, ConfigRepository configRepository, String message, List<String> messages, Map<String, String> data) {
        super(logger, configRepository);
        this.message = message;
        this.messages = messages;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getMessages() {
        return messages;
    }

    public Map<String, String> getData() {
        return data;
    }
}
