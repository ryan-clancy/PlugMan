package com.rylinaux.plugman.commands;

import com.rylinaux.plugman.PlugMan;
import com.rylinaux.plugman.utilities.PluginUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Command that lists a plugin's commands.
 *
 * @author rylinaux
 */
public class UsageCommand extends AbstractCommand {

    /**
     * The name of the command.
     */
    public static final String NAME = "Usage";

    /**
     * The description of the command.
     */
    public static final String DESCRIPTION = "List commands a plugin has registered.";

    /**
     * The main permission of the command.
     */
    public static final String PERMISSION = "plugman.usage";

    /**
     * The proper usage of the command.
     */
    public static final String USAGE = "/plugman usage [plugin]";

    /**
     * The sub permissions of the command.
     */
    public static final String[] SUB_PERMISSIONS = {""};

    /**
     * Construct out object.
     *
     * @param sender the command sender
     */
    public UsageCommand(CommandSender sender) {
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

        Plugin target = PluginUtil.getPluginByName(args, 1);

        if (target == null) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.invalid-plugin"));
            sendUsage();
            return;
        }

        String usages = PluginUtil.getUsages(target);

        sender.sendMessage(PlugMan.getInstance().getMessageManager().format("usage.usage", usages));

    }
}
