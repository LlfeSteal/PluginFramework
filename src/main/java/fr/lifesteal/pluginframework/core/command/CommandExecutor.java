package fr.lifesteal.pluginframework.core.command;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class CommandExecutor {
    private final CommandSender issuer;
    private final Map<String, String> parameters;
    private final String[] args;

    public CommandExecutor(CommandSender issuer, Map<String, String> parameters) {
        this.issuer = issuer;
        this.parameters = parameters;
        this.args = parameters.values().toArray(String[]::new);
    }

    protected CommandSender getIssuer() {
        return issuer;
    }

    protected String[] getArgs() {
        return this.args;
    }

    protected String getArg(String name) {
        return parameters.get(name);
    }

    protected String getArg(int position) {
        return this.args.length - 1 >= position
                ? this.args[position]
                : null;
    }

    public List<String> getTabSuggestion(String paramName) {
        return new ArrayList<>(){{
            add(paramName);
        }};
    }

    public abstract boolean prepare();

    public abstract boolean execute();
}
