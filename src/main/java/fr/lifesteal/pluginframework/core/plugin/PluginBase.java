package fr.lifesteal.pluginframework.core.plugin;

import fr.lifesteal.pluginframework.api.config.ConfigService;
import fr.lifesteal.pluginframework.core.command.PluginCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.List;

public abstract class PluginBase extends JavaPlugin {
    private List<PluginCommand> commands;
    private List<ConfigService> configurationsServices;

    @Override
    public void onEnable() {
        initConfiguration();
        registerCommands();
    }

    public abstract void Init();

    protected void setCommands(List<PluginCommand> commands) {
        this.commands = commands;
    }

    protected void setConfigurationsServices(List<ConfigService> configurationsServices) {
        this.configurationsServices = configurationsServices;
    }

    /**
     * Méthode used to register plugin commands.
     */
    private void registerCommands() {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);

            for (Command command : this.commands) {
                CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
                commandMap.register(command.getName(), command);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initConfiguration() {
        for (var configurationService : configurationsServices) {
            configurationService.initConfig();
        }
    }
}
