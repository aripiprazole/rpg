package me.lorenzo.rpg.listener;

import me.lorenzo.rpg.Main;
import me.lorenzo.rpg.manager.Account;
import me.lorenzo.rpg.manager.Character;
import net.eduard.api.lib.Mine;
import net.eduard.api.lib.manager.DBManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {

    @EventHandler
    public void event(PlayerQuitEvent event){
        Player p = event.getPlayer();
        if (Main.getManager().haveAccount(p)) {
            Account account = Main.getManager().getAccount(p);
            Character character = account.getActualCharacter();
            if (character != null) {
                character.setLocation(p.getLocation());
                character.setInventory(p.getInventory().getContents());
                Location loc = p.getLocation();
                String local = loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";"
                        + loc.getPitch();
                String inventario = Mine.fromItemsToBase64(character.getInventory());
                DBManager db = Main.getManager().getDatabase();
                Main.getRpgUtil().saveLocation(p, character, local, inventario, db);

            }
        }
    }

}
