package com.rylinaux.plugman.utilities;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class PluginUtils {

    public static void enable(Plugin plugin) {
        if (!plugin.isEnabled() && plugin != null)
            Bukkit.getServer().getPluginManager().enablePlugin(plugin);
    }

    public static void enableAll() {
        for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
            enable(plugin);
        }
    }

    public static void disable(Plugin plugin) {
        if (plugin.isEnabled() && plugin != null)
            // Don't disable ourself, for safety reasons.
            if (!plugin.getName().equalsIgnoreCase("PlugMan"))
                Bukkit.getServer().getPluginManager().disablePlugin(plugin);
    }

    public static void disableAll() {
        for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
            disable(plugin);
        }
    }

    public static Plugin getPluginByName(String[] args, int start) {
        String plugin = args[start];
        if (args.length > (start + 1)) {
            for (int i = (start + 1); i < args.length; i++) {
                plugin = plugin + " " + args[i];
            }
        }
        return Bukkit.getPluginManager().getPlugin(plugin);
    }

}
