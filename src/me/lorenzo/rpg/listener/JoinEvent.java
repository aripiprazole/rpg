package me.lorenzo.rpg.listener;

import me.lorenzo.rpg.Main;
import me.lorenzo.rpg.inventory.CharactersMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    @EventHandler
    public void event(PlayerJoinEvent event){
        Player p = event.getPlayer();
        if (!Main.getManager().haveAccount(p)) {
            Main.getManager().createAccount(p);
        }
        if (Main.getConfigs().contains("config.spawn")) {
            p.teleport(Main.getConfigs().getLocation("config.spawn"));
        } else {
            p.sendMessage("§cSpawn não foi selecionado.");
        }
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> CharactersMenu.open(p), 10L);
        p.sendMessage("§7Carregando texturas...");
        p.getInventory().clear();
    }

}
