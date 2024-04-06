package fr.lifesteal.pluginframework.core.command.builder;

import fr.lifesteal.pluginframework.core.command.CommandBase;
import fr.lifesteal.pluginframework.core.command.PluginCommand;

import java.util.ArrayList;
import java.util.List;

public class PluginCommandBuilder {
    private final List<String> aliases = new ArrayList<>();
    private final List<CommandBase> subCommands = new ArrayList<>();
    private String name = "";
    private String description;
    private String usageMessage;
    private CommandBase defaultCommand;

    public PluginCommandBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PluginCommandBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public PluginCommandBuilder setUsageMessage(String usageMessage) {
        this.usageMessage = usageMessage;
        return this;
    }

    public PluginCommandBuilder addAlias(String alias) {
        this.aliases.add(alias);
        return this;
    }

    public PluginCommandBuilder setDefaultCommand(CommandBase defaultCommand) {
        this.defaultCommand = defaultCommand;
        return this;
    }

    public PluginCommandBuilder setSubCommands(CommandBase subCommand) {
        this.subCommands.add(subCommand);
        return this;
    }

    public PluginCommand build() {
        return new PluginCommand(this.name, this.description, this.usageMessage, this.aliases, this.defaultCommand, this.subCommands);
    }
}
