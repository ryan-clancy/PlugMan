package com.rylinaux.plugman;

import com.rylinaux.plugman.utilities.Messaging;
import com.rylinaux.plugman.utilities.Permissions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlugManListener implements Listener {

    private PlugMan plugin;

    public PlugManListener(PlugMan plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (plugin.isUpdateAvailable()) {
            Player player = event.getPlayer();
            if (player.hasPermission(Permissions.UPDATE)) {
                player.sendMessage(Messaging.UPDATE_AVAILABLE);
            }
        }
    }
}