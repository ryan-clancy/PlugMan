package com.rylinaux.plugman.commands;

import com.rylinaux.plugman.contructs.SimpleCommand;
import com.rylinaux.plugman.contructs.SimpleCommandExecutor;
import com.rylinaux.plugman.utilities.Messaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ListCommand extends SimpleCommand implements SimpleCommandExecutor {

    public ListCommand(JavaPlugin plugin, CommandSender sender, Command command, String label, String[] args, String permission) {
        super(plugin, sender, command, label, args, permission);
    }

    public void execute() {

        if (!this.hasPermission()) {
            this.sender.sendMessage(Messaging.NO_PERMISSION);
            return;
        }

        StringBuilder builder = new StringBuilder();

        List<String> pluginList = new ArrayList<String>();

        for (Plugin p : plugin.getServer().getPluginManager().getPlugins()) {
            pluginList.add(getFormattedName(p));
        }

        Collections.sort(pluginList, String.CASE_INSENSITIVE_ORDER);

        for (String name : pluginList) {
            if (builder.length() > 0) {
                builder.append(ChatColor.WHITE).append(", ");
            }
            builder.append(name);
        }

        sender.sendMessage(Messaging.LIST + builder.toString());

    }

    private boolean includeVersions(String[] args) {
        for (String s : args) {
            if (s.equalsIgnoreCase("-v")) {
                return true;
            }
        }
        return false;
    }

    private String getFormattedName(Plugin p) {
        ChatColor color = p.isEnabled() ? ChatColor.GREEN : ChatColor.RED;
        boolean includeVersion = includeVersions(args);
        if (includeVersion) {
            return (color + p.getDescription().getFullName());
        } else {
            return (color + p.getDescription().getName());
        }
    }

}
