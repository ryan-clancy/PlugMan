package com.rylinaux.plugman.command;

/*
 * #%L
 * PlugMan
 * %%
 * Copyright (C) 2010 - 2015 PlugMan
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

import com.rylinaux.plugman.PlugMan;
import com.rylinaux.plugman.util.BukGetUtil;
import com.rylinaux.plugman.util.PluginUtil;
import com.rylinaux.plugman.util.StringUtil;
import com.rylinaux.plugman.util.ThreadUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Command that checks if a plugin is up-to-date.
 *
 * @author rylinaux
 */
public class CheckCommand extends AbstractCommand {

    /**
     * The name of the command.
     */
    public static final String NAME = "Check";

    /**
     * The description of the command.
     */
    public static final String DESCRIPTION = "Check if a plugin is up-to-date.";

    /**
     * The main permission of the command.
     */
    public static final String PERMISSION = "plugman.check";

    /**
     * The proper usage of the command.
     */
    public static final String USAGE = "/plugman check [plugin]";

    /**
     * The sub permissions of the command.
     */
    public static final String[] SUB_PERMISSIONS = {""};

    /**
     * Construct out object.
     *
     * @param sender the command sender
     */
    public CheckCommand(CommandSender sender) {
        super(sender, NAME, DESCRIPTION, PERMISSION, SUB_PERMISSIONS, USAGE);
    }

    /**
     * Execute the command
     *
     * @param sender  the sender of the command
     * @param command the command being done
     * @param label   the name of the command
     * @param args    the arguments supplied
     */
    @Override
    public void execute(final CommandSender sender, final Command command, final String label, final String[] args) {

        if (!hasPermission()) {
            sender.sendMessage(PlugMan.getInstance().getMessageFormatter().format("error.no-permission"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(PlugMan.getInstance().getMessageFormatter().format("error.specify-plugin"));
            sendUsage();
            return;
        }

        final String pluginName = StringUtil.consolidateStrings(args, 1).replaceAll(" ", "+");

        ThreadUtil.async(new Runnable() {

            @Override
            public void run() {

                final String pluginSlug = BukGetUtil.getPluginSlug(pluginName);

                if (pluginSlug == null || pluginSlug.isEmpty()) {
                    ThreadUtil.sync(new Runnable() {
                        @Override
                        public void run() {
                            sender.sendMessage(PlugMan.getInstance().getMessageFormatter().format("check.not-found-dbo"));
                        }
                    });
                    return;
                }

                JSONObject json = BukGetUtil.getPluginData(pluginSlug);
                JSONArray versions = json.getJSONArray("versions");

                if (versions.length() == 0) {
                    ThreadUtil.sync(new Runnable() {
                        @Override
                        public void run() {
                            sender.sendMessage(PlugMan.getInstance().getMessageFormatter().format("check.not-found-dbo"));
                        }
                    });
                    return;
                }

                final JSONObject latest = versions.getJSONObject(0);

                ThreadUtil.sync(new Runnable() {
                    @Override
                    public void run() {
                        String currentVersion = PluginUtil.getPluginVersion(pluginName);
                        String latestVersion = latest.getString("version");

                        if (currentVersion == null) {
                            sender.sendMessage(PlugMan.getInstance().getMessageFormatter().format("check.not-found", latestVersion));
                        } else if (currentVersion.equalsIgnoreCase(latestVersion)) {
                            sender.sendMessage(PlugMan.getInstance().getMessageFormatter().format("check.up-to-date", currentVersion));
                        } else {
                            sender.sendMessage(PlugMan.getInstance().getMessageFormatter().format("check.outdated", currentVersion, latestVersion));
                        }
                    }
                });


            }

        });

    }

}