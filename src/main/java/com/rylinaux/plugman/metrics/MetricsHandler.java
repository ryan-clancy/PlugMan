package com.rylinaux.plugman.metrics;

import com.rylinaux.plugman.PlugMan;

import java.io.IOException;
import java.util.logging.Level;

import org.mcstats.Metrics;

public class MetricsHandler implements Runnable {

    /**
     * The URL to the Metrics page.
     */
    private static final String METRICS_URL = "http://mcstats.org/plugin/PlugMan";

    /**
     * The plugin instance
     */
    private final PlugMan plugin;

    /**
     * Construct out object
     *
     * @param plugin the plugin instance
     */
    public MetricsHandler(PlugMan plugin) {
        this.plugin = plugin;
    }

    /**
     * Start metrics.
     */
    @Override
    public void run() {
        try {
            Metrics metrics = new Metrics(plugin);
            metrics.start();
            plugin.getLogger().log(Level.INFO, "Metrics started: " + METRICS_URL);
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to start Metrics.");
            e.printStackTrace();
        }
    }

}
