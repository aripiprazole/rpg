package me.lorenzo.rpg.inventory;

import net.eduard.api.lib.Mine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class IntialAttributesMenu implements Listener {

    static String invname = "";

    public static void abrir(Player p){
        Inventory inv = Bukkit.createInventory(null, 5*9, invname);
        ItemStack danoFisico = Mine.newItem(Material.STONE_SWORD, "§fEvoluir §6§lDANO FISICO");
        ItemStack danoMagico = Mine.newItem(Material.FIREBALL, "§fEvoluir §6§lDANO MAGICO");
        ItemStack manaPoints = Mine.newItem(Material.LAPIS_ORE, "§fEvoluir §6§lMANA");
        ItemStack healthPoints = Mine.newItem(Material.GOLDEN_APPLE, "§fEvoluir §6§lVIDA");
        ItemStack agilidade = Mine.newItem(Material.STONE_SWORD, "§fEvoluir §6§lAGILIDADE");
        ItemStack dextreza = Mine.newItem(Material.STONE_SWORD, "§fEvoluir §6§lDEXTREZA");
        ItemStack defesaFisica = Mine.newItem(Material.STONE_SWORD, "§fEvoluir §6§lDEFESA FISICA");
        ItemStack defesaMagica = Mine.newItem(Material.STONE_SWORD, "§fEvoluir §6§lDEFESA MAGICA");
        ItemStack lifesteal = Mine.newItem(Material.STONE_SWORD, "§fEvoluir §6§lROUBO DE VIDA");
        ItemStack inteligencia  = Mine.newItem(Material.STONE_SWORD, "§fEvoluir §6§lINTELIGENCIA");
        ItemStack hpRegen  = Mine.newItem(Material.STONE_SWORD, "§fEvoluir §6§lVIDA REGEN");
        ItemStack mpRegen  = Mine.newItem(Material.STONE_SWORD, "§fEvoluir §6§lMANA REGEN");
    }

}
