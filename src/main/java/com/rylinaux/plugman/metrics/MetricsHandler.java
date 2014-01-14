package com.rylinaux.plugman.metrics;

import com.rylinaux.plugman.PlugMan;

import java.io.IOException;
import java.util.logging.Level;

import org.mcstats.Metrics;

public class MetricsHandler {

    private static final String METRICS_URL = "http://mcstats.org/plugin/PlugMan";

    private final PlugMan plugin;

    public MetricsHandler(PlugMan plugin) {
        this.plugin = plugin;
    }

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
