package com.rylinaux.plugman.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Custom configuration implementation with caching.
 *
 * @author rylinaux
 */
public class CustomConfiguration {

    /**
     * The plugin instance.
     */
    private final JavaPlugin plugin;

    /**
     * The cache of config values.
     */
    private final Map<String, String> cache = new HashMap<>();

    /**
     * The name of the file.
     */
    private final String fileName;

    /**
     * The file.
     */
    private File file = null;

    /**
     * The configuration.
     */
    private FileConfiguration customConfig = null;

    /**
     * Constructs our object.
     *
     * @param plugin   the plugin instance
     * @param fileName the file name
     */
    public CustomConfiguration(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        reload();
        copyDefaults();
        cache();
    }

    /**
     * Clears the cache and re-builds it.
     */
    public void cache() {
        cache.clear();
        for (String s : getConfig().getKeys(true)) {
            if (getConfig().get(s) instanceof String)
                cache.put(s, getConfig().getString(s));
        }
    }

    /**
     * Copys the defaults to the file.
     */
    public void copyDefaults() {
        getConfig().options().copyDefaults(true);
        save();
    }

    /**
     * Returns the value associated with the key.
     *
     * @param key the key
     * @return the value
     */
    public String get(String key) {
        return cache.containsKey(key) ? cache.get(key) : customConfig.getString(key);
    }

    /**
     * Get the cache map.
     *
     * @return the cache map
     */
    public Map<String, String> getCache() {
        return cache;
    }

    /**
     * Returns the config.
     *
     * @return the config.
     */
    public FileConfiguration getConfig() {
        if (customConfig == null)
            reload();
        return customConfig;
    }

    /**
     * Reload the config.
     */
    public void reload() {
        if (file == null)
            file = new File(plugin.getDataFolder(), fileName);
        customConfig = YamlConfiguration.loadConfiguration(file);

        InputStream defConfigStream = plugin.getResource(fileName);

        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            customConfig.setDefaults(defConfig);
        }
    }

    /**
     * Save the config.
     */
    public void save() {
        if (customConfig != null && file != null) {
            try {
                getConfig().save(file);
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not save config to " + file, ex);
            }
        }
    }

}