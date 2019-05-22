package xyz.turpster.plugman.command;

import com.rylinaux.plugman.command.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Command that removes specified plugin(s).
 *
 * @author turpster
 */
public class RemoveCommand extends AbstractCommand
{
    /**
     * The name of the command.
     */
    public static final String NAME = "remove";

    /**
     * The description of the command.
     */
    public static final String DESCRIPTION = "Remove a plugin.";

    /**
     * The main permission of the command.
     */
    public static final String PERMISSION = "plugman.remove";

    /**
     * The proper usage of the command.
     */
    public static final String USAGE = "/plugman install <plugin-name>";

    /**
     * The sub permissions of the command.
     */
    public static final String[] SUB_PERMISSIONS = {""};

    /**
     * Construct out object.
     *
     * @param sender the command sender
     */
    public RemoveCommand(CommandSender sender) {
        super(sender, NAME, DESCRIPTION, PERMISSION, SUB_PERMISSIONS, USAGE);
    }

    /**
     * Execute the command.
     *
     * @param sender  the sender of the command
     * @param command the command being done
     * @param label   the name of the command
     * @param args    the arguments supplied
     */
    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args)
    {

    }
}
