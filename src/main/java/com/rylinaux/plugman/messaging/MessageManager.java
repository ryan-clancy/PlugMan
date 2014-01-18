package com.rylinaux.plugman.messaging;

import com.rylinaux.plugman.configuration.CustomConfiguration;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageManager {

    private final CustomConfiguration messaging;

    public MessageManager(JavaPlugin plugin) {
        this.messaging = new CustomConfiguration(plugin, "messages.yml");
    }

    public String format(String key, Object... args) {
        return format(true, key, args);
    }

    public String format(boolean prefix, String key, Object... args) {
        String message = prefix ? messaging.get("prefix") + messaging.get(key) : messaging.get(key);
        for (int i = 0; i < args.length; i++) {
            message = message.replace("{" + i + "}", String.valueOf(args[i]));
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public CustomConfiguration getMessaging() {
        return messaging;
    }

}
