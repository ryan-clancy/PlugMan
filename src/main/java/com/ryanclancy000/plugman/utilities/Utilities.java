package com.ryanclancy000.plugman.utilities;

import com.ryanclancy000.plugman.PlugMan;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.SortedSet;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.UnknownDependencyException;

public class Utilities {

    private final PlugMan plugin;
    private static final String pre = ChatColor.GRAY + "[" + ChatColor.GREEN + "PlugMan" + ChatColor.GRAY + "] ";
    private static final String tooMany = ChatColor.RED + "Too many arguments!";
    private static final String specifyPlugin = ChatColor.RED + "Must specify a plugin!";
    private static final String pluginNotFound = ChatColor.RED + "Plugin not found!";

    public Utilities(PlugMan instance) {
        plugin = instance;
    }

    private Plugin getPlugin(String p) {
        for (Plugin pl : plugin.getServer().getPluginManager().getPlugins()) {
            if (pl.getDescription().getName().equalsIgnoreCase(p)) {
                return pl;
            }
        }
        return null;
    }

    private String consolidateArgs(String[] args) {
        String pl = args[1];
        if (args.length > 2) {
            for (int i = 2; i < args.length; i++) {
                pl = pl + " " + args[i];
            }
        }
        return pl;
    }

    public void thisInfo(CommandSender sender) {
        sender.sendMessage(pre + ChatColor.GREEN + "v" + plugin.getDescription().getVersion() + ChatColor.GRAY + " by " + ChatColor.GREEN + "ryanclancy000");
        sender.sendMessage(ChatColor.GRAY + "-" + ChatColor.GREEN + " To view commands, do /plugman help");
    }

    public void helpCommand(CommandSender sender) {
        sender.sendMessage(ChatColor.WHITE + "--------------------- " + ChatColor.GRAY + "[" + ChatColor.GREEN + " PlugMan " + ChatColor.GRAY + "]" + ChatColor.WHITE + " ---------------------");
        if (sender.hasPermission("plugman.list")) {
            sender.sendMessage(ChatColor.GREEN + "/plugman list" + ChatColor.WHITE + " - " + ChatColor.GRAY + "List all plugins.");
        }
        if (sender.hasPermission("plugman.vlist")) {
            sender.sendMessage(ChatColor.GREEN + "/plugman vlist" + ChatColor.WHITE + " - " + ChatColor.GRAY + "List all plugins with versions.");
        }
        if (sender.hasPermission("plugman.info")) {
            sender.sendMessage(ChatColor.GREEN + "/plugman info [plugin]" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Give plugin info.");
        }
        if (sender.hasPermission("plugman.usage")) {
            sender.sendMessage(ChatColor.GREEN + "/plugman usage [plugin]" + ChatColor.WHITE + " - " + ChatColor.GRAY + "List command plugin has registered.");
        }
        if (sender.hasPermission("plugman.status")) {
            sender.sendMessage(ChatColor.GREEN + "/plugman status [plugin]" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Give the status of a plugin.");
        }
        if (sender.hasPermission("plugman.test")) {
            sender.sendMessage(ChatColor.GREEN + "/plugman test [permission] [player]" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Test a permission node.");
        }
        if (sender.hasPermission("plugman.load")) {
            sender.sendMessage(ChatColor.GREEN + "/plugman load [plugin]" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Load a plugin.");
        }
        if (sender.hasPermission("plugman.unload")) {
            sender.sendMessage(ChatColor.GREEN + "/plugman unload [plugin]" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Unload a plugin.");
        }
        if (sender.hasPermission("plugman.reload")) {
            sender.sendMessage(ChatColor.GREEN + "/plugman reload [plugin|all]" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Reload a plugin.");
        }
        if (sender.hasPermission("plugman.restart")) {
            sender.sendMessage(ChatColor.GREEN + "/plugman restart [plugin|all]" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Restart a plugin.");
        }
        if (sender.hasPermission("plugman.enable")) {
            sender.sendMessage(ChatColor.GREEN + "/plugman enable [plugin|all]" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Enable a plugin.");
        }
        if (sender.hasPermission("plugman.disable")) {
            sender.sendMessage(ChatColor.GREEN + "/plugman disable [plugin|all]" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Disable a plugin.");
        }
    }

    public void listCommand(CommandSender sender, String[] args) {
        String pluginName = "";
        StringBuilder list = new StringBuilder();
        List<String> pluginList = new ArrayList<String>();
        for (Plugin pl : plugin.getServer().getPluginManager().getPlugins()) {
            pluginName = (pl.isEnabled() ? ChatColor.GREEN : ChatColor.RED) + pl.getDescription().getName();
            pluginList.add(pluginName);
        }
        Collections.sort(pluginList, String.CASE_INSENSITIVE_ORDER);
        for (String name : pluginList) {
            if (list.length() > 0) {
                list.append(ChatColor.WHITE).append(", ");
            }
            list.append(name);
        }
        sender.sendMessage(pre + ChatColor.GRAY + "Plugins: " + list);
    }

    public void vlistCommand(CommandSender sender, String[] args) {
        String pluginName = "";
        StringBuilder list = new StringBuilder();
        List<String> pluginList = new ArrayList<String>();
        for (Plugin pl : plugin.getServer().getPluginManager().getPlugins()) {
            pluginName = (pl.isEnabled() ? ChatColor.GREEN : ChatColor.RED) + pl.getDescription().getFullName();
            pluginList.add(pluginName);
        }
        Collections.sort(pluginList, String.CASE_INSENSITIVE_ORDER);
        for (String name : pluginList) {
            if (list.length() > 0) {
                list.append(ChatColor.WHITE).append(", ");
            }
            list.append(name);
        }
        sender.sendMessage(pre + ChatColor.GRAY + "Plugins: " + list);
    }

    public void infoCommand(CommandSender sender, String[] args) {
        if (args.length > 1) {
            Plugin targetPlugin = getPlugin(consolidateArgs(args));
            if (targetPlugin == null) {
                sender.sendMessage(pre + ChatColor.RED + pluginNotFound);
            } else {
                sender.sendMessage(pre + ChatColor.GRAY + "Plugin Info: " + ChatColor.GREEN + targetPlugin.getName());
                sender.sendMessage(ChatColor.GREEN + "Version: " + ChatColor.GRAY + targetPlugin.getDescription().getVersion());
                sender.sendMessage(ChatColor.GREEN + "Authors: " + ChatColor.GRAY + targetPlugin.getDescription().getAuthors());
                sender.sendMessage(ChatColor.GREEN + "Status: " + (targetPlugin.isEnabled() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
            }
        } else {
            sender.sendMessage(pre + ChatColor.RED + specifyPlugin);
        }
    }

    public void statusCommand(CommandSender sender, String[] args) {
        if (args.length > 1) {
            Plugin targetPlugin = getPlugin(consolidateArgs(args));
            if (targetPlugin == null) {
                sender.sendMessage(pre + ChatColor.RED + pluginNotFound);
            } else if (targetPlugin.isEnabled()) {
                sender.sendMessage(pre + ChatColor.GREEN + targetPlugin.getName() + " is enabled!");
            } else {
                sender.sendMessage(pre + ChatColor.GREEN + targetPlugin.getName() + " is disabled!");
            }
        } else {
            sender.sendMessage(pre + ChatColor.RED + specifyPlugin);
        }
    }

    public void usageCommand(CommandSender sender, String[] args) {

        if (args.length > 1) {
            Plugin targetPlugin = getPlugin(consolidateArgs(args));
            if (targetPlugin != null) {
                ArrayList<String> out = new ArrayList<String>();
                ArrayList<String> parsedCommands = new ArrayList<String>();
                Map commands = targetPlugin.getDescription().getCommands();
                if (commands != null) {
                    Iterator commandsIt = commands.entrySet().iterator();
                    while (commandsIt.hasNext()) {
                        Map.Entry thisEntry = (Map.Entry) commandsIt.next();
                        if (thisEntry != null) {
                            parsedCommands.add((String) thisEntry.getKey());
                        }
                    }
                }
                if (!parsedCommands.isEmpty()) {
                    StringBuilder commandsOut = new StringBuilder();
                    commandsOut.append(pre).append(ChatColor.GRAY).append("Command List: ");
                    for (int i = 0; i < parsedCommands.size(); i++) {
                        String thisCommand = parsedCommands.get(i);
                        if (commandsOut.length() + thisCommand.length() > 55) {
                            sender.sendMessage(commandsOut.toString());
                            commandsOut = new StringBuilder();
                        }
                        if (parsedCommands.size() > 0) {
                            commandsOut.append(ChatColor.GREEN).append("\"").append(thisCommand).append("\" ");
                        } else {
                            commandsOut.append(ChatColor.GREEN).append("\"").append(thisCommand).append("\"");
                        }
                    }
                    out.add(commandsOut.toString());
                } else {
                    out.add(pre + ChatColor.RED + "Plugin has no registered commands!");
                }
                for (String s : out) {
                    sender.sendMessage(s);
                }
            } else {
                sender.sendMessage(pre + ChatColor.RED + pluginNotFound);
            }
        } else {
            sender.sendMessage(pre + ChatColor.RED + specifyPlugin);
        }
    }

    public void testCommand(CommandSender sender, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(pre + ChatColor.RED + "Must specify permission and player!");
        } else if (args.length == 2) {
            if (sender.hasPermission(args[1])) {
                sender.sendMessage(pre + ChatColor.GREEN + "You have permission for '" + args[1] + "'.");
            } else {
                sender.sendMessage(pre + ChatColor.RED + "You do not have permission for '" + args[1] + "'.");
            }
        } else if (args.length == 3) {
            Player target = plugin.getServer().getPlayer(args[2]);
            if (target == null) {
                sender.sendMessage(pre + ChatColor.RED + "Player not found!");
            } else {
                if (target.hasPermission(args[1])) {
                    sender.sendMessage(pre + ChatColor.GREEN + target.getName() + " has permission for " + args[1]);
                } else {
                    sender.sendMessage(pre + ChatColor.RED + target.getName() + " does not have permission for " + args[1]);
                }
            }
        } else {
            sender.sendMessage(pre + ChatColor.RED + "Invalid arguments!");
        }
    }

    public void loadCommand(CommandSender sender, String[] args) {
        if (args.length > 1) {
            String pl = consolidateArgs(args);
            Plugin targetPlugin = getPlugin(pl);
            if (targetPlugin != null) {
                if (targetPlugin.isEnabled()) {
                    sender.sendMessage(pre + ChatColor.RED + "Plugin already loaded and enabled!");
                } else {
                    sender.sendMessage(pre + ChatColor.RED + "Plugin already loaded, but is disabled!");
                }
                return;
            }
            sender.sendMessage(loadPlugin(pl));
        } else {
            sender.sendMessage(pre + ChatColor.RED + specifyPlugin);
        }
    }

    private String loadPlugin(String pl) {
        Plugin targetPlugin = null;
        String msg = "";
        File pluginDir = new File("plugins");
        if (!pluginDir.isDirectory()) {
            return (pre + ChatColor.RED + "Plugin directory not found!");
        }
        File pluginFile = new File(pluginDir, pl + ".jar");
        //plugin.getLogger().info("Want: " + pluginFile);
        if (!pluginFile.isFile()) {
            for (File f : pluginDir.listFiles()) {
                try {
                    if (f.getName().endsWith(".jar")) {
                        PluginDescriptionFile pdf = plugin.getPluginLoader().getPluginDescription(f);
                        //plugin.getLogger().info("Searching for " + pl + ": " + f + " -> " + pdf.getName());
                        if (pdf.getName().equalsIgnoreCase(pl)) {
                            pluginFile = f;
                            msg = "(via search) ";
                            break;
                        }
                    }
                } catch (InvalidDescriptionException e) {
                    return (pre + ChatColor.RED + "Couldn't find file and failed to search descriptions!");
                }
            }
        }
        try {
            plugin.getServer().getPluginManager().loadPlugin(pluginFile);
            targetPlugin = getPlugin(pl);
            targetPlugin.onLoad();
            plugin.getServer().getPluginManager().enablePlugin(targetPlugin);
            return (pre + ChatColor.GREEN + getPlugin(pl) + " loaded " + msg + "and enabled!");
        } catch (UnknownDependencyException e) {
            return (pre + ChatColor.RED + "File exists, but is missing a dependency!");
        } catch (InvalidPluginException e) {
            plugin.getLogger().log(Level.SEVERE, "Tried to load invalid Plugin.\n", e);
            return (pre + ChatColor.RED + "File exists, but isn't a loadable plugin file!");
        } catch (InvalidDescriptionException e) {
            return (pre + ChatColor.RED + "Plugin exists, but has an invalid description!");
        }
    }

    public void unloadCommand(CommandSender sender, String[] args) {
        if (args.length > 1) {
            String pl = consolidateArgs(args);
            Plugin targetPlugin = getPlugin(pl);
            if (targetPlugin == null) {
                sender.sendMessage(pre + ChatColor.RED + pluginNotFound);
            } else {
                sender.sendMessage(unloadPlugin(pl));
            }
        } else {
            sender.sendMessage(pre + ChatColor.RED + specifyPlugin);
        }
    }

    private String unloadPlugin(String pl) {

        PluginManager pm = plugin.getServer().getPluginManager();
        SimplePluginManager spm = (SimplePluginManager) pm;
        SimpleCommandMap cmdMap = null;
        List<Plugin> plugins = null;
        Map<String, Plugin> names = null;
        Map<String, Command> commands = null;
        Map<Event, SortedSet<RegisteredListener>> listeners = null;
        boolean reloadlisteners = true;

        if (spm != null) {
            try {
                Field pluginsField = spm.getClass().getDeclaredField("plugins");
                pluginsField.setAccessible(true);
                plugins = (List<Plugin>) pluginsField.get(spm);

                Field lookupNamesField = spm.getClass().getDeclaredField("lookupNames");
                lookupNamesField.setAccessible(true);
                names = (Map<String, Plugin>) lookupNamesField.get(spm);

                try {
                    Field listenersField = spm.getClass().getDeclaredField("listeners");
                    listenersField.setAccessible(true);
                    listeners = (Map<Event, SortedSet<RegisteredListener>>) listenersField.get(spm);
                } catch (Exception e) {
                    reloadlisteners = false;
                }

                Field commandMapField = spm.getClass().getDeclaredField("commandMap");
                commandMapField.setAccessible(true);
                cmdMap = (SimpleCommandMap) commandMapField.get(spm);

                Field knownCommandsField = cmdMap.getClass().getDeclaredField("knownCommands");
                knownCommandsField.setAccessible(true);
                commands = (Map<String, Command>) knownCommandsField.get(cmdMap);
            } catch (NoSuchFieldException e) {
                return (pre + ChatColor.RED + "Failed to unload plugin!");
            } catch (IllegalAccessException e) {
                return (pre + ChatColor.RED + "Failed to unload plugin!");
            }
        }

        String tp = "";
        for (Plugin p : plugin.getServer().getPluginManager().getPlugins()) {
            if (p.getDescription().getName().equalsIgnoreCase(pl)) {
                pm.disablePlugin(p);
                tp += p.getName() + " ";
                if (plugins != null && plugins.contains(p)) {
                    plugins.remove(p);
                }

                if (names != null && names.containsKey(pl)) {
                    names.remove(pl);
                }

                if (listeners != null && reloadlisteners) {
                    for (SortedSet<RegisteredListener> set : listeners.values()) {
                        for (Iterator<RegisteredListener> it = set.iterator(); it.hasNext();) {
                            RegisteredListener value = it.next();

                            if (value.getPlugin() == p) {
                                it.remove();
                            }
                        }
                    }
                }

                if (cmdMap != null) {
                    for (Iterator<Map.Entry<String, Command>> it = commands.entrySet().iterator(); it.hasNext();) {
                        Map.Entry<String, Command> entry = it.next();
                        if (entry.getValue() instanceof PluginCommand) {
                            PluginCommand c = (PluginCommand) entry.getValue();
                            if (c.getPlugin() == p) {
                                c.unregister(cmdMap);
                                it.remove();
                            }
                        }
                    }
                }
            }
        }
        return (pre + ChatColor.GREEN + tp + "has been unloaded and disabled!");
    }

    public void reloadCommand(CommandSender sender, String[] args) {
        if (args.length > 1) {
            if ("all".equalsIgnoreCase(args[1]) || "*".equalsIgnoreCase(args[1])) {
                for (Plugin pl : plugin.getServer().getPluginManager().getPlugins()) {
                    if (plugin.getSkipped().contains(pl.getName()) || plugin.getSkipped().isEmpty()) {
                        continue;
                    } else {
                        String pn = pl.getName();
                        sender.sendMessage(unloadPlugin(pn));
                        sender.sendMessage(loadPlugin(pn));
                    }
                }
                sender.sendMessage(pre + ChatColor.GREEN + "All plugins reloaded!");
                return;
            }
            String pl = consolidateArgs(args);
            if (getPlugin(pl) != null) {
                sender.sendMessage(unloadPlugin(pl));
                sender.sendMessage(loadPlugin(pl));
            } else {
                sender.sendMessage(pre + ChatColor.RED + pluginNotFound);
            }
        } else {
            sender.sendMessage(pre + ChatColor.RED + specifyPlugin);
        }
    }

    public void restartCommand(CommandSender sender, String[] args) {
        if (args.length > 1) {
            if ("all".equalsIgnoreCase(args[1]) || "*".equalsIgnoreCase(args[1])) {
                for (Plugin pl : plugin.getServer().getPluginManager().getPlugins()) {
                    if (plugin.getSkipped().contains(pl.getName()) || plugin.getSkipped().isEmpty()) {
                        continue;
                    } else {
                        plugin.getServer().getPluginManager().disablePlugin(pl);
                        plugin.getServer().getPluginManager().enablePlugin(pl);
                    }
                }
                sender.sendMessage(pre + ChatColor.GREEN + "All plugins restarted!");
            } else {
                String pl = consolidateArgs(args);
                if (getPlugin(pl) != null) {
                    Plugin targetPlugin = getPlugin(pl);
                    plugin.getServer().getPluginManager().disablePlugin(targetPlugin);
                    plugin.getServer().getPluginManager().enablePlugin(targetPlugin);
                    sender.sendMessage(pre + ChatColor.GREEN + targetPlugin.getName() + " Restarted!");
                } else {
                    sender.sendMessage(pre + ChatColor.RED + pluginNotFound);
                }
            }
        } else {
            sender.sendMessage(pre + ChatColor.RED + specifyPlugin);
        }
    }

    public void enableCommand(CommandSender sender, String[] args) {
        if (args.length > 1) {
            if ("all".equalsIgnoreCase(args[1]) || "*".equalsIgnoreCase(args[1])) {
                for (Plugin pl : plugin.getServer().getPluginManager().getPlugins()) {
                    plugin.getServer().getPluginManager().enablePlugin(pl);
                }
                sender.sendMessage(pre + ChatColor.GREEN + "All plugins enabled!");
            } else {
                String pl = consolidateArgs(args);
                if (getPlugin(pl) != null) {
                    if (!getPlugin(pl).isEnabled()) {
                        Plugin targetPlugin = getPlugin(pl);
                        plugin.getServer().getPluginManager().enablePlugin(targetPlugin);
                        sender.sendMessage(pre + ChatColor.GREEN + targetPlugin.getName() + " Enabled!");
                    } else {
                        sender.sendMessage(pre + ChatColor.RED + "Plugin already enabled!");
                    }
                } else {
                    sender.sendMessage(pre + ChatColor.RED + pluginNotFound);
                }
            }
        } else {
            sender.sendMessage(pre + ChatColor.RED + specifyPlugin);
        }
    }

    public void disableCommand(CommandSender sender, String[] args) {
        if (args.length > 1) {
            if ("all".equalsIgnoreCase(args[1]) || "*".equalsIgnoreCase(args[1])) {
                for (Plugin pl : plugin.getServer().getPluginManager().getPlugins()) {
                    plugin.getServer().getPluginManager().disablePlugin(pl);
                }
                sender.sendMessage(pre + ChatColor.RED + "All plugins disabled!");
            } else {
                String pl = consolidateArgs(args);
                if (getPlugin(pl) != null) {
                    if (getPlugin(pl).isEnabled()) {
                        Plugin targetPlugin = getPlugin(pl);
                        plugin.getServer().getPluginManager().disablePlugin(targetPlugin);
                        sender.sendMessage(pre + ChatColor.RED + targetPlugin.getName() + " Disabled!");
                    } else {
                        sender.sendMessage(pre + ChatColor.RED + "Plugin already disabled!");
                    }
                } else {
                    sender.sendMessage(pre + ChatColor.RED + pluginNotFound);
                }
            }
        } else {
            sender.sendMessage(pre + ChatColor.RED + specifyPlugin);
        }
    }

    public void noPerms(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "You do not have permission for that command...");
    }
}
