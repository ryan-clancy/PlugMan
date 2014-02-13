package com.rylinaux.plugman.updater;

import com.rylinaux.plugman.PlugMan;

import java.io.File;
import java.util.logging.Level;

import net.gravitydevelopment.updater.Updater;

import org.bukkit.plugin.java.JavaPlugin;

public class UpdaterHandler implements Runnable {

    /**
     * Whether an update is available.
     */
    private static boolean updateAvailable = false;

    /**
     * ID of the DBO project.
     */
    private static final int ID = 36006;

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
