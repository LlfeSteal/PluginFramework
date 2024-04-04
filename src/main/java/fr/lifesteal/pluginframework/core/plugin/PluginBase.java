package fr.lifesteal.pluginframework.core.plugin;

import fr.lifesteal.pluginframework.api.config.ConfigService;
import fr.lifesteal.pluginframework.core.command.PluginCommand;
import fr.lifesteal.pluginframework.core.config.factory.ConfigRepositoryFactory;
import fr.lifesteal.pluginframework.core.config.framework.FrameworkLangService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.List;

public abstract class PluginBase extends JavaPlugin {
    private final String PluginFolder = getDataFolder().toString();
    private final ConfigRepositoryFactory  ConfigRepositoryFactory = new ConfigRepositoryFactory(PluginFolder);
    private List<PluginCommand> commands;
    private List<ConfigService> configurationsServices;

    @Override
    public void onEnable() {
        initPluginFields();
        initConfiguration();
        registerBukkitCommand();
    }

    public abstract void Init();

    public ConfigRepositoryFactory getConfigRepositoryFactory() {
        return ConfigRepositoryFactory;
    }

    protected abstract List<PluginCommand> registerCommands();
    protected abstract List<ConfigService> registerConfigurationsServices();

    private void initPluginFields() {
        commands = this.registerCommands();
        configurationsServices = this.registerConfigurationsServices();
        configurationsServices.add(new FrameworkLangService(ConfigRepositoryFactory.getNewYamlConfigFactory("framework", "framework-lang.yml")));
    }

    /**
     * Méthode used to register plugin commands.
     */
    private void registerBukkitCommand() {
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
