package com.rylinaux.plugman;

import com.rylinaux.plugman.utilities.Updater;

import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import org.mcstats.Metrics;

public class PlugMan extends JavaPlugin {

    private static final String METRICS_URL = "http://mcstats.org/plugin/PlugMan";

    private static final int PROJECT_ID = 36006;

    private boolean updateAvailable = false;

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {

        PlugManConfiguration configuration = new PlugManConfiguration(this, this.getConfig());
        configuration.cache();
        configuration.printCache();

        initConfig();
        initCommands();
        initMetrics();
        initUpdater();

    }

    private void initConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    private void initCommands() {
        this.getCommand("plugman").setExecutor(new PlugManCommandExecutor(this));
    }

    private void initMetrics() {
        boolean useMetrics = this.getConfig().getBoolean("use-metrics");
        if (useMetrics) {
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
                this.getLogger().log(Level.INFO, "Metrics started (%s).", METRICS_URL);
            } catch (IOException e) {
                this.getLogger().log(Level.WARNING, "Metrics failed to start.");
            }
        } else {
            this.getLogger().log(Level.INFO, "Skipping Metrics.");
        }
    }

    private void initUpdater() {
        String updaterType = this.getConfig().getString("updater-type");
        if (!updaterType.equalsIgnoreCase("none")) {
            Updater updater = null;
            boolean showProgress = this.getConfig().getBoolean("update-progress");
            switch (updaterType) {
                case "download":
                    updater = new Updater(this, PROJECT_ID, this.getFile(), Updater.UpdateType.DEFAULT, showProgress);
                    break;
                case "check":
                    updater = new Updater(this, PROJECT_ID, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, showProgress);
                    break;
                case "force":
                    updater = new Updater(this, PROJECT_ID, this.getFile(), Updater.UpdateType.NO_VERSION_CHECK, showProgress);
                    break;
            }
            Updater.UpdateResult result = updater.getResult();
            switch (result) {
                case SUCCESS:
                    this.getLogger().log(Level.INFO, "A new update has been downloaded. Please restart the server for the changes to take effect.");
                    break;
                case NO_UPDATE:
                    this.getLogger().log(Level.INFO, "No update was found.");
                    break;
                case DISABLED:
                    this.getLogger().log(Level.INFO, "Updater did not run, it is globally disabled.");
                    break;
                case FAIL_DOWNLOAD:
                    this.getLogger().log(Level.WARNING, "An update was found, but failed to download.");
                    break;
                case FAIL_DBO:
                    this.getLogger().log(Level.WARNING, "Updater was unable to connect with dev.bukkit.org to look for updates.");
                    break;
                case FAIL_NOVERSION:
                    this.getLogger().log(Level.WARNING, "Invalid version name on dev.bukkit.org - please contact the author.");
                    break;
                case FAIL_BADID:
                    this.getLogger().log(Level.WARNING, "The project ID was invalid or could not be found.");
                    break;
                case FAIL_APIKEY:
                    this.getLogger().log(Level.WARNING, "Invalid API key.");
                    break;
                case UPDATE_AVAILABLE:
                    this.getLogger().log(Level.INFO, "A new update has been found. Please visit \"" + updater.getLatestFileLink() + "\" to download it.");
                    updateAvailable = true;
                    break;
                default:
                    this.getLogger().log(Level.WARNING, "An unexpected result was recieved from Updater.");
                    break;
            }
        } else {
            this.getLogger().log(Level.INFO, "Skipping Updater.");
        }
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }
}