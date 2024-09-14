package unit.config;

import fr.lifesteal.pluginframework.api.config.ConfigRepository;
import org.junit.jupiter.api.Test;
import unit.config.utils.FakeConfigService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfigServiceBaseTest {

    @Test
    public void initConfigShouldLoadConfiguration() {
        // Arrange
        var message = "&6message";
        var messages = new ArrayList<String>(){{
            add("&1Message1");
            add("&2Message1");
            add("&3Message1");
            add("&4Message1");
            add("&5Message1");
        }};
        Map<String, String> defaultData = new HashMap<>() {{
            put("KEY1", "DATA1");
            put("KEY2", "DATA2");
            put("KEY3", "DATA3");
            put("KEY4", "DATA4");
        }};

        var data = new HashMap<String, String>() {{
            put("KEY2", "DATA2");
            put("KEY3", "DATA3");
            put("KEY4", "DATA4");
        }};

        var logger = mock(Logger.class);
        var configRepository = mock(ConfigRepository.class);
        when(configRepository.getConfigValue("message", message)).thenReturn(message);
        when(configRepository.getConfigValue("messages", messages)).thenReturn(messages);
        when(configRepository.getConfigValue("data", defaultData)).thenReturn(data);

        var configService = new FakeConfigService(logger, configRepository, message, messages, defaultData);

        // Act
        configService.initConfig();

        // Assert
        assertThat(configService.getMessage()).isEqualTo("§6message");
        assertThat(configService.getMessages()).isEqualTo(new ArrayList<String>(){{
            add("§1Message1");
            add("§2Message1");
            add("§3Message1");
            add("§4Message1");
            add("§5Message1");
        }});
        assertThat(configService.getData()).isEqualTo(data);
    }
}
