package com.rylinaux.plugman.commands;

import com.rylinaux.plugman.PlugMan;
import com.rylinaux.plugman.utilities.PluginUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Command that enables plugin(s).
 *
 * @author rylinaux
 */
public class EnableCommand extends AbstractCommand {

    /**
     * The name of the command.
     */
    public static final String NAME = "Enable";

    /**
     * The description of the command.
     */
    public static final String DESCRIPTION = "Enable a plugin.";

    /**
     * The main permission of the command.
     */
    public static final String PERMISSION = "plugman.enable";

    /**
     * The proper usage of the command.
     */
    public static final String USAGE = "/plugman enable [plugin|all]";

    /**
     * The sub permissions of the command.
     */
    public static final String[] SUB_PERMISSIONS = {"all"};

    /**
     * Construct out object.
     *
     * @param sender the command sender
     */
    public EnableCommand(CommandSender sender) {
        super(sender, NAME, DESCRIPTION, PERMISSION, SUB_PERMISSIONS, USAGE);
    }

    /**
     * Execute the command
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

        if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("*")) {
            if (hasPermission("all")) {
                PluginUtil.enableAll();
                sender.sendMessage(PlugMan.getInstance().getMessenger().format("enable.all"));
            } else {
                sender.sendMessage(PlugMan.getInstance().getMessenger().format("error.no-permission"));
            }
            return;
        }

        Plugin target = PluginUtil.getPluginByName(args, 1);

        if (target == null) {
            sender.sendMessage(PlugMan.getInstance().getMessenger().format("error.invalid-plugin"));
            sendUsage();
            return;
        }

        if (PluginUtil.isIgnored(target)) {
            sender.sendMessage(PlugMan.getInstance().getMessenger().format("error.ignored"));
            return;
        }

        if (target.isEnabled()) {
            sender.sendMessage(PlugMan.getInstance().getMessenger().format("enable.already-enabled", target.getName()));
            return;
        }

        PluginUtil.enable(target);

        sender.sendMessage(PlugMan.getInstance().getMessenger().format("enable.enabled", target.getName()));

    }

}