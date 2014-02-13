package com.rylinaux.plugman;

import com.rylinaux.plugman.listeners.PlugManListener;
import com.rylinaux.plugman.messaging.MessageManager;
import com.rylinaux.plugman.metrics.MetricsHandler;
import com.rylinaux.plugman.updater.UpdaterHandler;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PlugMan extends JavaPlugin {

    private static PlugMan instance = null;

    private List<String> ignoredPlugins = null;

    private MessageManager messageManager = null;

    @Override
    public void onEnable() {

        instance = this;

        messageManager = new MessageManager(this);

        this.getCommand("plugman").setExecutor(new PlugManCommands());
        this.getCommand("plugman").setTabCompleter(new PlugManTabCompleter());

        initConfig();

        initAlerts();
        initMetrics();
        initUpdater();

    }

    @Override
    public void onDisable() {
        instance = null;
        messageManager = null;
        ignoredPlugins = null;
    }

    private void initAlerts() {
        boolean alerts = this.getConfig().getBoolean("update-alerts");
        if (alerts)
            this.getServer().getPluginManager().registerEvents(new PlugManListener(this), this);
    }

    private void initConfig() {
        this.getConfig().options().copyDefaults(true);
        ignoredPlugins = this.getConfig().getStringList("ignored-plugins");
        this.saveConfig();
    }

    private void initMetrics() {
        boolean useMetrics = this.getConfig().getBoolean("use-metrics");
        if (useMetrics) {
            Bukkit.getScheduler().runTask(this, new MetricsHandler(this));
        } else {
            this.getLogger().log(Level.INFO, "Skipping Metrics.");
        }
    }

    private void initUpdater() {
        String updaterType = this.getConfig().getString("updater-type");
        if (!updaterType.equalsIgnoreCase("none")) {
            Bukkit.getScheduler().runTask(this, new UpdaterHandler(this, this.getFile(), updaterType));
        } else {
            this.getLogger().log(Level.INFO, "Skipping Updater.");
        }
    }

    public static PlugMan getInstance() {
        return instance;
    }

    public List<String> getIgnoredPlugins() {
        return ignoredPlugins;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

}