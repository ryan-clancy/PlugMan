package com.rylinaux.plugman.utilities;

import org.bukkit.ChatColor;

public class Messaging {

    public static final String PREFIX = ChatColor.GRAY + "[" + ChatColor.GREEN + "PlugMan" + ChatColor.GRAY + "] ";

    public static final String UPDATE_AVAILABLE = PREFIX + "An update is available: %s (Current version: %s)";

    public static final String NO_PERMISSION = PREFIX + "You don't have permission for this.";

    public static final String NO_EXIST = PREFIX + "Plugin doesn't exist.";

    public static final String MUST_SPECIFY = PREFIX + "Must specify a plugin.";

    public static final String ENABLED = PREFIX + "%s has been enabled.";

    public static final String DISABLED = PREFIX + "%s has been disabled.";

    public static final String ALREADY_ENABLED = PREFIX + "%s is already enabled.";

    public static final String ALREADY_DISABLED = PREFIX + "%s is already disabled.";

    public static final String LIST = PREFIX + "Plugins: ";

    public static final String RELOADED = PREFIX + "%s has been reloaded.";

    public static final String HELP_HEADER = ChatColor.GRAY + "--------------------- [" + ChatColor.GREEN + " PlugMan " + ChatColor.GRAY + "] ---------------------";

    public static final String HELP_LIST = ChatColor.GREEN + "/plugman list [-v]" + ChatColor.WHITE + " - " + ChatColor.GRAY + "List all plugins.";

    public static final String HELP_INFO = ChatColor.GREEN + "/plugman info [plugin]" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Give plugin info.";

    public static final String HELP_USAGE = ChatColor.GREEN + "/plugman usage [plugin]" + ChatColor.WHITE + " - " + ChatColor.GRAY + "List command plugin has registered.";

    public static final String HELP_STATUS = ChatColor.GREEN + "/plugman status [plugin]" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Give the status of a plugin.";

    public static final String HELP_LOAD = ChatColor.GREEN + "/plugman load [plugin]" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Load a plugin.";

    public static final String HELP_UNLOAD = ChatColor.GREEN + "/plugman unload [plugin]" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Unload a plugin.";

    public static final String HELP_RELOAD = ChatColor.GREEN + "/plugman reload [plugin|all]" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Reload a plugin.";

    public static final String HELP_RESTART = ChatColor.GREEN + "/plugman restart [plugin|all]" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Restart a plugin.";

    public static final String HELP_ENABLE = ChatColor.GREEN + "/plugman enable [plugin|all]" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Enable a plugin.";

    public static final String HELP_DISABLE = ChatColor.GREEN + "/plugman disable [plugin|all]" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Disable a plugin.";


}
