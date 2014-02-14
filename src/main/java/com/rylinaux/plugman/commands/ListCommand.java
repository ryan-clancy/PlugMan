package com.rylinaux.plugman.commands;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import com.rylinaux.plugman.PlugMan;
import com.rylinaux.plugman.utilities.PluginUtil;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Command that lists plugins.
 *
 * @author rylinaux
 */
public class ListCommand extends AbstractCommand {

    /**
     * The name of the command.
     */
    public static final String NAME = "List";

    /**
     * The description of the command.
     */
    public static final String DESCRIPTION = "List all plugins.";

    /**
     * The main permission of the command.
     */
    public static final String PERMISSION = "plugman.list";

    /**
     * The proper usage of the command.
     */
    public static final String USAGE = "/plugman list [-v]";

    /**
     * The sub permissions of the command.
     */
    public static final String[] SUB_PERMISSIONS = {""};

    /**
     * Construct out object.
     *
     * @param sender the command sender
     */
    public ListCommand(CommandSender sender) {
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

        boolean includeVersions = includeVersions(args);

        List<String> pluginList = Lists.newArrayList();

        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            pluginList.add(PluginUtil.getFormattedName(plugin, includeVersions));
        }

        Collections.sort(pluginList, String.CASE_INSENSITIVE_ORDER);

        String plugins = Joiner.on(", ").join(pluginList);

        sender.sendMessage(PlugMan.getInstance().getMessenger().format("list.list", pluginList.size(), plugins));

    }

    private boolean includeVersions(String[] args) {
        for (String arg : args) {
            if (arg.equalsIgnoreCase("-v"))
                return true;
        }
        return false;
    }

}
