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

import com.rylinaux.plugman.command.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Listen for commands and execute them.
 *
 * @author rylinaux
 */
public class PlugManCommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        AbstractCommand cmd = new HelpCommand(sender);

        // No args, show help.
        if (args.length == 0) {
            cmd.execute(sender, command, label, args);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help":
                cmd = new HelpCommand(sender);
                break;
            case "list":
                cmd = new ListCommand(sender);
                break;
            case "dump":
                cmd = new DumpCommand(sender);
                break;
            case "info":
                cmd = new InfoCommand(sender);
                break;
            case "lookup":
                cmd = new LookupCommand(sender);
                break;
            case "usage":
                cmd = new UsageCommand(sender);
                break;
            case "enable":
                cmd = new EnableCommand(sender);
                break;
            case "disable":
                cmd = new DisableCommand(sender);
                break;
            case "restart":
                cmd = new RestartCommand(sender);
                break;
            case "load":
                cmd = new LoadCommand(sender);
                break;
            case "reload":
                cmd = new ReloadCommand(sender);
                break;
            case "unload":
                cmd = new UnloadCommand(sender);
                break;
            case "check":
                cmd = new CheckCommand(sender);
                break;
        }

        cmd.execute(sender, command, label, args);
        return true;

    }

}
