package com.rylinaux.plugman.utilities;

import com.google.common.base.Joiner;

import com.rylinaux.plugman.PlugMan;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;

public class PluginUtil {

    public static void enable(Plugin plugin) {
        if (!plugin.isEnabled() && plugin != null)
            Bukkit.getServer().getPluginManager().enablePlugin(plugin);
    }

    public static void enableAll() {
        for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins())
            if (!isIgnored(plugin))
                enable(plugin);
    }

    public static void disable(Plugin plugin) {
        if (plugin.isEnabled() && plugin != null)
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
    }

    public static void disableAll() {
        for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
            if (!isIgnored(plugin))
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
        return getPluginByName(StringUtil.consolidateStrings(args, start));
    }

    public static Plugin getPluginByName(String name) {
        for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
            if (name.equalsIgnoreCase(plugin.getName()))
                return plugin;
        }
        return null;
    }

    public static List<String> getPluginNames() {
        List<String> plugins = new ArrayList<>();
        for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins())
            plugins.add(plugin.getName());
        return plugins;
    }

    public static String getUsages(Plugin plugin) {

        List<String> parsedCommands = new ArrayList<>();

        Map commands = plugin.getDescription().getCommands();

        if (commands != null) {
            Iterator commandsIt = commands.entrySet().iterator();
            while (commandsIt.hasNext()) {
                Map.Entry thisEntry = (Map.Entry) commandsIt.next();
                if (thisEntry != null)
                    parsedCommands.add((String) thisEntry.getKey());
            }
        }

        if (parsedCommands.isEmpty())
            return "No commands registered.";

        return Joiner.on(", ").join(parsedCommands);

    }

    public static boolean isIgnored(Plugin plugin) {
        return isIgnored(plugin.getName());
    }

    public static boolean isIgnored(String plugin) {
        for (String name : PlugMan.getInstance().getIgnoredPlugins()) {
            if (name.equalsIgnoreCase(plugin))
                return true;
        }
        return false;
    }

    public static String load(Plugin plugin) {
        return load(plugin.getName());
    }

    public static String load(String name) {

        Plugin target = null;

        File pluginDir = new File("plugins");

        if (!pluginDir.isDirectory())
            return PlugMan.getInstance().getMessageManager().format("load.plugin-directory");

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
            target = Bukkit.getServer().getPluginManager().loadPlugin(pluginFile);
        } catch (InvalidDescriptionException e) {
            e.printStackTrace();
            return PlugMan.getInstance().getMessageManager().format("load.invalid-description");
        } catch (InvalidPluginException e) {
            e.printStackTrace();
            return PlugMan.getInstance().getMessageManager().format("load.invalid-plugin");
        }

        target.onLoad();
        Bukkit.getServer().getPluginManager().enablePlugin(target);

        return PlugMan.getInstance().getMessageManager().format("load.loaded", name);

    }

    public static void reload(Plugin plugin) {
        if (plugin != null) {
            unload(plugin);
            load(plugin);
        }
    }

    public static void reloadAll() {
        for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
            if (!isIgnored(plugin))
                reload(plugin);
        }
    }

    public static String unload(Plugin plugin) {

        String name = plugin.getName();

        PluginManager pluginManager = Bukkit.getServer().getPluginManager();

        SimpleCommandMap commandMap = null;

        List<Plugin> plugins = null;

        Map<String, Plugin> names = null;
        Map<String, Command> commands = null;
        Map<Event, SortedSet<RegisteredListener>> listeners = null;

        boolean reloadlisteners = true;

        if (pluginManager != null) {

            try {

                Field pluginsField = Bukkit.getServer().getPluginManager().getClass().getDeclaredField("plugins");
                pluginsField.setAccessible(true);
                plugins = (List<Plugin>) pluginsField.get(pluginManager);

                Field lookupNamesField = Bukkit.getServer().getPluginManager().getClass().getDeclaredField("lookupNames");
                lookupNamesField.setAccessible(true);
                names = (Map<String, Plugin>) lookupNamesField.get(pluginManager);

                try {
                    Field listenersField = Bukkit.getServer().getPluginManager().getClass().getDeclaredField("listeners");
                    listenersField.setAccessible(true);
                    listeners = (Map<Event, SortedSet<RegisteredListener>>) listenersField.get(pluginManager);
                } catch (Exception e) {
                    reloadlisteners = false;
                }

                Field commandMapField = Bukkit.getServer().getPluginManager().getClass().getDeclaredField("commandMap");
                commandMapField.setAccessible(true);
                commandMap = (SimpleCommandMap) commandMapField.get(pluginManager);

                Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
                knownCommandsField.setAccessible(true);
                commands = (Map<String, Command>) knownCommandsField.get(commandMap);

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                return PlugMan.getInstance().getMessageManager().format("unload.failed", name);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return PlugMan.getInstance().getMessageManager().format("unload.failed", name);
            }
        }

        pluginManager.disablePlugin(plugin);

        if (plugins != null && plugins.contains(plugin))
            plugins.remove(plugin);

        if (names != null && names.containsKey(name))
            names.remove(name);

        if (listeners != null && reloadlisteners) {
            for (SortedSet<RegisteredListener> set : listeners.values()) {
                for (Iterator<RegisteredListener> it = set.iterator(); it.hasNext(); ) {
                    RegisteredListener value = it.next();
                    if (value.getPlugin() == plugin) {
                        it.remove();
                    }
                }
            }
        }

        if (commandMap != null) {
            for (Iterator<Map.Entry<String, Command>> it = commands.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Command> entry = it.next();
                if (entry.getValue() instanceof PluginCommand) {
                    PluginCommand c = (PluginCommand) entry.getValue();
                    if (c.getPlugin() == plugin) {
                        c.unregister(commandMap);
                        it.remove();
                    }
                }
            }
        }

        return PlugMan.getInstance().getMessageManager().format("unload.unloaded", name);

    }

}