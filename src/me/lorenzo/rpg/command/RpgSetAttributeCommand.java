package me.lorenzo.rpg.command;

import me.lorenzo.rpg.manager.Attribute;
import net.eduard.api.lib.manager.CommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RpgSetAttributeCommand extends CommandManager {

    public RpgSetAttributeCommand(){
        super("setattribute");
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {
        if (!(sender instanceof Player)) sender.sendMessage("§cVocê não é um jogador!");
        else {
            if (sender.hasPermission("rpg.attributes")) {
                Player p = (Player) sender;
                if (p.getItemInHand() != null) {
                    if (args.length < 3) {
                        p.sendMessage("§a/rpg setattribute <atributo> <valor>");
                    } else {
                        try {
                            Attribute attribute = Attribute.valueOf(args[0].toUpperCase());
                            try {
                                Double valor = Double.parseDouble(args[1]);
                                attribute.set(p.getItemInHand(),valor);
                            } catch (Exception ex) {
                                p.sendMessage("§cDigite um numero decimal.");
                            }
                        } catch (Exception ex) {
                            StringBuilder sb = new StringBuilder();
                            for(Attribute atr : Attribute.values()){
                                sb.append(atr.name());
                                sb.append(", ");
                            }
                            String message = sb.toString().substring(0, sb.toString().length() - 2);
                            p.sendMessage("§eAtributos: "+message);
                        }
                    }

                } else p.sendMessage("§cColoque um item na mão!");
            } else sender.sendMessage("§cVocê não tem permissão!");
        }
        return false;
    }

}
