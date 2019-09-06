package me.lorenzo.rpg.inventory;

import me.lorenzo.rpg.manager.Character;
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

public class CharactersMenu implements Listener {

	public static void open(Player p) {
		Inventory inventory = Bukkit.createInventory(null, 3 * 9, "§7§lSelecione um personagem:");
		ItemStack characterItem = Mine.newItem(Material.ARMOR_STAND, "§a[+] Create a new character");
		Account account = Main.getManager().getAccount(p);
		int slot = 0;
		for (Character character : account.getCharacters()) {
			ItemStack head = Mine.newHead("§7§lPersonagem: " + character.getId(), account.getName(), 1);
			inventory.setItem(slot++, head);
		}

		inventory.setItem(account.getCharacterAmount(), characterItem);
		if (p.hasPermission("rpg.staff")) {
			ItemStack staff = Mine.newItem(Material.APPLE, "§aEntre com sua conta staff!");
			inventory.setItem(17, staff);
		}

		ItemStack frame = Mine.newItem(Material.ITEM_FRAME, " ");

		ItemStack edit = Mine.newItem(Material.ENCHANTMENT_TABLE, "§7§lEdit Classes ");
		ItemStack cancel = Mine.newItem(Material.BLAZE_POWDER, "§7§lCancel Class Deletion");

		inventory.setItem(7, frame);
		inventory.setItem(16, frame);
		inventory.setItem(25, frame);
		inventory.setItem(8, edit);
		inventory.setItem(26, cancel);

		p.openInventory(inventory);

	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void event(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			Player player = (Player) e.getWhoClicked();
			Account account = Main.getManager().getAccount(player);
			if (e.getCurrentItem()==null)return;
			if (e.getInventory().getName().equals("§7§lSelecione um personagem:")) {
				e.setCancelled(true);
				if (e.getCurrentItem().getType().equals(Material.ARMOR_STAND)) {
					ChooseClassMenu.abrir(player);
				}
				if (e.getRawSlot() == 17) {
					if (player.hasPermission("wynncraft.staff")) {
						player.closeInventory();
						player.sendMessage("§cVocê entrou na sua conta staff!");
						account.setStaff(true);
					}
				}
				for (Character character : account.getCharacters()) {
					ItemStack item = Mine.newHead("§7§lPersonagem: " + character.getId(), account.getName(), 1);
					if (Mine.equals(item, e.getCurrentItem())) {
						account.selectCharacter(character);
						player.closeInventory();
						if (character.getLocation() != null) {
							player.teleport(character.getLocation());
						}
						character.updateLife(player);
						player.getInventory().setContents(character.getInventory());
						player.closeInventory();
						player.sendMessage("§7ID: §c" + character.getId());
						player.sendMessage("§7Classe: §c" + character.getRpgClass().toString());
						player.sendMessage("§7Vida: §c" + player.getMaxHealth());
					}
				}
			}
		}
	}

}
