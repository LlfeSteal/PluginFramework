package fr.lifesteal.pluginframework.core.business;

import fr.lifesteal.pluginframework.api.config.ConfigRepository;
import fr.lifesteal.pluginframework.core.config.Colorized;
import fr.lifesteal.pluginframework.core.config.ConfigParam;
import fr.lifesteal.pluginframework.core.config.ConfigServiceBase;

import java.util.logging.Logger;

public class FrameworkLangService extends ConfigServiceBase implements fr.lifesteal.pluginframework.api.config.FrameworkLangService {
    @Colorized
    @ConfigParam(paramKey = "messages.error.command.default")
    private String defaultErrorMessage = "&cUne erreur est survenue.";

    @Colorized
    @ConfigParam(paramKey="messages.error.command.disabled")
    private String commandDisabledErrorMessage = "&cLa commande est désactivée.";

    @Colorized
    @ConfigParam(paramKey="messages.error.command.no-permission")
    private String commandNoPermissionErrorMessage = "&cVous n'avez pas la permission d'éxécuter cette commande.";

    @Colorized
    @ConfigParam(paramKey = "messages.info.command.usage")
    private String commandUsageMessage = "&aUsage: &e%command_usage%";

    public FrameworkLangService(Logger logger, ConfigRepository configRepository) {
        super(logger, configRepository);
    }

    @Override
    public String getDefaultErrorMessage() {
        return defaultErrorMessage;
    }

    @Override
    public String getCommandDisabledErrorMessage() {
        return commandDisabledErrorMessage;
    }

    @Override
    public String getCommandNoPermissionErrorMessage() {
        return commandNoPermissionErrorMessage;
    }

    @Override
    public String getCommandUsageMessage(String usage) {
        return commandUsageMessage.replace("%command_usage%", usage);
    }
}
