package com.rylinaux.plugman.contructs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SimpleCommand {

    public final JavaPlugin plugin;

    public final CommandSender sender;

    public final Command command;

    public final String label;

    public final String[] args;

    public final String permission;

    public SimpleCommand(JavaPlugin plugin, CommandSender sender, Command command, String label, String[] args, String permission) {
        this.plugin = plugin;
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args;
        this.permission = permission;
    }

    public boolean hasPermission() {
        return sender.hasPermission(permission);
    }

}
