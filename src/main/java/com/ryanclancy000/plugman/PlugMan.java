package com.ryanclancy000.plugman;

import com.ryanclancy000.plugman.utilities.MetricsLite;
import com.ryanclancy000.plugman.utilities.Utilities;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;

public class PlugMan extends JavaPlugin {

    private List<String> skipPlugins;
    private final Utilities utils = new Utilities(this);
    
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
            skipPlugins = this.getConfig().getStringList("skip-on-reload");
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
        if (this.getConfig().getBoolean("use-metrics")) {
            try {
                new MetricsLite(this).start();
                this.getLogger().log(Level.INFO, "Metrics successfully started!");
            } catch (Exception e) {
                this.getLogger().log(Level.SEVERE, "Failed to start Metrics!{0}", e);
            }
        } else {
            this.getLogger().log(Level.INFO, "Ignoring Metrics!");
        }
    }

    public List<String> getSkipped() {
        return skipPlugins;
    }
    
    public Utilities getUtils() {
        return utils;
    }
}