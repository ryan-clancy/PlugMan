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
    //
    public static final ChatColor red = ChatColor.RED;
    public static final ChatColor gray = ChatColor.GRAY;
    public static final ChatColor white = ChatColor.WHITE;
    public static final ChatColor green = ChatColor.GREEN;
    public static final ChatColor yellow = ChatColor.YELLOW;
    //
    public static final String pre = gray + "[" + green + "PlugMan" + gray + "] ";
    private static final String tooMany = red + "Too many arguments!";
    private static final String specifyPlugin = red + "Must specify a plugin!";
    private static final String pluginNotFound = red + "Plugin not found!";

    public Utilities(PlugMan plugin) {
        this.plugin = plugin;
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

    // PlugMan Command
    public void thisInfo(CommandSender sender) {
        sender.sendMessage(pre + green + "v" + plugin.getDescription().getVersion() + gray + " by " + green + "ryanclancy000");
        sender.sendMessage(gray + "-" + green + " To view commands, do /plugman help");
    }

    // Help Command
    public void helpCommand(CommandSender sender) {
        sender.sendMessage(white + "--------------------- " + gray + "[" + green + " PlugMan " + gray + "]" + white + " ---------------------");
        if (sender.hasPermission("plugman.list")) {
            sender.sendMessage(green + "/plugman list" + white + " - " + gray + "List all plugins.");
        }
        if (sender.hasPermission("plugman.vlist")) {
            sender.sendMessage(green + "/plugman vlist" + white + " - " + gray + "List all plugins with versions.");
        }
        if (sender.hasPermission("plugman.info")) {
            sender.sendMessage(green + "/plugman info [plugin]" + white + " - " + gray + "Give plugin info.");
        }
        if (sender.hasPermission("plugman.usage")) {
            sender.sendMessage(green + "/plugman usage [plugin]" + white + " - " + gray + "List command plugin has registered.");
        }
        if (sender.hasPermission("plugman.status")) {
            sender.sendMessage(green + "/plugman status [plugin]" + white + " - " + gray + "Give the status of a plugin.");
        }
        if (sender.hasPermission("plugman.test")) {
            sender.sendMessage(green + "/plugman test [permission] [player]" + white + " - " + gray + "Test a permission node.");
        }
        if (sender.hasPermission("plugman.load")) {
            sender.sendMessage(green + "/plugman load [plugin]" + white + " - " + gray + "Load a plugin.");
        }
        if (sender.hasPermission("plugman.unload")) {
            sender.sendMessage(green + "/plugman unload [plugin]" + white + " - " + gray + "Unload a plugin.");
        }
        if (sender.hasPermission("plugman.reload")) {
            sender.sendMessage(green + "/plugman reload [plugin|all]" + white + " - " + gray + "Reload a plugin.");
        }
        if (sender.hasPermission("plugman.restart")) {
            sender.sendMessage(green + "/plugman restart [plugin|all]" + white + " - " + gray + "Restart a plugin.");
        }
        if (sender.hasPermission("plugman.enable")) {
            sender.sendMessage(green + "/plugman enable [plugin|all]" + white + " - " + gray + "Enable a plugin.");
        }
        if (sender.hasPermission("plugman.disable")) {
            sender.sendMessage(green + "/plugman disable [plugin|all]" + white + " - " + gray + "Disable a plugin.");
        }
    }

    // List Command
    public void listCommand(CommandSender sender, String[] args) {

        if (args.length > 1) {
            sender.sendMessage(pre + red + tooMany);
        } else {

            String pluginName = "";
            StringBuilder list = new StringBuilder();
            List<String> pluginList = new ArrayList<String>();

            for (Plugin pl : plugin.getServer().getPluginManager().getPlugins()) {
                pluginName = (pl.isEnabled() ? green : red) + pl.getDescription().getName();
                pluginList.add(pluginName);
            }
            Collections.sort(pluginList, String.CASE_INSENSITIVE_ORDER);
            for (String name : pluginList) {
                if (list.length() > 0) {
                    list.append(white).append(", ");
                }
                list.append(name);
            }
            sender.sendMessage(pre + gray + "Plugins: " + list);
        }
    }

    // vList Command
    public void vlistCommand(CommandSender sender, String[] args) {

        if (args.length > 1) {
            sender.sendMessage(pre + red + tooMany);
        } else {

            String pluginName = "";
            StringBuilder list = new StringBuilder();
            List<String> pluginList = new ArrayList<String>();

            for (Plugin pl : plugin.getServer().getPluginManager().getPlugins()) {
                pluginName = (pl.isEnabled() ? green : red) + pl.getDescription().getFullName();
                pluginList.add(pluginName);
            }
            Collections.sort(pluginList, String.CASE_INSENSITIVE_ORDER);
            for (String name : pluginList) {
                if (list.length() > 0) {
                    list.append(white).append(", ");
                }
                list.append(name);
            }
            sender.sendMessage(pre + gray + "Plugins: " + list);
        }
    }

    // Info Command
    public void infoCommand(CommandSender sender, String[] args) {

        if (args.length == 1) {
            sender.sendMessage(pre + red + specifyPlugin);
            return;
        }

        Plugin targetPlugin = getPlugin(consolidateArgs(args));

        if (targetPlugin == null) {
            sender.sendMessage(pre + red + pluginNotFound);
        } else {
            sender.sendMessage(pre + gray + "Plugin Info: " + green + targetPlugin.getName());
            sender.sendMessage(green + "Version: " + gray + targetPlugin.getDescription().getVersion());
            sender.sendMessage(green + "Authors: " + gray + targetPlugin.getDescription().getAuthors());
            sender.sendMessage(green + "Status: " + (targetPlugin.isEnabled() ? green + "Enabled" : red + "Disabled"));
        }
    }

    // Status Command
    public void statusCommand(CommandSender sender, String[] args) {

        if (args.length == 1) {
            sender.sendMessage(pre + red + specifyPlugin);
            return;
        }

        Plugin targetPlugin = getPlugin(consolidateArgs(args));

        if (targetPlugin == null) {
            sender.sendMessage(pre + red + pluginNotFound);
        } else if (targetPlugin.isEnabled()) {
            sender.sendMessage(pre + green + targetPlugin.getName() + " is enabled!");
        } else {
            sender.sendMessage(pre + green + targetPlugin.getName() + " is disabled!");
        }
    }

    // Usage Command
    public void usageCommand(CommandSender sender, String[] args) {

        if (args.length == 1) {
            sender.sendMessage(pre + red + specifyPlugin);
            return;
        }

        Plugin targetPlugin = getPlugin(consolidateArgs(args));

        if (targetPlugin == null) {
            sender.sendMessage(pre + red + pluginNotFound);
        } else {
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
                commandsOut.append(pre).append(gray).append("Command List: ");

                for (int i = 0; i < parsedCommands.size(); i++) {

                    String thisCommand = parsedCommands.get(i);

                    if (commandsOut.length() + thisCommand.length() > 55) {
                        sender.sendMessage(commandsOut.toString());
                        commandsOut = new StringBuilder();
                    }

                    if (parsedCommands.size() > 0) {
                        commandsOut.append(green).append("\"").append(thisCommand).append("\" ");
                    } else {
                        commandsOut.append(green).append("\"").append(thisCommand).append("\"");
                    }

                }

                out.add(commandsOut.toString());

            } else {
                out.add(pre + red + "Plugin has no registered commands!");
            }

            for (String s : out) {
                sender.sendMessage(s);
            }
        }
    }

    // Test Command
    public void testCommand(CommandSender sender, String[] args) {

        if (args.length == 1) {
            sender.sendMessage(pre + red + "Must specify permission and player!");
        } else if (args.length == 2) {
            if (sender.hasPermission(args[1])) {
                sender.sendMessage(pre + green + "You have permission for '" + args[1] + "'.");
            } else {
                sender.sendMessage(pre + red + "You do not have permission for '" + args[1] + "'.");
            }
        } else if (args.length == 3) {
            Player target = plugin.getServer().getPlayer(args[2]);
            if (target == null) {
                sender.sendMessage(pre + red + "Player not found!");
            } else {
                if (target.hasPermission(args[1])) {
                    sender.sendMessage(pre + green + target.getName() + " has permission for " + args[1]);
                } else {
                    sender.sendMessage(pre + red + target.getName() + " does not have permission for " + args[1]);
                }
            }
        } else {
            sender.sendMessage(pre + red + "Invalid arguments!");
        }
    }

    // Load Command
    public void loadCommand(CommandSender sender, String[] args) {

        if (args.length == 1) {
            sender.sendMessage(pre + red + specifyPlugin);
            return;
        }

        String pl = consolidateArgs(args);
        Plugin targetPlugin = getPlugin(pl);

        if (targetPlugin != null) {
            if (targetPlugin.isEnabled()) {
                sender.sendMessage(pre + red + "Plugin already loaded and enabled!");
                return;
            } else {
                sender.sendMessage(pre + red + "Plugin already loaded, but is disabled!");
                return;
            }
        }
        sender.sendMessage(loadPlugin(pl));
    }

    //Plugin Loading
    private String loadPlugin(String pl) {

        Plugin targetPlugin = null;
        String msg = "";
        File pluginDir = new File("plugins");
        if (!pluginDir.isDirectory()) {return(pre + red + "Plugin directory not found!");}
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
                    return(pre + red + "Couldn't find file and failed to search descriptions!");
                }
            }
        }
        try {
            plugin.getServer().getPluginManager().loadPlugin(pluginFile);
            targetPlugin = getPlugin(pl);
            targetPlugin.onLoad();
            plugin.getServer().getPluginManager().enablePlugin(targetPlugin);
            return(pre + green + getPlugin(pl) + " loaded " + msg + "and enabled!");
        } catch (UnknownDependencyException e) {
            return(pre + red + "File exists, but is missing a dependency!");
        } catch (InvalidPluginException e) {
            plugin.getLogger().log(Level.SEVERE, "Tried to load invalid Plugin.\n", e);
            return(pre + red + "File exists, but isn't a loadable plugin file!");
        } catch (InvalidDescriptionException e) {
            return(pre + red + "Plugin exists, but has an invalid description!");
        }
    }

    // Unload Command
    public void unloadCommand(CommandSender sender, String[] args) {

        if (args.length == 1) {
            sender.sendMessage(pre + red + specifyPlugin);
            return;
        }

        String pl = consolidateArgs(args);
        Plugin targetPlugin = getPlugin(pl);

        if (targetPlugin == null) {
            sender.sendMessage(pre + red + pluginNotFound);
        } else {
            sender.sendMessage(unloadPlugin(pl));
        }
    }

    //Plugin unloading
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
            } catch (NoSuchFieldException | IllegalAccessException e) {
                return(pre + red + "Failed to unload plugin!");
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
        return(pre + green + tp + "has been unloaded and disabled!");
    }


    // Reload Command
    public void reloadCommand(CommandSender sender, String[] args) {

        if (args.length == 1) {
            sender.sendMessage(pre + red + specifyPlugin);
            return;
        }

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
            sender.sendMessage(pre + green + "All plugins reloaded!");
            return;
        }

        String pl = consolidateArgs(args);

        if (getPlugin(pl) == null) {
            sender.sendMessage(pre + red + pluginNotFound);
            return;
        }

        sender.sendMessage(unloadPlugin(pl));
        sender.sendMessage(loadPlugin(pl));

    }


    // Restart Command
    public void restartCommand(CommandSender sender, String[] args) {

        if (args.length == 1) {
            sender.sendMessage(pre + red + specifyPlugin);
            return;
        }

        if ("all".equalsIgnoreCase(args[1]) || "*".equalsIgnoreCase(args[1])) {
            for (Plugin pl : plugin.getServer().getPluginManager().getPlugins()) {
                if (plugin.getSkipped().contains(pl.getName()) || plugin.getSkipped().isEmpty()) {
                    continue;
                } else {
                    plugin.getServer().getPluginManager().disablePlugin(pl);
                    plugin.getServer().getPluginManager().enablePlugin(pl);
                }
            }
            sender.sendMessage(pre + green + "All plugins restarted!");
            return;
        }

        String pl = consolidateArgs(args);

        if (getPlugin(pl) == null) {
            sender.sendMessage(pre + red + pluginNotFound);
            return;
        }

        Plugin targetPlugin = getPlugin(pl);
        plugin.getServer().getPluginManager().disablePlugin(targetPlugin);
        plugin.getServer().getPluginManager().enablePlugin(targetPlugin);
        sender.sendMessage(pre + green + targetPlugin.getName() + " Restarted!");

    }

    // Enable Command
    public void enableCommand(CommandSender sender, String[] args) {

        if (args.length == 1) {
            sender.sendMessage(pre + red + specifyPlugin);
            return;
        }

        if ("all".equalsIgnoreCase(args[1]) || "*".equalsIgnoreCase(args[1])) {
            for (Plugin pl : plugin.getServer().getPluginManager().getPlugins()) {
                plugin.getServer().getPluginManager().enablePlugin(pl);
            }
            sender.sendMessage(pre + green + "All plugins enabled!");
            return;
        }

        String pl = consolidateArgs(args);

        if (getPlugin(pl) == null) {
            sender.sendMessage(pre + red + pluginNotFound);
            return;
        }

        if (getPlugin(pl).isEnabled()) {
            sender.sendMessage(pre + red + "Plugin already enabled!");
            return;
        }

        Plugin targetPlugin = getPlugin(pl);
        plugin.getServer().getPluginManager().enablePlugin(targetPlugin);
        sender.sendMessage(pre + green + targetPlugin.getName() + " Enabled!");

    }

    // Disable Command
    public void disableCommand(CommandSender sender, String[] args) {

        if (args.length == 1) {
            sender.sendMessage(pre + red + specifyPlugin);
            return;
        }

        if ("all".equalsIgnoreCase(args[1]) || "*".equalsIgnoreCase(args[1])) {
            for (Plugin pl : plugin.getServer().getPluginManager().getPlugins()) {
                plugin.getServer().getPluginManager().disablePlugin(pl);
            }
            sender.sendMessage(pre + red + "All plugins disabled!");
            return;
        }

        String pl = consolidateArgs(args);

        if (getPlugin(pl) == null) {
            sender.sendMessage(pre + red + pluginNotFound);
            return;
        }

        if (!getPlugin(pl).isEnabled()) {
            sender.sendMessage(pre + red + "Plugin already disabled!");
            return;
        }

        Plugin targetPlugin = getPlugin(pl);
        plugin.getServer().getPluginManager().disablePlugin(targetPlugin);
        sender.sendMessage(pre + red + targetPlugin.getName() + " Disabled!");

    }

    public void noPerms(CommandSender sender) {
        sender.sendMessage(red + "You do not have permission for that command...");
    }
}
