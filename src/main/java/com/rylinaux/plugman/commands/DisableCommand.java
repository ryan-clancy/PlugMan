package com.rylinaux.plugman.commands;

import com.rylinaux.plugman.contructs.SimpleCommand;
import com.rylinaux.plugman.contructs.SimpleCommandExecutor;
import com.rylinaux.plugman.utilities.Messaging;
import com.rylinaux.plugman.utilities.Utilities;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class DisableCommand extends SimpleCommand implements SimpleCommandExecutor {

    public DisableCommand(JavaPlugin plugin, CommandSender sender, Command command, String label, String[] args, String permission) {
        super(plugin, sender, command, label, args, permission);
    }

    public void execute() {

        if (!hasPermission()) {
            sender.sendMessage(Messaging.NO_PERMISSION);
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(Messaging.MUST_SPECIFY);
            return;
        }

        Plugin target = Utilities.getPluginByName(args, 1);

        if (target == null) {
            sender.sendMessage(String.format(Messaging.NO_EXIST));
            return;
        }

        if (!target.isEnabled()) {
            sender.sendMessage(String.format(Messaging.ALREADY_DISABLED, target.getName()));
            return;
        }

        Utilities.disable(target);

        sender.sendMessage(String.format(Messaging.DISABLED, target.getName()));
    }


}
