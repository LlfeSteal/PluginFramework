package fr.lifesteal.pluginframework.core.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

final class CommandParameterManager {
    private final String commandName;
    private final String usage;
    private final Map<Integer, CommandParameter> commandParameters = new HashMap<>();
    private int size = 0;
    private int minimumArgs = 0;
    private int argsToSkip = 0;

    public CommandParameterManager(String commandName, String usage) {
        this.commandName = commandName;
        this.usage = usage;
        this.initCommandParameters();
    }

    public String getParameterNameAtPosition(int position) {
        var parameter = this.commandParameters.get(position);
        return parameter != null ? parameter.name : null;
    }

    public boolean areParametersValid(String[] args) {
        var parameters = Arrays.stream(args).skip(argsToSkip).toArray();
        return parameters.length >= this.minimumArgs;
    }

    public Map<String, String> getParameterValues(String[] args) {
        var parameterValues = new HashMap<String, String>();

        for (int i = this.argsToSkip; i < args.length; ++i) {
            var parameterName = this.getParameterNameAtPosition(i);
            parameterValues.put(parameterName, args[i]);
        }

        return parameterValues;
    }

    public int getSize() {
        return size;
    }

    private void initCommandParameters() {
        if (this.usage == null) {
            return;
        }

        var args = this.usage.split(" ");
        this.size = args.length;

        for (int i = 0; i < this.size; ++i) {
            var parameterName = args[i];

            if (parameterName.contains(this.commandName)) {
                continue;
            }

            var parameter = getCommandParameter(parameterName);

            if (parameter == null) {
                ++this.argsToSkip;
                continue;
            } else if (parameter.isRequired) {
                ++this.minimumArgs;
            }

            commandParameters.put(i - 1, parameter);
        }
    }

    private CommandParameter getCommandParameter(String parameter) {
        if (parameter.startsWith("[") && parameter.endsWith("]")) {
            return new CommandParameter(parseParameterName(parameter), true);
        } else if (parameter.startsWith("<") && parameter.endsWith(">")) {
            return new CommandParameter(parseParameterName(parameter), false);
        } else {
            return null;
        }
    }

    private String parseParameterName(String parameter) {
        return parameter.replaceAll("[\\[\\]<>]", "");
    }

    private static final class CommandParameter {
        private final String name;
        private final boolean isRequired;

        public CommandParameter(String name, boolean isRequired) {
            this.name = name;
            this.isRequired = isRequired;
        }
    }
}
