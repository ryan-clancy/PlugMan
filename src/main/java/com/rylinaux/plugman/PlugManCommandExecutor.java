package com.rylinaux.plugman;

import com.rylinaux.plugman.commands.DisableCommand;
import com.rylinaux.plugman.commands.EnableCommand;
import com.rylinaux.plugman.commands.HelpCommand;
import com.rylinaux.plugman.commands.ListCommand;
import com.rylinaux.plugman.commands.ReloadCommand;
import com.rylinaux.plugman.utilities.Permissions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PlugManCommandExecutor implements CommandExecutor {

    private final PlugMan plugin;

    public PlugManCommandExecutor(PlugMan plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String command;

        if (args.length == 0) {
            new HelpCommand(plugin, sender, cmd, label, args, Permissions.HELP).execute();
            return true;
        } else {
            command = args[0].toLowerCase();
        }

        switch (command) {
            case "enable":
                new EnableCommand(plugin, sender, cmd, label, args, Permissions.ENABLE).execute();
                break;
            case "disable":
                new DisableCommand(plugin, sender, cmd, label, args, Permissions.DISABLE).execute();
                break;
            case "list":
                new ListCommand(plugin, sender, cmd, label, args, Permissions.LIST).execute();
                break;
            case "reload":
                new ReloadCommand(plugin, sender, cmd, label, args, Permissions.RELOAD).execute();
                break;
            case "help":
                new HelpCommand(plugin, sender, cmd, label, args, Permissions.HELP).execute();
                break;
            default:
                new HelpCommand(plugin, sender, cmd, label, args, Permissions.HELP).execute();
                break;
        }

        return true;
    }

}