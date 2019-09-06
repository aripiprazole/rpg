package me.lorenzo.rpg.command;

import me.lorenzo.rpg.Main;
import net.eduard.api.lib.manager.CommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RpgSetSpawnCommand extends CommandManager {

    public RpgSetSpawnCommand() {
        super("setspawn");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {
        if (!(sender instanceof Player)) sender.sendMessage("§cVocê não é um jogador!");
        else {
            Player p = (Player) sender;
            if (p.hasPermission("rpg.warps")) {
                Main.getConfigs().set("config.spawn", p.getLocation());
                Main.getConfigs().saveConfig();
                p.sendMessage("§aVocê selecionou o spawn.");
            }
        }
        return false;
    }
}
