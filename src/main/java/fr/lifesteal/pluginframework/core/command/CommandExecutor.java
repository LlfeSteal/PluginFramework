package fr.lifesteal.pluginframework.core.command;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

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

    public List<String> getTabSuggestion(String paramName) {
        return new ArrayList<>(){{
            add(paramName);
        }};
    }

    public abstract boolean prepare();

    public abstract boolean execute();

}
