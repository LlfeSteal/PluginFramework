package fr.lifesteal.pluginframework.core.command.builder;

import fr.lifesteal.pluginframework.api.config.FrameworkLangService;
import fr.lifesteal.pluginframework.core.command.CommandBase;
import fr.lifesteal.pluginframework.core.command.CommandExecutor;

import java.util.ArrayList;
import java.util.List;

public class CommandBuilder {
    private final FrameworkLangService langService;
    private final List<Object> extraArguments = new ArrayList<>();
    private String name;
    private String permission = null;
    private Class<? extends CommandExecutor> executorType;
    private boolean isDisabled = false;

    public CommandBuilder(FrameworkLangService langService) {
        this.langService = langService;
    }

    public CommandBuilder setDisabled(boolean disabled) {
        isDisabled = disabled;
        return this;
    }

    public CommandBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CommandBuilder setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public CommandBuilder setExecutorType(Class<? extends CommandExecutor> executorType) {
        this.executorType = executorType;
        return this;
    }

    public CommandBuilder addExtraArgument(Object extraArgument) {
        this.extraArguments.add(extraArgument);
        return this;
    }

    public CommandBase build() {
        return new CommandBase(this.langService, this.name, this.permission, this.isDisabled, this.executorType, this.extraArguments.toArray());
    }
}
