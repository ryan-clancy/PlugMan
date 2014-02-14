package com.rylinaux.plugman;

import com.rylinaux.plugman.listeners.PlugManListener;
import com.rylinaux.plugman.messaging.Messenger;
import com.rylinaux.plugman.metrics.MetricsHandler;
import com.rylinaux.plugman.updater.UpdaterHandler;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PlugMan extends JavaPlugin {

    /**
     * The instance of the plugin
     */
    private static PlugMan instance = null;

    /**
     * List of plugins to ignore, partially.
     */
    private List<String> ignoredPlugins = null;

    /**
     * The message manager
     */
    private Messenger messenger = null;

    @Override
    public void onEnable() {

        instance = this;

        messenger = new Messenger(this);

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
        messenger = null;
        ignoredPlugins = null;
    }

    /**
     * Register event for alerts, if enabled.
     */
    private void initAlerts() {
        boolean alerts = this.getConfig().getBoolean("update-alerts");
        if (alerts) {
            this.getServer().getPluginManager().registerEvents(new PlugManListener(this), this);
        }
    }

    /**
     * Copy default config values
     */
    private void initConfig() {
        this.getConfig().options().copyDefaults(true);
        ignoredPlugins = this.getConfig().getStringList("ignored-plugins");
        this.saveConfig();
    }

    /**
     * Start Metrics, if enabled.
     */
    private void initMetrics() {
        boolean useMetrics = this.getConfig().getBoolean("use-metrics");
        if (useMetrics) {
            Bukkit.getScheduler().runTask(this, new MetricsHandler(this));
        } else {
            this.getLogger().log(Level.INFO, "Skipping Metrics.");
        }
    }

    /**
     * Start Updater, if enabled.
     */
    private void initUpdater() {
        String updaterType = this.getConfig().getString("updater-type");
        if (!updaterType.equalsIgnoreCase("none")) {
            Bukkit.getScheduler().runTask(this, new UpdaterHandler(this, this.getFile(), updaterType));
        } else {
            this.getLogger().log(Level.INFO, "Skipping Updater.");
        }
    }

    /**
     * Returns the instance of the plugin.
     *
     * @return the instance of the plugin
     */
    public static PlugMan getInstance() {
        return instance;
    }

    /**
     * Returns the list of ignored plugins.
     *
     * @return the ignored plugins
     */
    public List<String> getIgnoredPlugins() {
        return ignoredPlugins;
    }

    /**
     * Returns the message manager.
     *
     * @return the message manager
     */
    public Messenger getMessenger() {
        return messenger;
    }

}