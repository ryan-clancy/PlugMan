package com.rylinaux.plugman.messaging;

import com.rylinaux.plugman.configuration.CustomConfiguration;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Manages custom messaging.
 *
 * @author rylinaux
 */
public class Messenger {

    /**
     * The configuration file.
     */
    private final CustomConfiguration messaging;

    /**
     * Construct our object.
     *
     * @param plugin the plugin instance
     */
    public Messenger(JavaPlugin plugin) {
        this.messaging = new CustomConfiguration(plugin, "messages.yml");
    }

    /**
     * Returns the formatted version of the message.
     *
     * @param key  the key
     * @param args the args to replace
     * @return the formatted String
     */
    public String format(String key, Object... args) {
        return format(true, key, args);
    }

    /**
     * Returns the formatted version of the message.
     *
     * @param prefix whether to prepend with the plugin's prefix
     * @param key    the key
     * @param args   the args to replace
     * @return the formatted String
     */
    public String format(boolean prefix, String key, Object... args) {
        String message = prefix ? messaging.get("prefix") + messaging.get(key) : messaging.get(key);
        for (int i = 0; i < args.length; i++) {
            message = message.replace("{" + i + "}", String.valueOf(args[i]));
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Returns the messaging configuration.
     *
     * @return the messaging configuration.
     */
    public CustomConfiguration getMessaging() {
        return messaging;
    }

}
