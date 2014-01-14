package com.rylinaux.plugman;

import com.rylinaux.plugman.commands.EnableCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PlugManCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args[0].equalsIgnoreCase("enable")) {
            new EnableCommand(sender).execute(sender, command, label, args);
        }

        return true;
    }

}
