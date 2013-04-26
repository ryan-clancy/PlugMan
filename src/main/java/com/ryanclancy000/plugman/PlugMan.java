package com.ryanclancy000.plugman;

import com.ryanclancy000.plugman.utilities.MetricsLite;
import com.ryanclancy000.plugman.utilities.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

public class PlugMan extends JavaPlugin {

    private final Utilities utils = new Utilities(this);
    private final List<String> skipPlugins = new ArrayList<String>();
    
    @Override
    public void onDisable() {
        skipPlugins.clear();
    }

    @Override
    public void onEnable() {
        initConfig();
        initCommands();
        initMetrics();
    }

    private void initConfig() {
        try {
            getConfig().options().copyDefaults(true);
            for (String s : getConfig().getStringList("skip-on-reload")) {
                skipPlugins.add(s);
            }
            saveConfig();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to load config - ignoring skip-plugins feature!{0}", e);
            skipPlugins.clear();
        }
    }

    private void initCommands() {
        getCommand("plugman").setExecutor(new PlugManCommands(this));
    }

    private void initMetrics() {
        if (getConfig().getBoolean("use-metrics")) {
            try {
                new MetricsLite(this).start();
                getLogger().log(Level.INFO, "Metrics successfully started!");
            }  catch (final IOException e) {
                getLogger().log(Level.SEVERE, "Failed to start Metrics!{0}", e);
            }
        } else {
            getLogger().log(Level.INFO, "Ignoring Metrics!");
        }
    }

    public List<String> getSkipped() {
        return skipPlugins;
    }

    public Utilities getUtils() {
        return utils;
    }
}