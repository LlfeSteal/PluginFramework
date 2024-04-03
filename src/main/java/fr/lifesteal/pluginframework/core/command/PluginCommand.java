package fr.lifesteal.pluginframework.core.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class PluginCommand extends Command {

    private final CommandBase defaultCommand;
    private final List<CommandBase> subCommands;

    protected PluginCommand(String name, String description, String usageMessage, List<String> aliases, CommandBase defaultCommand, List<CommandBase> subCommands) {
        super(name, description, usageMessage, aliases);
        this.defaultCommand = defaultCommand;
        this.subCommands = subCommands;
    }

    @Override
    public boolean execute(CommandSender commandSender, String command, String[] args) {
        if (defaultCommand != null && subCommands.isEmpty()) {
            return this.defaultCommand.execute(commandSender, args);
        }

        var subCommand = GetSubCommand(args);
        return subCommand != null && subCommand.execute(commandSender, args);
    }

    private CommandBase GetSubCommand(String[] args) {
        if (args.length == 0) return null;

        for (var command : this.subCommands) {
            if (command.getName().equalsIgnoreCase(args[0])) {
                return command;
            }
        }

        return null;
    }
}
