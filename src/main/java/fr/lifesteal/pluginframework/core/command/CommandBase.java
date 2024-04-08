package fr.lifesteal.pluginframework.core.command;

import fr.lifesteal.pluginframework.api.config.FrameworkLangService;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class CommandBase {
    private final FrameworkLangService langService;
    private final String name;
    private final String usage;
    private final String permission;
    private final Class<? extends CommandExecutor> executorType;
    private final List<Object> extraArguments;
    private final Constructor<? extends CommandExecutor> executorConstructor;
    private final Map<Integer, String> parameterPosition;
    private boolean isDisabled;

    public CommandBase(
            FrameworkLangService langService,
            String name,
            String permission,
            boolean isDisabled,
            String usage,
            Class<? extends CommandExecutor> executorType,
            Object... extraArguments) {
        this.langService = langService;
        this.name = name;
        this.permission = permission;
        this.isDisabled = isDisabled;
        this.usage = usage;
        this.executorType = executorType;
        this.extraArguments = extraArguments.length > 0 ? List.of(extraArguments) : new ArrayList<>();
        this.executorConstructor = getExecutorConstructor();
        this.parameterPosition = getCommandParameterPositions();
    }

    public boolean execute(CommandSender issuer, String[] args) {
        if (this.isDisabled) {
            issuer.sendMessage(this.langService.getCommandDisabledErrorMessage());
            return false;
        }

        if (!checkPermission(issuer)) {
            issuer.sendMessage(this.langService.getCommandNoPermissionErrorMessage());
            return false;
        }

        var commandExecutor = getNewExecutorInstance(issuer, args);

        return commandExecutor.prepare() && commandExecutor.execute();
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        var commandExecutor = getNewExecutorInstance(sender, args);

        if (args.length > this.parameterPosition.size()) {
            return new ArrayList<>();
        }

        var parameterName = this.parameterPosition.get(args.length);
        return commandExecutor.getTabSuggestion(parameterName != null ? parameterName : "");
    }

    public String getName() {
        return name;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    private CommandExecutor getNewExecutorInstance(CommandSender issuer, String[] args) {
        var parameters = new ArrayList<>() {{
            add(issuer);
            add(args);
            addAll(extraArguments);
        }};

        try {
            return this.executorConstructor.newInstance(parameters.toArray());
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            issuer.sendMessage(this.langService.getDefaultErrorMessage());
            throw new RuntimeException(e);
        }
    }

    private boolean checkPermission(CommandSender issuer) {
        return this.permission == null || issuer.hasPermission(this.permission);
    }

    private Map<Integer, String> getCommandParameterPositions() {
        var argumentsPositions = new HashMap<Integer, String>();
        var args = this.usage
                .replace("[", "")
                .replace("]", "")
                .split(" ");

        for (int i = 0; i < args.length; ++i) {
            argumentsPositions.put(i, args[i]);
        }

        return argumentsPositions;
    }

    private Constructor<? extends CommandExecutor> getExecutorConstructor() {
        var parametersTypes = new ArrayList<Class<?>>() {{
            add(CommandSender.class);
            add(String[].class);
            addAll(extraArguments.stream().map(Object::getClass).collect(Collectors.toList()));
        }};

        try {
            return executorType.getConstructor(parametersTypes.toArray(Class<?>[]::new));
        } catch (NoSuchMethodException e) {
            setDisabled(true);
            // Todo : Ajouter log d'error.
        }

        return null;
    }
}