package com.rylinaux.plugman.util;

/*
 * #%L
 * PlugMan
 * %%
 * Copyright (C) 2010 - 2014 PlugMan
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import com.google.common.base.Joiner;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.rylinaux.plugman.PlugMan;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.plugin.*;

/**
 * Utilities for managing plugins.
 *
 * @author rylinaux
 */
public class PluginUtil {

    /**
     * Enable a plugin.
     *
     * @param plugin the plugin to enable
     */
    public static void enable(Plugin plugin) {
        if (plugin != null && !plugin.isEnabled()) {
            Bukkit.getPluginManager().enablePlugin(plugin);
        }
    }

    /**
     * Enable all plugins.
     */
    public static void enableAll() {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (!isIgnored(plugin)) {
                enable(plugin);
            }
        }
    }

    /**
     * Disable a plugin.
     *
     * @param plugin the plugin to disable
     */
    public static void disable(Plugin plugin) {
        if (plugin != null && plugin.isEnabled()) {
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    /**
     * Disable all plugins.
     */
    public static void disableAll() {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (!isIgnored(plugin)) {
                disable(plugin);
            }
        }
    }

    /**
     * Returns the formatted name of the plugin.
     *
     * @param plugin the plugin to format
     * @return the formatted name
     */
    public static String getFormattedName(Plugin plugin) {
        return getFormattedName(plugin, false);
    }

    /**
     * Returns the formatted name of the plugin.
     *
     * @param plugin          the plugin to format
     * @param includeVersions whether to include the version
     * @return the formatted name
     */
    public static String getFormattedName(Plugin plugin, boolean includeVersions) {
        ChatColor color = plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED;
        String pluginName = color + plugin.getName();
        if (includeVersions) {
            pluginName += " (" + plugin.getDescription().getVersion() + ")";
        }
        return pluginName;
    }

    /**
     * Returns a plugin from an array of Strings.
     *
     * @param args  the array
     * @param start the index to start at
     * @return the plugin
     */
    public static Plugin getPluginByName(String[] args, int start) {
        return getPluginByName(StringUtil.consolidateStrings(args, start));
    }

    /**
     * Returns a plugin from a String.
     *
     * @param name the name of the plugin
     * @return the plugin
     */
    public static Plugin getPluginByName(String name) {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (name.equalsIgnoreCase(plugin.getName())) {
                return plugin;
            }
        }
        return null;
    }

    /**
     * Returns a List of plugin names.
     *
     * @return list of plugin names
     */
    public static List<String> getPluginNames(boolean fullName) {
        List<String> plugins = new ArrayList<>();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            plugins.add(fullName ? plugin.getDescription().getFullName() : plugin.getName());
        }
        return plugins;
    }

    /**
     * Get the version of another plugin.
     *
     * @param name the name of the other plugin.
     * @return the version.
     */
    public static String getPluginVersion(String name) {
        Plugin plugin = getPluginByName(name);
        if (plugin != null && plugin.getDescription() != null) {
            return plugin.getDescription().getVersion();
        }
        return null;
    }

    /**
     * Returns the commands a plugin has registered.
     *
     * @param plugin the plugin to deal with
     * @return the commands registered
     */
    public static String getUsages(Plugin plugin) {

        List<String> parsedCommands = new ArrayList<>();

        Map commands = plugin.getDescription().getCommands();

        if (commands != null) {
            Iterator commandsIt = commands.entrySet().iterator();
            while (commandsIt.hasNext()) {
                Map.Entry thisEntry = (Map.Entry) commandsIt.next();
                if (thisEntry != null) {
                    parsedCommands.add((String) thisEntry.getKey());
                }
            }
        }

        if (parsedCommands.isEmpty())
            return "No commands registered.";

        return Joiner.on(", ").join(parsedCommands);

    }

    /**
     * Find which plugin has a given command registered.
     *
     * @param command the command.
     * @return the plugin.
     */
    public static List<String> findByCommand(String command) {

        List<String> plugins = new ArrayList<>();

        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {

            // Map of commands and their attributes.
            Map<String, Map<String, Object>> commands = plugin.getDescription().getCommands();

            if (commands != null) {

                // Iterator for all the plugin's commands.
                Iterator<Map.Entry<String, Map<String, Object>>> commandIterator = commands.entrySet().iterator();

                while (commandIterator.hasNext()) {

                    // Current value.
                    Map.Entry<String, Map<String, Object>> commandNext = commandIterator.next();

                    // Plugin name matches - return.
                    if (commandNext.getKey().equalsIgnoreCase(command)) {
                        plugins.add(plugin.getName());
                        continue;
                    }

                    // No match - let's iterate over the attributes and see if
                    // it has aliases.
                    Iterator<Map.Entry<String, Object>> attributeIterator = commandNext.getValue().entrySet().iterator();

                    while (attributeIterator.hasNext()) {

                        // Current value.
                        Map.Entry<String, Object> attributeNext = attributeIterator.next();

                        // Has an alias attribute.
                        if (attributeNext.getKey().equals("aliases")) {

                            Object aliases = attributeNext.getValue();

                            if (aliases instanceof String) {
                                if (((String) aliases).equalsIgnoreCase(command)) {
                                    plugins.add(plugin.getName());
                                    continue;
                                }
                            } else {

                                // Cast to a List of Strings.
                                List<String> array = (List<String>) aliases;

                                // Check for matches here.
                                for (String str : array) {
                                    if (str.equalsIgnoreCase(command)) {
                                        plugins.add(plugin.getName());
                                        continue;
                                    }
                                }

                            }

                        }

                    }
                }

            }

        }

        // No matches.
        return plugins;

    }

    /**
     * Checks whether the plugin is ignored.
     *
     * @param plugin the plugin to check
     * @return whether the plugin is ignored
     */
    public static boolean isIgnored(Plugin plugin) {
        return isIgnored(plugin.getName());
    }

    /**
     * Checks whether the plugin is ignored.
     *
     * @param plugin the plugin to check
     * @return whether the plugin is ignored
     */
    public static boolean isIgnored(String plugin) {
        for (String name : PlugMan.getInstance().getIgnoredPlugins()) {
            if (name.equalsIgnoreCase(plugin)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Loads and enables a plugin.
     *
     * @param plugin plugin to load
     * @return status message
     */
    private static String load(Plugin plugin) {
        return load(plugin.getName());
    }

    /**
     * Loads and enables a plugin.
     *
     * @param name plugin's name
     * @return status message
     */
    public static String load(String name) {

        Plugin target = null;

        File pluginDir = new File("plugins");

        if (!pluginDir.isDirectory()) {
            return PlugMan.getInstance().getMessageFormatter().format("load.plugin-directory");
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
                        return PlugMan.getInstance().getMessageFormatter().format("load.cannot-find");
                    }
                }
            }
        }

        try {
            target = Bukkit.getPluginManager().loadPlugin(pluginFile);
        } catch (InvalidDescriptionException e) {
            e.printStackTrace();
            return PlugMan.getInstance().getMessageFormatter().format("load.invalid-description");
        } catch (InvalidPluginException e) {
            e.printStackTrace();
            return PlugMan.getInstance().getMessageFormatter().format("load.invalid-plugin");
        }

        target.onLoad();
        Bukkit.getPluginManager().enablePlugin(target);

        return PlugMan.getInstance().getMessageFormatter().format("load.loaded", target.getName());

    }

    /**
     * Reload a plugin.
     *
     * @param plugin the plugin to reload
     */
    public static void reload(Plugin plugin) {
        if (plugin != null) {
            unload(plugin);
            load(plugin);
        }
    }

    /**
     * Reload all plugins.
     */
    public static void reloadAll() {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (!isIgnored(plugin)) {
                reload(plugin);
            }
        }
    }

    /**
     * Unload a plugin.
     *
     * @param plugin the plugin to unload
     * @return the message to send to the user.
     */
    public static String unload(Plugin plugin) {

        String name = plugin.getName();

        PluginManager pluginManager = Bukkit.getPluginManager();

        SimpleCommandMap commandMap = null;

        List<Plugin> plugins = null;

        Map<String, Plugin> names = null;
        Map<String, Command> commands = null;
        Map<Event, SortedSet<RegisteredListener>> listeners = null;

        boolean reloadlisteners = true;

        if (pluginManager != null) {

            pluginManager.disablePlugin(plugin);

            try {

                Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
                pluginsField.setAccessible(true);
                plugins = (List<Plugin>) pluginsField.get(pluginManager);

                Field lookupNamesField = Bukkit.getPluginManager().getClass().getDeclaredField("lookupNames");
                lookupNamesField.setAccessible(true);
                names = (Map<String, Plugin>) lookupNamesField.get(pluginManager);

                try {
                    Field listenersField = Bukkit.getPluginManager().getClass().getDeclaredField("listeners");
                    listenersField.setAccessible(true);
                    listeners = (Map<Event, SortedSet<RegisteredListener>>) listenersField.get(pluginManager);
                } catch (Exception e) {
                    reloadlisteners = false;
                }

                Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
                commandMapField.setAccessible(true);
                commandMap = (SimpleCommandMap) commandMapField.get(pluginManager);

                Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
                knownCommandsField.setAccessible(true);
                commands = (Map<String, Command>) knownCommandsField.get(commandMap);

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                return PlugMan.getInstance().getMessageFormatter().format("unload.failed", name);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return PlugMan.getInstance().getMessageFormatter().format("unload.failed", name);
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

        // Attempt to close the classloader to unlock any handles on the plugin's jar file.
        ClassLoader cl = plugin.getClass().getClassLoader();

        if (cl instanceof URLClassLoader) {

            try {

                Field pluginField = cl.getClass().getDeclaredField("plugin");
                pluginField.setAccessible(true);
                pluginField.set(cl, null);

                Field pluginInitField = cl.getClass().getDeclaredField("pluginInit");
                pluginInitField.setAccessible(true);
                pluginInitField.set(cl, null);

            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(PluginUtil.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {

                ((URLClassLoader) cl).close();
            } catch (IOException ex) {
                Logger.getLogger(PluginUtil.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        // Will not work on processes started with the -XX:+DisableExplicitGC flag, but lets try it anyway.
        // This tries to get around the issue where Windows refuses to unlock jar files that were previously loaded into the JVM.
        System.gc();

        return PlugMan.getInstance().getMessageFormatter().format("unload.unloaded", name);

    }

}
