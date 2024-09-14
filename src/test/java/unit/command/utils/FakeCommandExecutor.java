package unit.command.utils;

import fr.lifesteal.pluginframework.core.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

public class FakeCommandExecutor extends CommandExecutor {

    private final boolean prepareResult;
    private final boolean executeResult;
    private final List<String> tabResult;

    public FakeCommandExecutor(CommandSender issuer, Map<String, String> namedArgs, Boolean prepareResult, Boolean executeResult, List<String> tabResult) {
        super(issuer, namedArgs);
        this.prepareResult = prepareResult;
        this.executeResult = executeResult;
        this.tabResult = tabResult;
    }

    public FakeCommandExecutor(CommandSender issuer, Map<String, String> namedArgs) {
        super(issuer, namedArgs);
        this.prepareResult = false;
        this.executeResult = false;
        this.tabResult = List.of();
    }

    public FakeCommandExecutor(CommandSender issuer, Map<String, String> namedArgs, Throwable exception) throws Throwable {
        super(issuer, namedArgs);
        throw exception;
    }


    @Override
    public boolean prepare() {
        return prepareResult;
    }

    @Override
    public boolean execute() {
        return executeResult;
    }

    @Override
    public List<String> getTabSuggestion(String paramName) {
        return tabResult;
    }
}
