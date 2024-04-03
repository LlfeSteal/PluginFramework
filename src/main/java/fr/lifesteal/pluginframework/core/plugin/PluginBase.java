package fr.lifesteal.pluginframework.core.plugin;

import fr.lifesteal.pluginframework.core.command.PluginCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.List;

public class PluginBase extends JavaPlugin {
    private final List<PluginCommand> commands;

    public PluginBase(List<PluginCommand> commands) {
        this.commands = commands;
    }

    @Override
    public void onEnable() {
        registerCommands();
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
}
