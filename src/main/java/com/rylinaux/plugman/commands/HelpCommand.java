package com.rylinaux.plugman.commands;

import com.rylinaux.plugman.contructs.SimpleCommand;
import com.rylinaux.plugman.contructs.SimpleCommandExecutor;
import com.rylinaux.plugman.utilities.Messaging;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class HelpCommand extends SimpleCommand implements SimpleCommandExecutor {

    public HelpCommand(JavaPlugin plugin, CommandSender sender, Command command, String label, String[] args, String permission) {
        super(plugin, sender, command, label, args, permission);
    }

    public void execute() {

        if (!hasPermission()) {
            sender.sendMessage(Messaging.NO_PERMISSION);
            return;
        }

        StringBuilder builder = new StringBuilder();

        builder.append(Messaging.HELP_HEADER + "\n");

        if (sender.hasPermission("plugman.list")) {
            builder.append(Messaging.HELP_LIST + "\n");
        }
        if (sender.hasPermission("plugman.info")) {
            builder.append(Messaging.HELP_INFO + "\n");
        }
        if (sender.hasPermission("plugman.usage")) {
            builder.append(Messaging.HELP_USAGE + "\n");
        }
        if (sender.hasPermission("plugman.status")) {
            builder.append(Messaging.HELP_STATUS + "\n");
        }
        if (sender.hasPermission("plugman.load")) {
            builder.append(Messaging.HELP_LOAD + "\n");
        }
        if (sender.hasPermission("plugman.unload")) {
            builder.append(Messaging.HELP_UNLOAD + "\n");
        }
        if (sender.hasPermission("plugman.reload")) {
            builder.append(Messaging.HELP_RELOAD + "\n");
        }
        if (sender.hasPermission("plugman.enable")) {
            builder.append(Messaging.HELP_ENABLE + "\n");
        }
        if (sender.hasPermission("plugman.disable")) {
            builder.append(Messaging.HELP_DISABLE);
        }

        sender.sendMessage(builder.toString());

    }

}