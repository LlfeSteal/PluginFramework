package fr.lifesteal.pluginframework.core.command;

import fr.lifesteal.pluginframework.api.config.FrameworkLangService;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CommandBase {
    private final FrameworkLangService langService;
    private final Logger logger;
    private final String name;
    private final String usage;
    private final String permission;
    private final Class<? extends CommandExecutor> executorType;
    private final List<Object> extraArguments;
    private final Constructor<? extends CommandExecutor> executorConstructor;
    private final CommandParameterManager parameterManager;
    private boolean isDisabled;

    public CommandBase(
            FrameworkLangService langService,
            Logger logger,
            String name,
            String permission,
            boolean isDisabled,
            String usage,
            Class<? extends CommandExecutor> executorType,
            Object... extraArguments) {
        this.langService = langService;
        this.logger = logger;
        this.name = name;
        this.permission = permission;
        this.isDisabled = isDisabled;
        this.usage = usage;
        this.executorType = executorType;
        this.extraArguments = extraArguments.length > 0 ? List.of(extraArguments) : new ArrayList<>();
        this.executorConstructor = getExecutorConstructor();
        this.parameterManager = new CommandParameterManager(this.name, this.usage);
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

        if (!this.parameterManager.areParametersValid(args)) {
            issuer.sendMessage(this.langService.getCommandUsageMessage(this.usage));
            return false;
        }

        var commandExecutor = getNewExecutorInstance(issuer, args);

        return commandExecutor.prepare() && commandExecutor.execute();
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length > this.parameterManager.getSize()) {
            return new ArrayList<>();
        }

        var commandExecutor = getNewExecutorInstance(sender, args);
        var parameterName = this.parameterManager.getParameterNameAtPosition(args.length - 1);
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
            add(parameterManager.getParameterValues(args));
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

    private Constructor<? extends CommandExecutor> getExecutorConstructor() {
        var parametersTypes = new ArrayList<Class<?>>() {{
            add(CommandSender.class);
            add(Map.class);
            addAll(extraArguments.stream().map(Object::getClass).collect(Collectors.toList()));
        }};

        try {
            return executorType.getConstructor(parametersTypes.toArray(Class<?>[]::new));
        } catch (NoSuchMethodException e) {
            var backupConstructor = getExecutionConstructorByImplementedInterfaces(parametersTypes);
            if (backupConstructor == null) {
                setDisabled(true);
                this.logger.severe("No executor constructor found for " + this.name + ". Command has been disabled.");
            }

            return backupConstructor;
        }
    }

    private Constructor<? extends CommandExecutor> getExecutionConstructorByImplementedInterfaces(List<Class<?>> parameterTypes) {
        for (var constructor : executorType.getConstructors()) {
            Class<?>[] constructorParameterTypes = constructor.getParameterTypes();
            if (constructorParameterTypes.length != parameterTypes.size()) {
                continue;
            }

            boolean match = true;
            for (int i = 0; i < constructorParameterTypes.length; i++) {
                if (!constructorParameterTypes[i].isAssignableFrom(parameterTypes.get(i))) {
                    match = false;
                    break;
                }
            }

            if (match) {
                return (Constructor<? extends CommandExecutor>) constructor;
            }
        }
        return null;
    }
}