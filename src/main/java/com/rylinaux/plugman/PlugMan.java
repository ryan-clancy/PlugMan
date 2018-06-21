package com.rylinaux.plugman;

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

import com.rylinaux.plugman.messaging.MessageFormatter;

import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Plugin manager for Bukkit servers.
 *
 * @author rylinaux
 */
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
    private MessageFormatter messageFormatter = null;

    @Override
    public void onEnable() {

        instance = this;

        messageFormatter = new MessageFormatter();

        this.getCommand("plugman").setExecutor(new PlugManCommandHandler());
        this.getCommand("plugman").setTabCompleter(new PlugManTabCompleter());

        initConfig();

    }

    @Override
    public void onDisable() {
        instance = null;
        messageFormatter = null;
        ignoredPlugins = null;
    }

    /**
     * Copy default config values
     */
    private void initConfig() {
        this.saveDefaultConfig();
        ignoredPlugins = this.getConfig().getStringList("ignored-plugins");
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
    public MessageFormatter getMessageFormatter() {
        return messageFormatter;
    }

}