package com.rylinaux.plugman.updater;

import com.rylinaux.plugman.PlugMan;

import java.io.File;
import java.util.logging.Level;

import net.gravitydevelopment.updater.Updater;

public class UpdaterHandler implements Runnable {

    /**
     * Whether an update is available or .
     */
    private static boolean updateAvailable = false;

    /**
     * The instance of the plugin.
     */
    private final PlugMan plugin;

    /**
     * ID of the DBO project.
     */
    private final int id;

    /**
     * The plugin's file.
     */
    private final File file;

    /**
     * The type of Updater to use.
     */
    private final String updaterType;

    /**
     * Whether to make the output verbose.
     */
    private final boolean verbose;

    /**
     * Construct our object.
     *
     * @param plugin      the plugin instance
     * @param id          the id of the dbo project
     * @param file        the plugin's file
     * @param updaterType the type of updater
     * @param verbose     whether output if verbose
     */
    public UpdaterHandler(PlugMan plugin, int id, File file, String updaterType, boolean verbose) {
        this.plugin = plugin;
        this.id = id;
        this.file = file;
        this.updaterType = updaterType;
        this.verbose = verbose;
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

        Updater updater = new Updater(plugin, id, file, type, verbose);

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
