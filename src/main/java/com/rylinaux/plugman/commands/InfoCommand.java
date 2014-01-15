package com.rylinaux.plugman.commands;

import com.google.common.base.Joiner;

import com.rylinaux.plugman.PlugMan;
import com.rylinaux.plugman.utilities.PluginUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class InfoCommand extends AbstractCommand {

    public static final String DESCRIPTION = "View information on a plugin.";

    public static final String PERMISSION = "plugman.info";

    public static final String USAGE = "/plugman info [plugin]";

    public static final String[] SUB_PERMISSIONS = {""};

    public InfoCommand(CommandSender sender) {
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

        Plugin target = PluginUtils.getPluginByName(args, 1);

        if (target == null) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.invalid-plugin"));
            return;
        }

        String name = target.getName();
        String version = target.getDescription().getVersion();
        String authors = Joiner.on(", ").join(target.getDescription().getAuthors());
        String status = target.isEnabled() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled";

        sender.sendMessage(PlugMan.getInstance().getMessageManager().format("info.header", name));
        sender.sendMessage(PlugMan.getInstance().getMessageManager().format(false, "info.version", version));
        sender.sendMessage(PlugMan.getInstance().getMessageManager().format(false, "info.authors", authors));
        sender.sendMessage(PlugMan.getInstance().getMessageManager().format(false, "info.status", status));

    }
}
