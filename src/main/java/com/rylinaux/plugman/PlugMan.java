/** TODO: Maven checkstyle, maven license **/

package com.rylinaux.plugman;

import com.rylinaux.plugman.utilities.ConfigurationManager;
import com.rylinaux.plugman.utilities.Messaging;
import com.rylinaux.plugman.utilities.Profiler;
import com.rylinaux.plugman.utilities.Updater;

import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import org.mcstats.Metrics;

public class PlugMan extends JavaPlugin {

    private static final String METRICS_URL = "http://mcstats.org/plugin/PlugMan";

    private static final String SLUG = "plugman";

    private ConfigurationManager configurationManager = null;

    private Messaging messaging = null;

    @Override
    public void onDisable() {
        configurationManager.unload();
    }

    @Override
    public void onEnable() {

        Profiler profiler = new Profiler("Config");
        profiler.start();

        configurationManager = new ConfigurationManager(this, this.getConfig());
        configurationManager.load();

        initCommands();
        initMetrics();
        initUpdater();

        profiler.end();
        log(Level.INFO, profiler.toString());

    }

    private void initCommands() {
        this.getCommand("plugman").setExecutor(new PlugManCommandExecutor(this));
    }

    private void initMetrics() {
        boolean useMetrics = (boolean) configurationManager.get("use-metrics");
        if (useMetrics) {
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
                log(Level.INFO, "Metrics started (%s).", METRICS_URL);
            } catch (IOException e) {
                log(Level.WARNING, "Metrics failed to start.");
            }
        } else {
            log(Level.INFO, "Skipping Metrics.");
        }
    }

    private void initUpdater() {
        String updaterType = (String) configurationManager.get("updater-type");
        if (!updaterType.equalsIgnoreCase("none")) {
            Updater updater = null;
            boolean showProgress = this.getConfig().getBoolean("update-progress");
            switch (updaterType) {
                case "download":
                    updater = new Updater(this, SLUG, this.getFile(), Updater.UpdateType.DEFAULT, showProgress);
                    break;
                case "check":
                    updater = new Updater(this, SLUG, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, showProgress);
                    break;
                case "force":
                    updater = new Updater(this, SLUG, this.getFile(), Updater.UpdateType.NO_VERSION_CHECK, showProgress);
                    break;
            }
            Updater.UpdateResult result = updater.getResult();
            switch (result) {
                case SUCCESS:
                    log(Level.INFO, "Updater successfully downloaded new version - restart server for changes to apply.");
            }
        } else {
            log(Level.INFO, "Skipping Updater.");
        }
    }

    private void log(Level level, String message) {
        log(level, message, null);
    }

    private void log(Level level, String message, Object... args) {
        this.getLogger().log(level, String.format(message, args));
    }

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }
}