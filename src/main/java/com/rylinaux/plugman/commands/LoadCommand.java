package com.rylinaux.plugman.commands;

import com.rylinaux.plugman.PlugMan;
import com.rylinaux.plugman.utilities.PluginUtil;
import com.rylinaux.plugman.utilities.StringUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Command that lists plugins.
 *
 * @author rylinaux
 */
public class LoadCommand extends AbstractCommand {

    /**
     * The name of the command.
     */
    public static final String NAME = "Load";

    /**
     * The description of the command.
     */
    public static final String DESCRIPTION = "Load a plugin.";

    /**
     * The main permission of the command.
     */
    public static final String PERMISSION = "plugman.load";

    /**
     * The proper usage of the command.
     */
    public static final String USAGE = "/plugman load [plugin]";

    /**
     * The sub permissions of the command.
     */
    public static final String[] SUB_PERMISSIONS = {""};

    /**
     * Construct out object.
     *
     * @param sender the command sender
     */
    public LoadCommand(CommandSender sender) {
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

        Plugin potential = PluginUtil.getPluginByName(args, 1);

        if (potential != null) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("load.already-loaded", potential.getName()));
            return;
        }

        String name = StringUtil.consolidateStrings(args, 1);

        if (PluginUtil.isIgnored(name)) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.ignored"));
            return;
        }

        sender.sendMessage(PluginUtil.load(name));

    }
}
