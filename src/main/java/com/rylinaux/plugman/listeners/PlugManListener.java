package com.rylinaux.plugman.listeners;

import com.rylinaux.plugman.PlugMan;
import com.rylinaux.plugman.updater.UpdaterHandler;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Alerts appropriate players of plugin updates.
 *
 * @author rylinaux
 */
public class PlugManListener implements Listener {

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
                if (UpdaterHandler.isUpdateAvailable() && event.getPlayer().hasPermission("plugman.update")) {
                    event.getPlayer().sendMessage(plugin.getMessenger().format("updater.available"));
                }
            }
        }, 100L);
    }

}
