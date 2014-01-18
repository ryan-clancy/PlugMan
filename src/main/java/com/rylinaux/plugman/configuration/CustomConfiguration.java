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

public class CustomConfiguration {

    private final JavaPlugin plugin;

    private final Map<String, String> cache = new HashMap<String, String>();

    private final String fileName;

    private File file = null;

    private FileConfiguration customConfig = null;

    public CustomConfiguration(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        reload();
        copyDefaults();
        cache();
    }

    public void cache() {
        cache.clear();
        for (String s : getConfig().getKeys(true)) {
            if (getConfig().get(s) instanceof String)
                cache.put(s, getConfig().getString(s));
        }
    }

    public void copyDefaults() {
        getConfig().options().copyDefaults(true);
        save();
    }

    public String get(String s) {
        if (cache.containsKey(s))
            return cache.get(s);
        else
            return getConfig().getString(s);
    }

    public Map<String, String> getCache() {
        return cache;
    }

    public FileConfiguration getConfig() {
        if (customConfig == null)
            reload();
        return customConfig;
    }

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