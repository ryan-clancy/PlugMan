package com.rylinaux.plugman;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import org.mcstats.Metrics;

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
            this.getConfig().options().copyDefaults(true);
            skipPlugins.addAll(this.getConfig().getStringList("skip-on-reload"));
            this.saveConfig();
        } catch (Exception e) {
            this.getLogger().log(Level.SEVERE, "Failed to load config - ignoring skip-plugins feature!{0}", e);
            skipPlugins.clear();
        }
    }

    private void initCommands() {
        this.getCommand("plugman").setExecutor(new PlugManCommands(this));
    }

    private void initMetrics() {
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
            this.getLogger().log(Level.INFO, "Metrics successfully started!");
        } catch (IOException e) {
            this.getLogger().log(Level.SEVERE, "Failed to start Metrics!{0}", e);
        }
    }

    public List<String> getSkipped() {
        return skipPlugins;
    }

    public Utilities getUtils() {
        return utils;
    }
}