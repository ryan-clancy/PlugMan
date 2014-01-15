package com.rylinaux.plugman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;

public class PlugManTabCompleter implements TabCompleter {

    private static final String[] COMMANDS = {"disable", "enable", "help", "info", "list", "load", "reload", "restart", "test", "unload", "usage"};

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String partialCommand = args[0];
            List<String> commands = new ArrayList<>(Arrays.asList(COMMANDS));
            StringUtil.copyPartialMatches(partialCommand, commands, completions);
        }

        if (args.length == 2) {
            String partial = args[1];
            List<String> plugins = getPluginNames();
            StringUtil.copyPartialMatches(partial, plugins, completions);
        }

        Collections.sort(completions);

        return completions;

    }

    private List<String> getPluginNames() {
        List<String> plugins = new ArrayList<>();
        for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
            plugins.add(plugin.getName());
        }
        return plugins;
    }
}
