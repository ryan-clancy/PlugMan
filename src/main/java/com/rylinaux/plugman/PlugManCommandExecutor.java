package com.rylinaux.plugman;

import com.rylinaux.plugman.commands.EnableCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PlugManCommandExecutor implements CommandExecutor {

    private PlugMan plugin;

    public PlugManCommandExecutor(PlugMan plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String command = args[0];

        switch (command) {
            case "enable":
                new EnableCommand().execute(plugin, sender, cmd, label, args);
                break;
            default:
                break;
        }

        return true;
    }

}