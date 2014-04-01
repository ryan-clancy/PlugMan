package com.rylinaux.plugman.updater;

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

import java.io.File;
import java.util.logging.Level;

import net.gravitydevelopment.updater.Updater;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Start the Updater.
 *
 * @author rylinaux
 */
public class UpdaterHandler implements Runnable {

    /**
     * ID of the DBO project.
     */
    private static final int ID = 36006;

    /**
     * Whether an update is available.
     */
    private static boolean updateAvailable = false;

    /**
     * The instance of the plugin.
     */
    private final JavaPlugin plugin;

    /**
     * The plugin's file.
     */
    private final File file;

    /**
     * The type of Updater to use.
     */
    private final String updaterType;

    /**
     * Construct our object.
     *
     * @param plugin      the plugin instance
     * @param file        the plugin's file
     * @param updaterType the type of updater
     */
    public UpdaterHandler(JavaPlugin plugin, File file, String updaterType) {
        this.plugin = plugin;
        this.file = file;
        this.updaterType = updaterType;
    }

    /**
     * Run the updater.
     */
    @Override
    public void run() {

        Updater.UpdateType type;

        switch (updaterType.toLowerCase()) {
            case "download":
                type = Updater.UpdateType.DEFAULT;
                break;
            case "check":
                type = Updater.UpdateType.NO_DOWNLOAD;
                break;
            case "force":
                type = Updater.UpdateType.NO_VERSION_CHECK;
                break;
            default:
                type = Updater.UpdateType.DEFAULT;
        }

        Updater updater = new Updater(plugin, ID, file, type, true);

        Updater.UpdateResult result = updater.getResult();

        switch (result) {
            case SUCCESS:
                plugin.getLogger().log(Level.INFO, "A new update has been downloaded. Please restart the server for the changes to take effect.");
                break;
            case NO_UPDATE:
                plugin.getLogger().log(Level.INFO, "No update was found.");
                break;
            case DISABLED:
                plugin.getLogger().log(Level.INFO, "Updater did not run, it is globally disabled.");
                break;
            case FAIL_DOWNLOAD:
                plugin.getLogger().log(Level.WARNING, "An update was found, but failed to download.");
                break;
            case FAIL_DBO:
                plugin.getLogger().log(Level.WARNING, "Updater was unable to connect with dev.bukkit.org to look for updates.");
                break;
            case FAIL_NOVERSION:
                plugin.getLogger().log(Level.WARNING, "Invalid version name on dev.bukkit.org - please contact the author.");
                break;
            case FAIL_BADID:
                plugin.getLogger().log(Level.WARNING, "The project ID was invalid or could not be found.");
                break;
            case FAIL_APIKEY:
                plugin.getLogger().log(Level.WARNING, "Invalid API key.");
                break;
            case UPDATE_AVAILABLE:
                plugin.getLogger().log(Level.INFO, "A new update has been found. Please visit \"" + updater.getLatestFileLink() + "\" to download it.");
                updateAvailable = true;
                break;
            default:
                plugin.getLogger().log(Level.WARNING, "An unexpected result was recieved from Updater.");
        }

    }

    /**
     * Returns whether an update is available.
     *
     * @return whether an update is available
     */
    public static boolean isUpdateAvailable() {
        return updateAvailable;
    }

}
