package unit.command;

import com.google.common.collect.ImmutableList;
import fr.lifesteal.pluginframework.core.command.CommandBase;
import fr.lifesteal.pluginframework.core.command.PluginCommand;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class PluginCommandTest {
    @Test
    public void executeShouldCallSubCommandExecute() {
        // Arrange
        var commandSender = mock(CommandSender.class);
        var args = new String[0];

        var defaultCommand = mock(CommandBase.class);
        when(defaultCommand.execute(commandSender, args)).thenReturn(true);

        var pluginCommand = new PluginCommand(
                "commandName",
                "",
                "",
                ImmutableList.of(),
                defaultCommand,
                ImmutableList.of());
        // Act
        boolean result = pluginCommand.execute(commandSender, "", args);

        // Assert
        assertThat(result).isTrue();
        verify(defaultCommand, times(1)).execute(commandSender, args);
    }

    @Test
    public void tabCompleteShouldReturnSubCommands() {
        // Arrange
        var commandSender = mock(CommandSender.class);
        var defaultCommand = mock(CommandBase.class);

        var subCommand1 = mock(CommandBase.class);
        when(subCommand1.getName()).thenReturn("Command1");

        var subCommand2 = mock(CommandBase.class);
        when(subCommand2.getName()).thenReturn("Command2");

        var subCommand3 = mock(CommandBase.class);
        when(subCommand3.getName()).thenReturn("Command3");

        var subCommands = new ArrayList<CommandBase>() {{
            add(subCommand1);
            add(subCommand2);
            add(subCommand3);
        }};

        var pluginCommand = new PluginCommand(
                "commandName",
                "",
                "",
                ImmutableList.of(),
                defaultCommand,
                subCommands);
        // Act
        var result = pluginCommand.tabComplete(commandSender, "", new String[0]);

        // Assert
        var expectedResult = List.of("Command1", "Command2", "Command3");
        assertThat(result).containsExactlyElementsOf(expectedResult);

        verify(subCommand1, times(1)).getName();
        verify(subCommand2,  times(1)).getName();
        verify(subCommand2, times(1)).getName();
    }

    @Test
    public void tabCompleteShouldReturnSubCommandTabComplete() {
        // Arrange
        var expectedResult = List.of("TAB1", "TAB2");
        var commandSender = mock(CommandSender.class);
        var args = new String[]{"Command1", ""};

        var subCommand1 = mock(CommandBase.class);
        when(subCommand1.getName()).thenReturn("Command1");
        when(subCommand1.tabComplete(commandSender, args)).thenReturn(expectedResult);

        var subCommand2 = mock(CommandBase.class);
        when(subCommand2.getName()).thenReturn("Command2");

        var subCommand3 = mock(CommandBase.class);
        when(subCommand3.getName()).thenReturn("Command3");

        var subCommands = new ArrayList<CommandBase>() {{
            add(subCommand1);
            add(subCommand2);
            add(subCommand3);
        }};

        var defaultCommand = mock(CommandBase.class);

        var pluginCommand = new PluginCommand(
                "commandName",
                "",
                "",
                ImmutableList.of(),
                defaultCommand,
                subCommands);
        // Act
        var result = pluginCommand.tabComplete(commandSender, "", args);

        // Assert
        assertThat(result).containsExactlyElementsOf(expectedResult);
        verify(subCommand1, times(1)).tabComplete(commandSender, args);
    }

    @Test
    public void tabCompleteShouldReturnDefaultTabComplete() {
        // Arrange
        var commandSender = mock(CommandSender.class);
        var args = new String[]{"anotherCommand", ""};

        var subCommand1 = mock(CommandBase.class);
        when(subCommand1.getName()).thenReturn("Command1");

        var subCommand2 = mock(CommandBase.class);
        when(subCommand2.getName()).thenReturn("Command2");

        var subCommand3 = mock(CommandBase.class);
        when(subCommand3.getName()).thenReturn("Command3");

        var subCommands = new ArrayList<CommandBase>() {{
            add(subCommand1);
            add(subCommand2);
            add(subCommand3);
        }};

        var defaultCommand = mock(CommandBase.class);

        var pluginCommand = new PluginCommand(
                "commandName",
                "",
                "",
                ImmutableList.of(),
                defaultCommand,
                subCommands);
        // Act
        var result = pluginCommand.tabComplete(commandSender, "", args);

        // Assert
        assertThat(result).isEmpty();
    }
}
