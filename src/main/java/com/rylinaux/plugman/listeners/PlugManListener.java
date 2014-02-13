package com.rylinaux.plugman.listeners;

import com.rylinaux.plugman.PlugMan;
import com.rylinaux.plugman.updater.UpdaterHandler;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlugManListener implements Listener {

    private final PlugMan plugin;

    public PlugManListener(PlugMan plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(final PlayerJoinEvent event) {
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                if (UpdaterHandler.isUpdateAvailable() && event.getPlayer().hasPermission("plugman.update")) {
                    event.getPlayer().sendMessage(plugin.getMessageManager().format("updater.available"));
                }
            }
        }, 100L);
    }

}
