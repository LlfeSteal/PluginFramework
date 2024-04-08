package fr.lifesteal.pluginframework.core.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class PluginCommand extends Command {

    private final CommandBase defaultCommand;
    private final List<CommandBase> subCommands;

    public PluginCommand(String name, String description, String usageMessage, List<String> aliases, CommandBase defaultCommand, List<CommandBase> subCommands) {
        super(name, description, usageMessage, aliases);
        this.defaultCommand = defaultCommand;
        this.subCommands = subCommands;
    }

    @Override
    public boolean execute(CommandSender commandSender, String command, String[] args) {
        var commandToExecute = GetCommand(args);
        return commandToExecute != null && commandToExecute.execute(commandSender, args);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length <= 1 && !subCommands.isEmpty()) {
            return this.subCommands.stream().map(CommandBase::getName).collect(Collectors.toList());
        }

        var command = GetCommand(args);

        return command != null
                ? command.tabComplete(sender, args)
                : super.tabComplete(sender, alias, args);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        return this.tabComplete(sender, alias, args);
    }

    private CommandBase GetCommand(String[] args) {
        if (defaultCommand != null && args.length == 0) {
            return this.defaultCommand;
        }

        if (args.length == 0) return null;

        for (var command : this.subCommands) {
            if (command.getName().equalsIgnoreCase(args[0])) {
                return command;
            }
        }

        return null;
    }
}
