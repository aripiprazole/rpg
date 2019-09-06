package me.lorenzo.rpg.command;

import net.eduard.api.lib.manager.CommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class RpgHelpCommand extends CommandManager {

    public RpgHelpCommand() {
        super("help");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        for (CommandManager cmd : getParent().getCommands().values()) {
            if (sender.hasPermission(cmd.getPermission())) {
                sender.sendMessage("§c" + cmd.getUsage() + " §8- §7" + cmd.getDescription());
            }
        }
        return false;
    }
}
