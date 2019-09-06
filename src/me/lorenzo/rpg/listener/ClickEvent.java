package me.lorenzo.rpg.listener;

import me.lorenzo.rpg.Main;
import me.lorenzo.rpg.manager.Account;
import me.lorenzo.rpg.manager.Character;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClickEvent implements Listener {

    @EventHandler
    public void event(InventoryClickEvent event){
        if (event.getWhoClicked() instanceof Player) {
            Player p = (Player) event.getWhoClicked();
            Account account = Main.getManager().getAccount(p);
            Character character = account.getActualCharacter();
        }
    }

}
