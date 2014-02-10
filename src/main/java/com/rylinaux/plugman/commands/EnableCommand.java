package com.rylinaux.plugman.commands;

import com.rylinaux.plugman.PlugMan;
import com.rylinaux.plugman.utilities.PluginUtils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class EnableCommand extends AbstractCommand {

    public static final String NAME = "Enable";

    public static final String DESCRIPTION = "Enable a plugin.";

    public static final String PERMISSION = "plugman.enable";

    public static final String USAGE = "/plugman enable [plugin|all]";

    public static final String[] SUB_PERMISSIONS = {"all"};

    public EnableCommand(CommandSender sender) {
        super(sender, NAME, DESCRIPTION, PERMISSION, SUB_PERMISSIONS, USAGE);
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {

        if (!hasPermission()) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.no-permission"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.specify-plugin"));
            sendUsage();
            return;
        }

        if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("*")) {
            if (hasPermission("all")) {
                PluginUtils.enableAll();
                sender.sendMessage(PlugMan.getInstance().getMessageManager().format("enable.all"));
            } else {
                sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.no-permission"));
            }
            return;
        }

        Plugin target = PluginUtils.getPluginByName(args, 1);

        if (target == null) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.invalid-plugin"));
            sendUsage();
            return;
        }

        if (PluginUtils.isIgnored(target)) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.ignored"));
            return;
        }

        if (target.isEnabled()) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("enable.already-enabled", target.getName()));
            return;
        }

        PluginUtils.enable(target);

        sender.sendMessage(PlugMan.getInstance().getMessageManager().format("enable.enabled", target.getName()));

    }

}