package com.ryanclancy000.plugman;

import com.ryanclancy000.plugman.utilities.Utilities;
import java.util.logging.Level;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PlugManCommands implements CommandExecutor {

    private final PlugMan plugin;

    public PlugManCommands(PlugMan instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            plugin.getUtils().thisInfo(sender);
        } else if ("help".equalsIgnoreCase(args[0])) {
            if (sender.hasPermission("plugman.help")) {
                plugin.getUtils().helpCommand(sender);
            } else {
                plugin.getUtils().noPerms(sender);
            }
        } else if ("list".equalsIgnoreCase(args[0])) {
            if (sender.hasPermission("plugman.list")) {
                plugin.getUtils().listCommand(sender, args);
            } else {
                plugin.getUtils().noPerms(sender);
            }
        } else if ("vlist".equalsIgnoreCase(args[0])) {
            if (sender.hasPermission("plugman.vlist")) {
                plugin.getUtils().vlistCommand(sender, args);
            } else {
                plugin.getUtils().noPerms(sender);
            }
        } else if ("info".equalsIgnoreCase(args[0])) {
            if (sender.hasPermission("plugman.info")) {
                plugin.getUtils().infoCommand(sender, args);
            } else {
                plugin.getUtils().noPerms(sender);
            }
        } else if ("status".equalsIgnoreCase(args[0])) {
            if (sender.hasPermission("plugman.status")) {
                plugin.getUtils().statusCommand(sender, args);
            } else {
                plugin.getUtils().noPerms(sender);
            }
        } else if ("usage".equalsIgnoreCase(args[0])) {
            if (sender.hasPermission("plugman.usage")) {
                plugin.getUtils().usageCommand(sender, args);
            } else {
                plugin.getUtils().noPerms(sender);
            }
        } else if ("test".equalsIgnoreCase(args[0])) {
            if (sender.hasPermission("plugman.test")) {
                plugin.getUtils().testCommand(sender, args);
            } else {
                plugin.getUtils().noPerms(sender);
            }
        } else if ("load".equalsIgnoreCase(args[0])) {
            if (sender.hasPermission("plugman.load")) {
                plugin.getUtils().loadCommand(sender, args);
            } else {
                plugin.getUtils().noPerms(sender);
            }
        } else if ("unload".equalsIgnoreCase(args[0])) {
            if (sender.hasPermission("plugman.unload")) {
                plugin.getUtils().unloadCommand(sender, args);
            } else {
                plugin.getUtils().noPerms(sender);
            }
        } else if ("restart".equalsIgnoreCase(args[0])) {
            if (sender.hasPermission("plugman.restart")) {
                plugin.getUtils().restartCommand(sender, args);
            } else {
                plugin.getUtils().noPerms(sender);
            }
        } else if ("reload".equalsIgnoreCase(args[0])) {
            if (sender.hasPermission("plugman.reload")) {
                plugin.getUtils().reloadCommand(sender, args);
            } else {
                plugin.getUtils().noPerms(sender);
            }
        } else if ("enable".equalsIgnoreCase(args[0])) {
            if (sender.hasPermission("plugman.enable")) {
                plugin.getUtils().enableCommand(sender, args);
            } else {
                plugin.getUtils().noPerms(sender);
            }
        } else if ("disable".equalsIgnoreCase(args[0])) {
            if (sender.hasPermission("plugman.disable")) {
                plugin.getUtils().disableCommand(sender, args);
            } else {
                plugin.getUtils().noPerms(sender);
            }
        } else {
            plugin.getUtils().helpCommand(sender);
        }
        return true;
    }
}
