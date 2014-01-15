package com.rylinaux.plugman.utilities;

import com.rylinaux.plugman.PlugMan;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class PluginUtils {

    public static String consolidateStrings(String[] args, int start) {
        String plugin = args[start];
        if (args.length > (start + 1)) {
            for (int i = (start + 1); i < args.length; i++) {
                plugin = plugin + " " + args[i];
            }
        }
        return plugin;
    }

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
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
    }

    public static void disableAll() {
        for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
            if (plugin.getName().equalsIgnoreCase("PlugMan"))
                continue;
            disable(plugin);
        }
    }

    public static String getFormattedName(Plugin plugin) {
        return getFormattedName(plugin, false);
    }

    public static String getFormattedName(Plugin plugin, boolean includeVersions) {
        ChatColor color = plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED;
        if (includeVersions)
            return color + plugin.getDescription().getFullName();
        return color + plugin.getDescription().getName();
    }

    public static Plugin getPluginByName(String[] args, int start) {
        String plugin = args[start];
        if (args.length > (start + 1)) {
            for (int i = (start + 1); i < args.length; i++) {
                plugin = plugin + " " + args[i];
            }
        }
        return Bukkit.getServer().getPluginManager().getPlugin(plugin);
    }

    public static List<String> getPluginNames() {
        List<String> plugins = new ArrayList<>();
        for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
            plugins.add(plugin.getName());
        }
        return plugins;
    }

    public static String load(String name) {

        File pluginDir = new File("plugins");

        if (!pluginDir.isDirectory()) {
            return PlugMan.getInstance().getMessageManager().format("load.plugin-directory");
        }

        File pluginFile = new File(pluginDir, name + ".jar");

        if (!pluginFile.isFile()) {
            for (File f : pluginDir.listFiles()) {
                if (f.getName().endsWith(".jar")) {
                    try {
                        PluginDescriptionFile desc = PlugMan.getInstance().getPluginLoader().getPluginDescription(f);
                        if (desc.getName().equalsIgnoreCase(name)) {
                            pluginFile = f;
                            break;
                        }
                    } catch (InvalidDescriptionException e) {
                        return PlugMan.getInstance().getMessageManager().format("load.cannot-find");
                    }
                }
            }
        }

        try {
            Bukkit.getServer().getPluginManager().loadPlugin(pluginFile);
        } catch (InvalidDescriptionException e) {
            e.printStackTrace();
            return PlugMan.getInstance().getMessageManager().format("load.invalid-description");
        } catch (InvalidPluginException e) {
            e.printStackTrace();
            return PlugMan.getInstance().getMessageManager().format("load.invalid-plugin");
        }

        Plugin target = Bukkit.getServer().getPluginManager().getPlugin(name);
        target.onLoad();
        enable(target);

        return PlugMan.getInstance().getMessageManager().format("load.loaded", name);

    }

}
