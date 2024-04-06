package fr.lifesteal.pluginframework.demo;

import fr.lifesteal.pluginframework.api.config.ConfigService;
import fr.lifesteal.pluginframework.core.command.PluginCommand;
import fr.lifesteal.pluginframework.core.plugin.PluginBase;
import fr.lifesteal.pluginframework.demo.business.DemoLangService;
import fr.lifesteal.pluginframework.demo.command.PingCommand;
import fr.lifesteal.pluginframework.demo.command.WikiCommand;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class TestPlugin extends PluginBase {
    private DemoLangService demoLangService;

    @Override
    public void init() {
        var demoConfigRepository = getConfigRepositoryFactory().getNewYamlConfigFactory("demo", "lang.yml");
        demoLangService = new DemoLangService(getLogger(), demoConfigRepository);
    }

    @Override
    protected List<PluginCommand> registerCommands() {
        return new ArrayList<>() {{
            add(getPluginCommandFactory()
                    .setName("ping")
                    .setDescription("Méthode de ping.")
                    .addAlias("png")
                    .setDefaultCommand(getCommandBaseBuilder()
                            .setPermission("demo.ping")
                            .setExecutorType(PingCommand.class)
                            .addExtraArgument(demoLangService)
                            .build())
                    .build());
            add(getPluginCommandFactory()
                    .setName("wiki")
                    .setDescription("Affichage du wiki.")
                    .addAlias("help")
                    .setDefaultCommand(getCommandBaseBuilder()
                            .setPermission("demo.wiki")
                            .setExecutorType(WikiCommand.class)
                            .addExtraArgument(demoLangService)
                            .build())
                    .build());
        }};
    }

    @Override
    protected List<ConfigService> registerConfigurationsServices() {
        return new ArrayList<>() {{
            add(demoLangService);
        }};
    }

    @Override
    protected List<Listener> registerListeners() {
        return new ArrayList<>() {{
        }};
    }
}
