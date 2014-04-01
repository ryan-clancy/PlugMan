package com.rylinaux.plugman.metrics;

/*
 * #%L
 * PlugMan
 * %%
 * Copyright (C) 2010 - 2014 PlugMan
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import org.mcstats.Metrics;

/**
 * Start Metrics.
 *
 * @author rylinaux
 */
public class MetricsHandler implements Runnable {

    /**
     * The URL to the Metrics page.
     */
    private static final String METRICS_URL = "http://mcstats.org/plugin/PlugMan";

    /**
     * The plugin instance
     */
    private final JavaPlugin plugin;

    /**
     * Construct out object
     *
     * @param plugin the plugin instance
     */
    public MetricsHandler(JavaPlugin plugin) {
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
