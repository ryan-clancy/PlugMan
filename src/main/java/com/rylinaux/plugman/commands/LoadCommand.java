package com.rylinaux.plugman.commands;

import com.rylinaux.plugman.PlugMan;
import com.rylinaux.plugman.utilities.PluginUtils;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class LoadCommand extends AbstractCommand {

    public static final String DESCRIPTION = "Load a plugin.";

    public static final String PERMISSION = "plugman.load";

    public static final String USAGE = "/plugman enable [plugin]";

    public static final String[] SUB_PERMISSIONS = {""};

    public LoadCommand(CommandSender sender) {
        super(sender, DESCRIPTION, PERMISSION, SUB_PERMISSIONS, USAGE);
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {

        if (!hasPermission()) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.no-permission"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.specify-plugin"));
            return;
        }

        Plugin potential = PluginUtils.getPluginByName(args, 1);

        if (potential != null) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("load.already-loaded", potential.getName()));
            return;
        }

        sender.sendMessage(PluginUtils.load(args));

    }
}
