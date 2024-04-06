package fr.lifesteal.pluginframework.core.business;

import fr.lifesteal.pluginframework.api.config.ConfigRepository;
import fr.lifesteal.pluginframework.core.config.Colorized;
import fr.lifesteal.pluginframework.core.config.ConfigParam;
import fr.lifesteal.pluginframework.core.config.ConfigServiceBase;

public class FrameworkLangService extends ConfigServiceBase implements fr.lifesteal.pluginframework.api.config.FrameworkLangService {

    @Colorized
    @ConfigParam(paramKey="messages.error.command.disabled")
    private String commandDisabledErrorMessage = "&cLa commande est désactivée.";

    @Colorized
    @ConfigParam(paramKey="messages.error.command.no-permission")
    private String commandNoPermissionErrorMessage = "&cVous n'avez pas la permission d'éxécuter cette commande.";

    public FrameworkLangService(ConfigRepository configRepository) {
        super(configRepository);
    }

    @Override
    public String getCommandDisabledErrorMessage() {
        return commandDisabledErrorMessage;
    }

    @Override
    public String getCommandNoPermissionErrorMessage() {
        return commandNoPermissionErrorMessage;
    }
}
