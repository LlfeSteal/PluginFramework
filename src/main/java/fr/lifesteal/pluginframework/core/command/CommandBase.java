package fr.lifesteal.pluginframework.core.command;

import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;

public abstract class CommandBase {
    private final String name;
    private final String permission;
    private final Class<? extends CommandExecutor> executorType;
    private boolean isDisabled;

    public CommandBase(String name, String permission, boolean isDisabled, Class<? extends CommandExecutor> executorType) {
        this.name = name;
        this.permission = permission;
        this.isDisabled = isDisabled;
        this.executorType = executorType;
    }

    public boolean execute(CommandSender issuer, String[] args) {

        if (this.isDisabled) {
            // Todo: Send message
            return false;
        }

        if (!checkPermission(issuer)) {
            // Todo: Send message
            return false;
        }

        try {
            var constructor = executorType.getConstructor(CommandSender.class, String[].class);
            var commandExecutor = constructor.newInstance(issuer, args);
            if (!commandExecutor.prepare()) {
                // Todo: Send usage
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

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }
}