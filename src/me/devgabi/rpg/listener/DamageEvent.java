package me.devgabi.rpg.listener;

import me.devgabi.rpg.manager.Account;
import me.devgabi.rpg.manager.Attribute;
import me.devgabi.rpg.Main;
import me.devgabi.rpg.manager.Character;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageEvent implements Listener {

    @EventHandler
    public void event(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();
            Account account = Main.getManager().getAccount(p);
            Character character = account.getActualCharacter();
            Integer nivelDano = character.getAttributeLevels().getOrDefault(Attribute.DANO, 0);
            double danoExtra = nivelDano * Attribute.DANO.getPerLevelRate();
            if (p.getItemInHand() != null) {
                danoExtra += Attribute.DANO.get(p.getItemInHand());
            }
            event.setDamage(event.getDamage() + danoExtra);
            p.sendMessage("§7Seu dano é: §c" + danoExtra);
        }
    }

}
