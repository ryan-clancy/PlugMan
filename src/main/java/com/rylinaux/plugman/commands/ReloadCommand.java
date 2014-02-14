package com.rylinaux.plugman.commands;

import com.rylinaux.plugman.PlugMan;
import com.rylinaux.plugman.utilities.PluginUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Command that reloads plugin(s).
 *
 * @author rylinaux
 */
public class ReloadCommand extends AbstractCommand {

    /**
     * The name of the command.
     */
    public static final String NAME = "Reload";

    /**
     * The description of the command.
     */
    public static final String DESCRIPTION = "Reload a plugin.";

    /**
     * The main permission of the command.
     */
    public static final String PERMISSION = "plugman.reload";

    /**
     * The proper usage of the command.
     */
    public static final String USAGE = "/plugman reload [plugin|all]";

    /**
     * The sub permissions of the command.
     */
    public static final String[] SUB_PERMISSIONS = {"all"};

    /**
     * Construct out object.
     *
     * @param sender the command sender
     */
    public ReloadCommand(CommandSender sender) {
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
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.no-permission"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.specify-plugin"));
            sendUsage();
            return;
        }

        if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("*")) {
            if (hasPermission("all")) {
                PluginUtil.reloadAll();
                sender.sendMessage(PlugMan.getInstance().getMessageManager().format("reload.all"));
            } else {
                sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.no-permission"));
            }
            return;
        }

        Plugin target = PluginUtil.getPluginByName(args, 1);

        if (target == null) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.invalid-plugin"));
            sendUsage();
            return;
        }

        if (PluginUtil.isIgnored(target)) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.ignored"));
            return;
        }

        PluginUtil.reload(target);

        sender.sendMessage(PlugMan.getInstance().getMessageManager().format("reload.reloaded", target.getName()));

    }
}
