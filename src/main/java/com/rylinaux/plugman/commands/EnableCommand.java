package com.rylinaux.plugman.commands;

import com.rylinaux.plugman.utilities.Permissions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EnableCommand {


    public void execute(JavaPlugin plugin, CommandSender sender, Command cmd, String label, String[] args) {

        // TODO: Fix chat formatting.

        if (!sender.hasPermission(Permissions.ENABLE)) {
            sender.sendMessage("");
            return;
        }

        PluginManager pluginManager = plugin.getServer().getPluginManager();
    }

}