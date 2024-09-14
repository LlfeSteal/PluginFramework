package unit.command;

import fr.lifesteal.pluginframework.api.config.FrameworkLangService;
import fr.lifesteal.pluginframework.core.command.CommandBase;
import fr.lifesteal.pluginframework.core.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import unit.command.utils.FakeCommandExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class CommandBaseTest {
    @Test
    public void getNameShouldReturnName() {
        // Arrange
        var commandName = "CommandName";

        var langService = mock(FrameworkLangService.class);
        var logger = mock(Logger.class);
        var executor = mock(CommandExecutor.class);

        var commandBase = new CommandBase(langService, logger, commandName, "", false, "", executor.getClass());

        // Act
        var result = commandBase.getName();

        // Assert
        assertThat(result).isEqualTo(commandName);
    }

    @Test
    public void setDisabledTrueShouldDisableMethod() {
        // Arrange
        var commandName = "CommandName";

        var langService = mock(FrameworkLangService.class);
        var logger = mock(Logger.class);
        var executor = mock(CommandExecutor.class);

        var commandBase = new CommandBase(langService, logger, commandName, "", false, "", executor.getClass());

        // Act
        commandBase.setDisabled(true);

        // Assert
        assertThat(commandBase.isDisabled()).isTrue();
    }

    @Test
    public void setDisabledFalseShouldDisableMethod() {
        // Arrange
        var commandName = "CommandName";

        var langService = mock(FrameworkLangService.class);
        var logger = mock(Logger.class);
        var executor = mock(CommandExecutor.class);

        var commandBase = new CommandBase(langService, logger, commandName, "", true, "", executor.getClass());

        // Act
        commandBase.setDisabled(false);

        // Assert
        assertThat(commandBase.isDisabled()).isFalse();
    }

    @Test
    public void tabCompleteShouldReturnEmptyList() {
        // Arrange
        var sender = mock(CommandSender.class);
        var args = new String[]{"command1", "param", "input"};
        var commandName = "CommandName";

        var langService = mock(FrameworkLangService.class);
        var logger = mock(Logger.class);
        var executor = mock(CommandExecutor.class);

        var commandBase = new CommandBase(langService, logger, commandName, "", true, "CommandName command1 [param]", executor.getClass());

        // Act
        var result = commandBase.tabComplete(sender, args);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    public void tabCompleteShouldReturnExecutorDefaultTabComplete() {
        // Arrange
        var sender = mock(CommandSender.class);

        var args = new String[]{"command1", "input"};
        var commandName = "CommandName";

        var expectedResult = List.of("param");

        var langService = mock(FrameworkLangService.class);
        var logger = mock(Logger.class);

        var executor = mock(CommandExecutor.class);

        var commandBase = new CommandBase(langService, logger, commandName, "", true, "CommandName command1 [param]", executor.getClass());

        // Act
        var result = commandBase.tabComplete(sender, args);

        // Assert
        assertThat(result).containsExactlyElementsOf(expectedResult);
    }

    @Test
    public void tabCompleteShouldReturnExecutorTabComplete() {
        // Arrange
        var sender = mock(CommandSender.class);
        var args = new String[]{"command1", "input"};
        var commandName = "CommandName";
        var expectedResult = new ArrayList<String>(){{
           add("Result1");
           add("Result2");
           add("Result3");
        }};

        var langService = mock(FrameworkLangService.class);
        var logger = mock(Logger.class);

        var commandBase = new CommandBase(langService, logger, commandName, "", true, "CommandName command1 [param]", FakeCommandExecutor.class, false, false, expectedResult);

        // Act
        var result = commandBase.tabComplete(sender, args);

        // Assert
        assertThat(result).containsExactlyElementsOf(expectedResult);
    }

    @Test
    public void executeShouldSendDisabledErrorMessageAndReturnFalse() {
        // Arrange
        var commandIssuer = mock(CommandSender.class);

        var args = new String[]{"command1", "input"};
        var commandName = "CommandName";

        var langService = mock(FrameworkLangService.class);
        when(langService.getCommandDisabledErrorMessage()).thenReturn("Command is disabled !");

        var logger = mock(Logger.class);

        var executor = mock(CommandExecutor.class);

        var commandBase = new CommandBase(langService, logger, commandName, "", true, "CommandName command1 [param]", executor.getClass());

        // Act
        boolean result = commandBase.execute(commandIssuer, args);

        // Assert
        verify(commandIssuer, times(1)).sendMessage("Command is disabled !");
        assertThat(result).isFalse();
    }

    @Test
    public void executeShouldSendPermissionErrorMessageAndReturnFalse() {
        // Arrange
        var permission = "ma.permission";

        var commandIssuer = mock(CommandSender.class);
        when(commandIssuer.hasPermission(permission)).thenReturn(false);

        var args = new String[]{"command1", "input"};
        var commandName = "CommandName";

        var langService = mock(FrameworkLangService.class);
        when(langService.getCommandNoPermissionErrorMessage()).thenReturn("You don't have permission to use this command !");

        var logger = mock(Logger.class);

        var executor = mock(CommandExecutor.class);

        var commandBase = new CommandBase(langService, logger, commandName, permission, false, "CommandName command1 [param]", executor.getClass());

        // Act
        boolean result = commandBase.execute(commandIssuer, args);

        // Assert
        verify(commandIssuer, times(1)).sendMessage("You don't have permission to use this command !");
        assertThat(result).isFalse();
    }

    @Test
    public void executeShouldSendUsageMessageAndReturnFalse() {
        // Arrange
        var permission = "ma.permission";
        var usage = "CommandName command1 [param] <optionalParam>";

        var commandIssuer = mock(CommandSender.class);
        when(commandIssuer.hasPermission(permission)).thenReturn(true);

        var args = new String[]{"command1"};
        var commandName = "CommandName";

        var langService = mock(FrameworkLangService.class);
        when(langService.getCommandUsageMessage(usage)).thenReturn("Command usage : ..... !");

        var logger = mock(Logger.class);

        var executor = mock(CommandExecutor.class);

        var commandBase = new CommandBase(langService, logger, commandName, permission, false, usage, executor.getClass());

        // Act
        boolean result = commandBase.execute(commandIssuer, args);

        // Assert
        verify(commandIssuer, times(1)).sendMessage("Command usage : ..... !");
        assertThat(result).isFalse();
    }

    @Test
    public void executeShouldCallExecutorPrepareAndReturnFalse() {
        // Arrange
        var permission = "ma.permission";
        var usage = "CommandName command1 [param] <optionalParam>";

        var commandIssuer = mock(CommandSender.class);
        when(commandIssuer.hasPermission(permission)).thenReturn(true);

        var args = new String[]{"command1"};
        var commandName = "CommandName";

        var langService = mock(FrameworkLangService.class);

        var logger = mock(Logger.class);


        var commandBase = new CommandBase(langService, logger, commandName, permission, false, usage, FakeCommandExecutor.class, true, false, List.of());

        // Act
        boolean result = commandBase.execute(commandIssuer, args);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void executeShouldCallExecutorAndReturnTrue() {
        // Arrange
        var permission = "ma.permission";
        var usage = "CommandName command1 [param] <optionalParam>";

        var commandIssuer = mock(CommandSender.class);
        when(commandIssuer.hasPermission(permission)).thenReturn(true);

        var args = new String[]{"command1", "param"};
        var commandName = "CommandName";

        var langService = mock(FrameworkLangService.class);

        var logger = mock(Logger.class);


        var commandBase = new CommandBase(langService, logger, commandName, permission, false, usage, FakeCommandExecutor.class, true, true, List.of());

        // Act
        boolean result = commandBase.execute(commandIssuer, args);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void invalidExtraArgumentForExecutorShouldDisabledPluginAndLogError() {
        // Arrange
        var commandName = "CommandName";

        var langService = mock(FrameworkLangService.class);

        var logger = mock(Logger.class);

        // Act
        var commandBase = new CommandBase(langService, logger, commandName, "", false, "", FakeCommandExecutor.class, true, List.of(), true);

        // Assert
        assertThat(commandBase.isDisabled()).isTrue();
        verify(logger, times(1)).severe("No executor constructor found for CommandName. Command has been disabled.");
    }

    @Test
    public void tabCompleteWithExceptionOnExecutorInstanciationShouldSendErrorMessageToSenderAndThrowRuntimeException() {
        // Arrange
        var sender = mock(CommandSender.class);
        var args = new String[]{"command1", "input"};
        var commandName = "CommandName";

        var exception = new IllegalAccessException();

        var langService = mock(FrameworkLangService.class);
        var logger = mock(Logger.class);

        var commandBase = new CommandBase(langService, logger, commandName, "", true, "CommandName command1 [param]", FakeCommandExecutor.class, exception);

        // Act
        Executable methodeCall = () -> commandBase.tabComplete(sender, args);

        // Assert
        assertThrows(RuntimeException.class, methodeCall);
    }
}
