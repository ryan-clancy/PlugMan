package com.rylinaux.plugman.commands;

import com.rylinaux.plugman.contructs.SimpleCommand;
import com.rylinaux.plugman.contructs.SimpleCommandExecutor;
import com.rylinaux.plugman.utilities.Messaging;
import com.rylinaux.plugman.utilities.Utilities;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadCommand extends SimpleCommand implements SimpleCommandExecutor {

    public ReloadCommand(JavaPlugin plugin, CommandSender sender, Command command, String label, String[] args, String permission) {
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

        PluginManager pluginManager = plugin.getServer().getPluginManager();

        pluginManager.disablePlugin(target);
        pluginManager.enablePlugin(target);

        sender.sendMessage(String.format(Messaging.RELOADED, target.getName()));

    }

}
