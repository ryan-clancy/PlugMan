package com.rylinaux.plugman.utilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Messaging {

    private JavaPlugin plugin;

    private File file = null;

    private FileConfiguration config = null;

    private String fileName = "messaging.yml";

    private Map<String, String> cache = new HashMap<>();

    public Messaging(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        for (String key : config.getKeys(false)) {
            cache.put(key.toLowerCase(), config.getString(key));
        }
    }

    public void unload() {
        cache.clear();
    }

    public String get(String path) {
        if (cache.keySet().contains(path.toLowerCase())) {
            return cache.get(path);
        }
        return null;
    }

    public void reload() {
        if (file == null) {
            file = new File(plugin.getDataFolder(), fileName);
        }
        config = YamlConfiguration.loadConfiguration(file);
        InputStream defConfigStream = plugin.getResource(fileName);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            config.setDefaults(defConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            reload();
        }
        return config;
    }

    public void save() {
        if (config != null && file != null) {
            try {
                getConfig().save(file);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not save config to " + file, e);
            }
        }
    }

    public void saveDefaults() {
        if (file == null) {
            file = new File(plugin.getDataFolder(), fileName);
        }
        if (!file.exists()) {
            this.plugin.saveResource(fileName, false);
        }
    }
}