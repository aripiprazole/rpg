package me.lorenzo.rpg.command;

import me.lorenzo.rpg.Main;
import me.lorenzo.rpg.manager.Account;
import me.lorenzo.rpg.manager.Character;
import me.lorenzo.rpg.inventory.CharactersMenu;
import net.eduard.api.lib.Mine;
import net.eduard.api.lib.manager.CommandManager;
import net.eduard.api.lib.manager.DBManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RpgClassCommand extends CommandManager {

    public RpgClassCommand() {
        super("class");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cVocê não é um jogador!");
        } else {
            Player p = (Player) sender;
            DBManager db = Main.getManager().getDatabase();
            Account account = Main.getManager().getAccount(p);
            Character character = Main.getManager().getAccount(p).getActualCharacter();
            if (account.haveSelectedCharacter()) {
                character.setLocation(p.getLocation());
                character.setInventory(p.getInventory().getContents());
                Location loc = p.getLocation();
                String local = loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";"
                        + loc.getPitch();
                String inventario = Mine.fromItemsToBase64(character.getInventory());
                Main.getRpgUtil().saveLocation(p, character, local, inventario, db);
                account.deselectCharacter();
            }
            if (Main.getConfigs().contains("config.spawn"))
                p.teleport(Main.getConfigs().getLocation("config.spawn"));
            else {
                p.teleport(new Location(p.getWorld(), 0, 5, 0));
                p.sendMessage("§cSpawn não selecionado.");
            }
            CharactersMenu.open(p);
            p.getInventory().clear();
        }
        return false;
    }
}
