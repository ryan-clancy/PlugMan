package com.rylinaux.plugman.command;

/*
 * #%L
 * PlugMan
 * %%
 * Copyright (C) 2010 - 2014 PlugMan
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
import com.rylinaux.plugman.util.PluginUtil;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Command that dumps plugin names and versions to file.
 *
 * @author rylinaux
 */
public class DumpCommand extends AbstractCommand {

    /**
     * The name of the command.
     */
    public static final String NAME = "Dump";

    /**
     * The description of the command.
     */
    public static final String DESCRIPTION = "Dump plugins and versions to file.";

    /**
     * The main permission of the command.
     */
    public static final String PERMISSION = "plugman.dump";

    /**
     * The proper usage of the command.
     */
    public static final String USAGE = "/plugman dump";

    /**
     * The sub permissions of the command.
     */
    public static final String[] SUB_PERMISSIONS = {""};

    /**
     * Construct out object.
     *
     * @param sender the command sender
     */
    public DumpCommand(CommandSender sender) {
        super(sender, NAME, DESCRIPTION, PERMISSION, SUB_PERMISSIONS, USAGE);
    }

    /**
     * Executes the command.
     *
     * @param sender  the sender of the command
     * @param command the command being done
     * @param label   the name of the command
     * @param args    the arguments supplied
     */
    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {

        if (!hasPermission()) {
            sender.sendMessage(PlugMan.getInstance().getMessageFormatter().format("error.no-permission"));
            return;
        }

        File dumpFile = new File(PlugMan.getInstance().getDataFolder(), "versions.txt");

        PrintWriter writer = null;

        List<String> plugins = PluginUtil.getPluginNames(true);
        Collections.sort(plugins, String.CASE_INSENSITIVE_ORDER);

        try {
            writer = new PrintWriter(dumpFile);
            for (String plugin : plugins)
                writer.println(plugin);
            sender.sendMessage(PlugMan.getInstance().getMessageFormatter().format("dump.dumped", dumpFile.getName()));
        } catch (IOException e) {
            sender.sendMessage(PlugMan.getInstance().getMessageFormatter().format("dump.error"));
            e.printStackTrace();
        } finally {
            if (writer != null)
                writer.close();
        }

    }

}
