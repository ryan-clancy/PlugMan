package com.rylinaux.plugman.commands;

import com.google.common.base.Joiner;

import com.rylinaux.plugman.PlugMan;
import com.rylinaux.plugman.utilities.PluginUtil;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Command that displays information on a plugin.
 *
 * @author rylinaux
 */
public class InfoCommand extends AbstractCommand {

    /**
     * The name of the command.
     */
    public static final String NAME = "Info";

    /**
     * The description of the command.
     */
    public static final String DESCRIPTION = "View information on a plugin.";

    /**
     * The main permission of the command.
     */
    public static final String PERMISSION = "plugman.info";

    /**
     * The proper usage of the command.
     */
    public static final String USAGE = "/plugman info [plugin]";

    /**
     * The sub permissions of the command.
     */
    public static final String[] SUB_PERMISSIONS = {""};

    /**
     * Construct out object.
     *
     * @param sender the command sender
     */
    public InfoCommand(CommandSender sender) {
        super(sender, NAME, DESCRIPTION, PERMISSION, SUB_PERMISSIONS, USAGE);
    }

    /**
     * Execute the command.
     *
     * @param sender  the sender of the command
     * @param command the command being done
     * @param label   the name of the command
     * @param args    the arguments supplied
     */
    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {

        if (!hasPermission()) {
            sender.sendMessage(PlugMan.getInstance().getMessenger().format("error.no-permission"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(PlugMan.getInstance().getMessenger().format("error.specify-plugin"));
            sendUsage();
            return;
        }

        Plugin target = PluginUtil.getPluginByName(args, 1);

        if (target == null) {
            sender.sendMessage(PlugMan.getInstance().getMessenger().format("error.invalid-plugin"));
            sendUsage();
            return;
        }

        String name = target.getName();
        String version = target.getDescription().getVersion();
        String authors = Joiner.on(", ").join(target.getDescription().getAuthors());
        String status = target.isEnabled() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled";

        sender.sendMessage(PlugMan.getInstance().getMessenger().format("info.header", name));
        sender.sendMessage(PlugMan.getInstance().getMessenger().format(false, "info.version", version));
        sender.sendMessage(PlugMan.getInstance().getMessenger().format(false, "info.authors", authors));
        sender.sendMessage(PlugMan.getInstance().getMessenger().format(false, "info.status", status));

    }
}
