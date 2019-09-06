package me.lorenzo.rpg.command;

import me.lorenzo.rpg.Main;
import me.lorenzo.rpg.manager.Character;
import net.eduard.api.lib.manager.CommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RpgSetNickCommand extends CommandManager {

    public RpgSetNickCommand(){
        super("setnick");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) sender.sendMessage("§cVocê não é um jogador"); else {
            Player p = (Player) sender;
            if(args.length==1){
                return true;
            } else if(args.length==2) {
                if(p.hasPermission("rpg.setnick")) {
                    String sub = args[1];
                    if(Main.getManager().getAccount(p).haveSelectedCharacter()){
                        Character character = Main.getManager().getAccount(p).getActualCharacter();
                        character.setName(sub);
                        p.sendMessage("§7Você mudou seu nick do o character: "+ character.getId()+" para: "+sub);
                    } else {
                        p.sendMessage("§cSelecione um personagem");
                    }
                }
            }
        }
        return false;
    }
}
