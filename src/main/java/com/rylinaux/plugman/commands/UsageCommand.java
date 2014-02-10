package com.rylinaux.plugman.commands;

import com.rylinaux.plugman.PlugMan;
import com.rylinaux.plugman.utilities.PluginUtils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class UsageCommand extends AbstractCommand {

    public static final String NAME = "Usage";

    public static final String DESCRIPTION = "List commands a plugin has registered.";

    public static final String PERMISSION = "plugman.usage";

    public static final String USAGE = "/plugman usage [plugin]";

    public static final String[] SUB_PERMISSIONS = {""};

    public UsageCommand(CommandSender sender) {
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

        Plugin target = PluginUtils.getPluginByName(args, 1);

        if (target == null) {
            sender.sendMessage(PlugMan.getInstance().getMessageManager().format("error.invalid-plugin"));
            sendUsage();
            return;
        }

        String usages = PluginUtils.getUsages(target);

        sender.sendMessage(PlugMan.getInstance().getMessageManager().format("usage.usage", usages));

    }
}
