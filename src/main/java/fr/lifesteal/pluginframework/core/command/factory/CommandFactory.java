package fr.lifesteal.pluginframework.core.command.factory;

import fr.lifesteal.pluginframework.api.config.FrameworkLangService;
import fr.lifesteal.pluginframework.core.command.CommandBase;
import fr.lifesteal.pluginframework.core.command.CommandExecutor;
import fr.lifesteal.pluginframework.core.command.PluginCommand;

import java.util.ArrayList;

public class CommandFactory {
    private final FrameworkLangService langService;

    public CommandFactory(FrameworkLangService langService) {
        this.langService = langService;
    }

    public CommandBase getNewCommand(String name, String permission, boolean isDisabled, Class<? extends CommandExecutor> executorType, Object... executorExtraParams) {
        return new CommandBase(langService, name, permission, isDisabled, executorType, executorExtraParams);
    }

    public PluginCommand getNewPluginCommand(String name, String permission, Class<? extends CommandExecutor> executorType, Object... executorExtraParams) {
        var defaultCommand = getNewCommand(name, permission, false, executorType, executorExtraParams);
        return new PluginCommand(name, "", null, new ArrayList<>(), defaultCommand, new ArrayList<>());
    }
}
