package com.rylinaux.plugman.commands;

import com.rylinaux.plugman.PlugMan;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class HelpCommand extends AbstractCommand {

    public static final String DESCRIPTION = "Displays help information.";

    public static final String PERMISSION = "plugman.help";

    public static final String USAGE = "/plugman help";

    public static final String[] SUB_PERMISSIONS = {""};

    public HelpCommand(CommandSender sender) {
        super(sender, DESCRIPTION, PERMISSION, SUB_PERMISSIONS, USAGE);
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {

        if (!hasPermission()) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.no-permission"));
            return;
        }

        ConfigurationSection help = PlugMan.getInstance().getMessageManager().getMessaging().getConfig().getConfigurationSection("help");

        for (String s : help.getKeys(false)) {
            if (sender.hasPermission("plugman." + s) || s.equalsIgnoreCase("header"))
                sender.sendMessage(PlugMan.getInstance().getMessageManager().format(false, help.getName() + "." + s));
        }

    }
}
