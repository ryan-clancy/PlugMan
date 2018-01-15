package com.rylinaux.plugman.listener;

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

import com.rylinaux.plugman.PlugMan;
import com.rylinaux.plugman.PlugManTabCompleter;
import com.rylinaux.plugman.task.UpdaterTask;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Alerts appropriate players of plugin updates.
 *
 * @author rylinaux
 */
public class PlugManListener implements Listener {

    public static final Pattern PLUGIN_CMD_PATTERN = Pattern.compile("^/?pl(?:ugins)? ");
    /**
     * The plugin instance.
     */
    private final PlugMan plugin;

    /**
     * Construct out object.
     *
     * @param plugin the plugin instance
     */
    public PlugManListener(PlugMan plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(final PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                if (UpdaterTask.isUpdateAvailable() && event.getPlayer().hasPermission("plugman.update")) {
                    event.getPlayer().sendMessage(plugin.getMessageFormatter().format("updater.available"));
                }
            }
        }, 100L);
    }

    @EventHandler
    public void onConsoleCommandPreprocess(ServerCommandEvent e) {
        if (plugin.isCommandOverrideEnabled()) {
            String newCommand = getNewCommand(e.getCommand());
            if (newCommand != null) e.setCommand(newCommand);
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        if (plugin.isCommandOverrideEnabled()) {
            String newCommand = getNewCommand(e.getMessage().substring(1));
            if (newCommand != null) e.setMessage("/" + newCommand);
        }
    }

    private String getNewCommand(String command) {
        if (command.toLowerCase().startsWith("pl ")) {
            return "plugman" + command.substring(2);
        } else if (command.toLowerCase().startsWith("plugins ")) {
            return "plugman" + command.substring(7);
        } else if (command.equalsIgnoreCase("pl") || command.equalsIgnoreCase("plugins")) {
            return "plugman list";
        } else {
            return null;
        }
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent e) {
        if (plugin.isCommandOverrideEnabled()) {
            if (e.getSender() instanceof Player && !e.getBuffer().startsWith("/")) return;

            String buffer = e.getBuffer();

            if (buffer.startsWith("/")) buffer = buffer.substring(1);

            if (!buffer.startsWith("pl ") && !buffer.startsWith("plugins ")) return;

            PlugManTabCompleter tab = new PlugManTabCompleter();
            String[] args = buffer.split(" ");
            List<String> completions = tab.onTabComplete(e.getSender(), null, args[0], Arrays.copyOfRange(args, 1, args.length));

            e.setCompletions(completions);
        }
    }

}
