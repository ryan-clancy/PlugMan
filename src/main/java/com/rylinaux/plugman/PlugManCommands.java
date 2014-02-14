package com.rylinaux.plugman;

import com.rylinaux.plugman.commands.DisableCommand;
import com.rylinaux.plugman.commands.EnableCommand;
import com.rylinaux.plugman.commands.HelpCommand;
import com.rylinaux.plugman.commands.InfoCommand;
import com.rylinaux.plugman.commands.ListCommand;
import com.rylinaux.plugman.commands.LoadCommand;
import com.rylinaux.plugman.commands.ReloadCommand;
import com.rylinaux.plugman.commands.RestartCommand;
import com.rylinaux.plugman.commands.UnloadCommand;
import com.rylinaux.plugman.commands.UsageCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Listen for commands and execute them.
 *
 * @author rylinaux
 */
public class PlugManCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            new HelpCommand(sender).execute(sender, command, label, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("disable")) {
            new DisableCommand(sender).execute(sender, command, label, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("enable")) {
            new EnableCommand(sender).execute(sender, command, label, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {
            new InfoCommand(sender).execute(sender, command, label, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            new ListCommand(sender).execute(sender, command, label, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("load")) {
            new LoadCommand(sender).execute(sender, command, label, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("restart")) {
            new RestartCommand(sender).execute(sender, command, label, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            new ReloadCommand(sender).execute(sender, command, label, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("unload")) {
            new UnloadCommand(sender).execute(sender, command, label, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("usage")) {
            new UsageCommand(sender).execute(sender, command, label, args);
            return true;
        }

        new HelpCommand(sender).execute(sender, command, label, args);

        return true;
    }

}
