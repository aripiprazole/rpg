package me.lorenzo.rpg.command;

import me.lorenzo.rpg.Main;
import me.lorenzo.rpg.manager.Account;
import me.lorenzo.rpg.manager.Character;
import net.eduard.api.lib.manager.CommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RpgLifeCommand extends CommandManager {

    public RpgLifeCommand() {
        super("life");
        register(new RpgFullLifeCommand());
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§4Você não é um jogador.");
        } else {
            Player p = (Player) sender;
            p.sendMessage(
                    "§7Sua vida é: §a" + p.getHealth() + "§f/§a" + p.getMaxHealth());
            Account account = Main.getManager().getAccount(p);
            Character a;
            if (account.haveSelectedCharacter()) {
                a = account.getActualCharacter();
                a.updateLife(p);
            }
        }
        return false;
    }

}
