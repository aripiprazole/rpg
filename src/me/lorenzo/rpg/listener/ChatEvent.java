package me.lorenzo.rpg.listener;

import me.lorenzo.rpg.Main;
import me.lorenzo.rpg.manager.Character;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler
    public void event(AsyncPlayerChatEvent event){
        if (Main.getManager().getAccount(event.getPlayer()).haveSelectedCharacter()) {
            Character character = Main.getManager().getAccount(event.getPlayer()).getActualCharacter();
            if (character.getName() != null)
                event.setFormat("§e[" + character.getRpgClass().name() + "] §r" + character.getName() + " §7(" + event.getPlayer().getName() + ")§8: §e" + event.getMessage());
            else
                event.setFormat(character.getRpgClass().name() + " §7(" + event.getPlayer().getName() + ")§8: §e" + event.getMessage());
        } else {
            event.setFormat(event.getPlayer().getName() + "§8: §e" + event.getMessage());
        }
    }

}
