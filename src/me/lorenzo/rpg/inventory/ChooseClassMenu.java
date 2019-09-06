package me.lorenzo.rpg.inventory;

import me.lorenzo.rpg.manager.RpgClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.lorenzo.rpg.Main;
import me.lorenzo.rpg.manager.Account;
import net.eduard.api.lib.Mine;

public class ChooseClassMenu implements Listener {

	public static void abrir(Player p) {
		Account account = Main.getManager().getAccount(p);
		Inventory inv = Bukkit.createInventory(null, 5 * 9, "§aSelecione sua classe:");

		if (account.getCharacters().size() == account.getCharacterLimit()) return;

		ItemStack arqueiro = Mine.newItem(Material.BOW, "§fPegar §6§lARQUEIRO");
		inv.setItem(10, arqueiro);
		ItemStack guerreiro = Mine.newItem(Material.IRON_SPADE, "§fPegar §6§lGUERREIRO");
		inv.setItem(11, guerreiro);
		ItemStack mago = Mine.newItem(Material.STICK, "§fPegar §6§lMAGO");
		inv.setItem(12, mago);
		ItemStack assassino = Mine.newItem(Material.SHEARS, "§fPegar §6§lASSASSINO");
		inv.setItem(19, assassino);
		ItemStack sacerdote = Mine.newItem(Material.POTION, "§fPegar §6§lSACERDOTE");
		inv.setItem(20, sacerdote);
		ItemStack arcanjo = Mine.newItem(Material.WOOD_SWORD, "§fPegar §6§lARCANJO");
		inv.setItem(21, arcanjo);

		ItemStack voltar = Mine.newItem(Material.BARRIER, "§cVoltar");
		inv.setItem(25, voltar);
		ItemStack continuar = Mine.newItem(Material.ARROW, "§aProsseguir");
		inv.setItem(26, continuar);

		p.openInventory(inv);

	}

	@EventHandler
	public void event(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			if (e.getInventory().getName().equals("§aSelecione sua classe:")) {

				Account account = Main.getManager().getAccount(p);

				ItemStack arqueiro = Mine.newItem(Material.BOW, "§fPegar §6§lARQUEIRO");
				ItemStack guerreiro = Mine.newItem(Material.IRON_SPADE, "§fPegar §6§lGUERREIRO");
				ItemStack mago = Mine.newItem(Material.STICK, "§fPegar §6§lMAGO");
				ItemStack assassino = Mine.newItem(Material.SHEARS, "§fPegar §6§lASSASSINO");
				ItemStack sacerdote = Mine.newItem(Material.POTION, "§fPegar §6§lSACERDOTE");
				ItemStack arcanjo = Mine.newItem(Material.WOOD_SWORD, "§fPegar §6§lARCANJO");

				if (e.getCurrentItem().isSimilar(arqueiro)) {
					account.createCharacter(RpgClass.ARCHER);
				}
				if (e.getCurrentItem().isSimilar(guerreiro)) {
					account.createCharacter(RpgClass.WAIRROR);
				}
				if (e.getCurrentItem().isSimilar(mago)) {
					account.createCharacter(RpgClass.MAGE);
				}
				if (e.getCurrentItem().isSimilar(assassino)) {
					account.createCharacter(RpgClass.ASSASSIN);
				}
				if (e.getCurrentItem().isSimilar(sacerdote)) {
					account.createCharacter(RpgClass.CLERIG);
				}
				if (e.getCurrentItem().isSimilar(arcanjo)) {
					account.createCharacter(RpgClass.ARCHANGEL);
				}
				CharactersMenu.open(p);
				e.setCancelled(true);

			}

		}
	}

}
