package fr.lifesteal.pluginframework.core.plugin;

import fr.lifesteal.pluginframework.api.config.ConfigService;
import fr.lifesteal.pluginframework.core.command.PluginCommand;
import fr.lifesteal.pluginframework.core.command.factory.CommandFactory;
import fr.lifesteal.pluginframework.core.config.factory.ConfigRepositoryFactory;
import fr.lifesteal.pluginframework.core.config.framework.FrameworkLangService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class PluginBase extends JavaPlugin {
    private final String PluginFolder = getDataFolder().toString();
    private final ConfigRepositoryFactory  ConfigRepositoryFactory = new ConfigRepositoryFactory(PluginFolder);
    private final fr.lifesteal.pluginframework.api.config.FrameworkLangService langService = new FrameworkLangService(ConfigRepositoryFactory.getNewYamlConfigFactory("framework", "framework-lang.yml"));
    private final CommandFactory commandFactory = new CommandFactory(langService);
    private List<PluginCommand> commands = new ArrayList<>();
    private List<ConfigService> configurationsServices = new ArrayList<>();
    private List<Listener> listeners = new ArrayList<>();

    @Override
    public void onEnable() {
        initPluginFields();
        initConfigurationServices();
        registerBukkitCommand();
        registerBukkitListeners();
    }

    public abstract void Init();

    public ConfigRepositoryFactory getConfigRepositoryFactory() {
        return ConfigRepositoryFactory;
    }

    public CommandFactory getCommandFactory() {
        return commandFactory;
    }

    protected abstract List<PluginCommand> registerCommands();
    protected abstract List<ConfigService> registerConfigurationsServices();
    protected abstract List<Listener> registerListeners();

    private void initPluginFields() {
        commands = this.registerCommands();
        configurationsServices = this.registerConfigurationsServices();
        configurationsServices.add(this.langService);
        listeners = registerListeners();
    }

    /**
     * Method used to init plugin's configuration services.
     */
    private void initConfigurationServices() {
        for (var configurationService : configurationsServices) {
            configurationService.initConfig();
        }
    }

    /**
     * Method used to register plugin's commands.
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

    /**
     * Method used to register plugin's listeners.
     */
    private void registerBukkitListeners() {
        for (var listener : this.listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
