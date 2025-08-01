package fr.lifesteal.pluginframework.core.plugin;

import fr.lifesteal.pluginframework.api.config.ConfigService;
import fr.lifesteal.pluginframework.core.business.FrameworkLangService;
import fr.lifesteal.pluginframework.core.command.builder.CommandBuilder;
import fr.lifesteal.pluginframework.core.command.builder.PluginCommandBuilder;
import fr.lifesteal.pluginframework.core.config.factory.ConfigRepositoryFactory;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class PluginBase extends JavaPlugin {
    private final String PluginFolder = getDataFolder().getPath();
    private ConfigRepositoryFactory  configRepositoryFactory;
    private fr.lifesteal.pluginframework.api.config.FrameworkLangService langService;
    private List<Command> commands = new ArrayList<>();
    private List<ConfigService> configurationsServices = new ArrayList<>();
    private List<Listener> listeners = new ArrayList<>();

    @Override
    public void onEnable() {
        initPluginFactories();
        init();
        initPluginFields();
        initConfigurationServices();
        registerBukkitCommand();
        registerBukkitListeners();
        postInit();
    }

    public abstract void init();

    public abstract void postInit();

    /**
     * This method is part of the public API and is intended to be used by
     * consumers of the framework, even if not referenced internally.
     */
    @SuppressWarnings("unused")
    public ConfigRepositoryFactory getConfigRepositoryFactory() {
        return configRepositoryFactory;
    }

    /**
     * This method is part of the public API and is intended to be used by
     * consumers of the framework, even if not referenced internally.
     */
    @SuppressWarnings("unused")
    public CommandBuilder getCommandBaseBuilder() {
        return new CommandBuilder(this.langService, getLogger());
    }

    /**
     * This method is part of the public API and is intended to be used by
     * consumers of the framework, even if not referenced internally.
     */
    @SuppressWarnings("unused")
    public PluginCommandBuilder getPluginCommandFactory() {
        return new PluginCommandBuilder();
    }

    protected abstract List<Command> registerCommands();
    protected abstract List<ConfigService> registerConfigurationsServices();
    protected abstract List<Listener> registerListeners();

    private void initPluginFactories() {
        var dataFolder = getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdir()) {
            getLogger().severe("Unable to create plugin folder: " + dataFolder.getAbsolutePath());
        }

        configRepositoryFactory = new ConfigRepositoryFactory(getLogger(), PluginFolder);
    }

    private void initPluginFields() {
        this.langService = new FrameworkLangService(getLogger(), configRepositoryFactory.getNewYamlRepository("framework", "framework-lang.yml"));

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
            getLogger().info("Loading config file : " + configurationService.getClass().getName());
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
            getLogger().severe("Unable to register bukkit commands." + e.getMessage());
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
