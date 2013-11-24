package com.rylinaux.plugman;

import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;

public class PlugManConfiguration {

    private final PlugMan plugin;

    private final FileConfiguration configuration;

    private final HashMap<String, Object> cache = new HashMap<String, Object>();

    public PlugManConfiguration(PlugMan plugin, FileConfiguration configuration) {
        this.plugin = plugin;
        this.configuration = configuration;
    }

    public void cache() {

        cache.clear();

        for (String s : configuration.getKeys(false)) {
            cache.put(s, configuration.get(s));
        }

    }

    public void printCache() {
        for (String s : cache.keySet()) {
            System.out.println("********** " + s + " : " + cache.get(s));
        }
    }

}
