package com.rylinaux.plugman.configuration;

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