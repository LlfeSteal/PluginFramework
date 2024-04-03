package fr.lifesteal.pluginframework.core.command;

import org.bukkit.command.CommandSender;

public abstract class CommandExecutor {

    private final CommandSender issuer;
    private final String[] args;

    public CommandExecutor(CommandSender issuer, String[] args) {
        this.issuer = issuer;
        this.args = args;
    }

    protected CommandSender getIssuer() {
        return issuer;
    }

    protected String[] getArgs() {
        return args;
    }

    public abstract boolean prepare();

    public abstract boolean execute();
}
