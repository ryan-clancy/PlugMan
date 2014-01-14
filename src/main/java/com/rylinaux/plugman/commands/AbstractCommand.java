package com.rylinaux.plugman.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractCommand {

    /**
     * The command's sender
     */
    private final CommandSender sender;

    /**
     * The command's description
     */
    private final String description;

    /**
     * The command's description
     */
    private final String permission;

    /**
     * The command's usage
     */
    private final String usage;

    /**
     * The sub permissions
     */
    private final String[] subPermissions;

    /**
     * Construct the object
     *
     * @param sender      the sender of the command
     * @param description the description of the command
     * @param permission  the permission to use the command
     * @param usage       the proper usage of the command
     */
    public AbstractCommand(CommandSender sender, String description, String permission, String[] subPermissions, String usage) {
        this.sender = sender;
        this.description = description;
        this.permission = permission;
        this.subPermissions = subPermissions;
        this.usage = usage;
    }

    /**
     * Gets the sender of the command
     *
     * @return the command's sender
     */
    public CommandSender getSender() {
        return sender;
    }

    /**
     * Gets the description of the command
     *
     * @return the command's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the permission associated with the command
     *
     * @return the command's permission
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Gets the proper usage for the command
     *
     * @return the command's usage
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Checks whether the sender has permission to do the command
     *
     * @return does the sender have permission
     */
    public boolean hasPermission() {
        return sender.hasPermission(permission) || isSenderConsole() || isSenderRcon();
    }

    /**
     * Checks whether the sender has permission to do the command
     *
     * @param sub the sub permission to check
     * @return does the sender have permission
     */
    public boolean hasPermission(String sub) {
        String permission = String.format("%s.%s", this.permission, sub);
        return sender.hasPermission(permission) || isSenderConsole() || isSenderRcon();
    }

    /**
     * Checks whether the sender is a player
     *
     * @return is the sender a player
     */
    public boolean isSenderPlayer() {
        return sender instanceof Player;
    }

    /**
     * Checks whether the sender is the console
     *
     * @return is the sender console
     */
    public boolean isSenderConsole() {
        return sender instanceof ConsoleCommandSender;
    }

    /**
     * Checks whether the sender is rcon
     *
     * @return is the sender rcon
     */
    public boolean isSenderRcon() {
        return sender.getName().equalsIgnoreCase("rcon");
    }

    /**
     * Handles the command
     *
     * @param sender  the sender of the command
     * @param command the command being done
     * @param label   the name of the command
     * @param args    the arguements supplied
     */
    public abstract void execute(CommandSender sender, Command command, String label, String[] args);

}
