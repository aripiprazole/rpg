package me.lorenzo.rpg.util;

import me.lorenzo.rpg.manager.Character;
import net.eduard.api.lib.manager.DBManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RPGUtil {

    public void verifyArmorStatus(Player p){

    }

    public void saveLocation(Player p, Character character, String local, String inventario, DBManager db) {
        PreparedStatement stm;
        try {
            Bukkit.getConsoleSender().sendMessage("§4[DB] §fSalvo localização de "+p.getName()+"§f.");
            stm = db.getConnection().prepareStatement("UPDATE `personagens` SET `location` = ? WHERE `id` = ?");
            stm.setString(1, local);
            stm.setInt(2, character.getId());
            stm.executeUpdate();
        } catch (SQLException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        try {
            Bukkit.getConsoleSender().sendMessage("§4[DB] §fSalvo inventário de "+p.getName()+"§f.");
            stm = db.getConnection().prepareStatement("UPDATE `personagens` SET `inventario` = ? WHERE `id` = ?");
            stm.setString(1, inventario);
            stm.setInt(2, character.getId());
            stm.executeUpdate();
        } catch (SQLException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

}
