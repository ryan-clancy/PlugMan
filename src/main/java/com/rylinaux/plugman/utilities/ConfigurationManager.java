package com.rylinaux.plugman.utilities;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigurationManager {

    private JavaPlugin plugin = null;

    private FileConfiguration config = null;

    private File configFile = null;

    private Map<String, Object> cache = new HashMap<>();

    public ConfigurationManager(JavaPlugin plugin, FileConfiguration config) {
        this.plugin = plugin;
        this.config = config;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
        copyDefaults();
    }

    private void copyDefaults() {
        config.options().copyDefaults(true);
        save();
    }

    public void load() {
        for (String key : config.getKeys(false)) {
            cache.put(key.toLowerCase(), config.get(key));
        }
    }

    public void unload() {
        cache.clear();
    }

    public void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, String.format("Could not save config file to: %s", configFile.getPath()));
        }
    }

    public Object get(String path) {
        if (cache.keySet().contains(path.toLowerCase())) {
            return cache.get(path);
        }
        return null;
    }

    public void set(String path, Object value) {
        config.set(path, value);
        save();
        cache.put(path, value);
    }
}