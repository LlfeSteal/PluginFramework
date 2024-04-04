package fr.lifesteal.pluginframework.demo;

import fr.lifesteal.pluginframework.api.config.ConfigService;
import fr.lifesteal.pluginframework.core.command.PluginCommand;
import fr.lifesteal.pluginframework.core.plugin.PluginBase;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class TestPlugin extends PluginBase {
    @Override
    public void Init() {

    }

    @Override
    protected List<PluginCommand> registerCommands() {
        return new ArrayList<>() {{
        }};
    }

    @Override
    protected List<ConfigService> registerConfigurationsServices() {
        return new ArrayList<>() {{
        }};
    }

    @Override
    protected List<Listener> registerListeners() {
        return new ArrayList<>() {{
        }};
    }
}
