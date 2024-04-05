package fr.lifesteal.pluginframework.core.command;

import fr.lifesteal.pluginframework.api.config.FrameworkLangService;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CommandBase {
    private final FrameworkLangService langService;
    private final String name;
    private final String permission;
    private final Class<? extends CommandExecutor> executorType;
    private final List<Object> extraArguments;
    private boolean isDisabled;

    public CommandBase(FrameworkLangService langService, String name, String permission, boolean isDisabled, Class<? extends CommandExecutor> executorType, Object... extraArguments) {
        this.langService = langService;
        this.name = name;
        this.permission = permission;
        this.isDisabled = isDisabled;
        this.executorType = executorType;
        this.extraArguments = extraArguments.length > 0 ? List.of(extraArguments) : new ArrayList<>();
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

        try {
            var parameters = new ArrayList<>(){{
                add(issuer);
                add(args);
                addAll(extraArguments);
            }};
            Class<?>[] parametersTypes = parameters.stream().map(Object::getClass).toArray(Class<?>[]::new);
            var constructor = executorType.getConstructor(parametersTypes);
            var commandExecutor = constructor.newInstance(parameters);

            if (!commandExecutor.prepare()) {
                return false;
            }

            return commandExecutor.execute();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkPermission(CommandSender issuer) {
        return issuer.hasPermission(this.permission);
    }

    public String getName() {
        return name;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }
}