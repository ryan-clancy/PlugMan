package com.rylinaux.plugman.utilities;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Utilities {

    public static Plugin getPluginByName(String[] args, int start) {
        String plugin = args[start];
        if (args.length > (start + 1)) {
            for (int i = (start + 1); i < args.length; i++) {
                plugin = plugin + " " + args[i];
            }
        }
        return Bukkit.getPluginManager().getPlugin(plugin);
    }

    public static void enable(Plugin plugin) {
        if (!plugin.isEnabled())
            Bukkit.getServer().getPluginManager().enablePlugin(plugin);
    }

    public static void enableAll() {
        for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
            if (!plugin.isEnabled())
                Bukkit.getServer().getPluginManager().enablePlugin(plugin);
        }
    }

    public static void disable(Plugin plugin) {
        if (plugin.isEnabled())
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
    }

    public static void disableAll() {
        for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
            if (plugin.isEnabled())
                Bukkit.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

}
