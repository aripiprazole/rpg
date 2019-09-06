package me.lorenzo.rpg.command;

import net.eduard.api.lib.manager.CommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RpgFullLifeCommand extends CommandManager {

    public RpgFullLifeCommand(){
        super("full");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args)  {
        if(!(sender instanceof Player)) sender.sendMessage("§cVocê não é um jogador");
        else {
            Player p = (Player) sender;
            p.setHealth(p.getMaxHealth());
            p.sendMessage(
                    "§7Sua vida é: §a" + p.getHealth() + "§f/§a" + p.getMaxHealth());
        }
        return false;
    }
}
