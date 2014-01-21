package com.rylinaux.plugman.commands;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import com.rylinaux.plugman.PlugMan;
import com.rylinaux.plugman.utilities.PluginUtils;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class ListCommand extends AbstractCommand {

    public static final String DESCRIPTION = "List all plugins.";

    public static final String PERMISSION = "plugman.list";

    public static final String USAGE = "/plugman list [-v]";

    public static final String[] SUB_PERMISSIONS = {""};

    public ListCommand(CommandSender sender) {
        super(sender, DESCRIPTION, PERMISSION, SUB_PERMISSIONS, USAGE);
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {

        if (!hasPermission()) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.no-permission"));
            return;
        }

        boolean includeVersions = includeVersions(args);

        List<String> pluginList = Lists.newArrayList();

        for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
            pluginList.add(PluginUtils.getFormattedName(plugin, includeVersions));
        }

        Collections.sort(pluginList, String.CASE_INSENSITIVE_ORDER);

        String plugins = Joiner.on(", ").join(pluginList);

        sender.sendMessage(PlugMan.getInstance().getMessageManager().format("list.list", pluginList.size(), plugins));

    }

    private boolean includeVersions(String[] args) {
        for (String arg : args) {
            if (arg.equalsIgnoreCase("-v"))
                return true;
        }
        return false;
    }

}
