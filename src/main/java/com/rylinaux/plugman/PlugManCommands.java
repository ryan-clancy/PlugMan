package com.rylinaux.plugman;

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

import com.rylinaux.plugman.command.DisableCommand;
import com.rylinaux.plugman.command.EnableCommand;
import com.rylinaux.plugman.command.HelpCommand;
import com.rylinaux.plugman.command.InfoCommand;
import com.rylinaux.plugman.command.ListCommand;
import com.rylinaux.plugman.command.LoadCommand;
import com.rylinaux.plugman.command.ReloadCommand;
import com.rylinaux.plugman.command.RestartCommand;
import com.rylinaux.plugman.command.UnloadCommand;
import com.rylinaux.plugman.command.UsageCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Listen for commands and execute them.
 *
 * @author rylinaux
 */
public class PlugManCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            new HelpCommand(sender).execute(sender, command, label, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("disable")) {
            new DisableCommand(sender).execute(sender, command, label, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("enable")) {
            new EnableCommand(sender).execute(sender, command, label, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {
            new InfoCommand(sender).execute(sender, command, label, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            new ListCommand(sender).execute(sender, command, label, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("load")) {
            new LoadCommand(sender).execute(sender, command, label, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("restart")) {
            new RestartCommand(sender).execute(sender, command, label, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            new ReloadCommand(sender).execute(sender, command, label, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("unload")) {
            new UnloadCommand(sender).execute(sender, command, label, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("usage")) {
            new UsageCommand(sender).execute(sender, command, label, args);
            return true;
        }

        new HelpCommand(sender).execute(sender, command, label, args);

        return true;
    }

}
