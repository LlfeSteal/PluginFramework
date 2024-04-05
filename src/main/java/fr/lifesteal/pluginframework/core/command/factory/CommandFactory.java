package fr.lifesteal.pluginframework.core.command.factory;

import fr.lifesteal.pluginframework.api.config.FrameworkLangService;
import fr.lifesteal.pluginframework.core.command.CommandBase;
import fr.lifesteal.pluginframework.core.command.CommandExecutor;

public class CommandFactory {
    private final FrameworkLangService langService;

    public CommandFactory(FrameworkLangService langService) {
        this.langService = langService;
    }

    public CommandBase getNewCommand(String name, String permission, boolean isDisabled, Class<? extends CommandExecutor> executorType) {
        return new CommandBase(langService, name, permission, isDisabled, executorType);
    }
}
