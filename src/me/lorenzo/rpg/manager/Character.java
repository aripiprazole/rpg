package me.lorenzo.rpg.manager;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Character {

	private Integer id;
	private RpgClass rpgClass;
	private String name;

	private Player player;
	private Map<Attribute, Integer> attributeLevels = new HashMap<>();
	private Map<Profession, Integer> professionLevels = new HashMap<>();
	private Map<Profession, Double> professionExperience = new HashMap<>();
	private Location location;
	private ItemStack[] inventory;
	private Integer level;
	private Long totalExperience;
	private Long levelExperience;

	public RpgClass getRpgClass() {
		return rpgClass;
	}

	@SuppressWarnings("deprecation")
	public void fillLife(Player player) {
		player.setHealth(player.getMaxHealth());
	}
	
	public void updateSpeed(Player p) {
		int level = getAttributeLevels().getOrDefault(Attribute.VIDA, 0);
		double speed = 0.2 +(this.level * Attribute.AGILIDADE.getPerLevelRate());
		for(ItemStack item : p.getInventory().getArmorContents()) {
			if(item == null)
				continue;
			speed += Attribute.AGILIDADE.get(item);
		}
		p.setWalkSpeed((float) speed);
		p.sendMessage("§4[SERVER] §fSua velocidade é: "+p.getWalkSpeed());
		
	}

	@SuppressWarnings("deprecation")
	public void updateLife(Player p) {
		int nivel = getAttributeLevels().getOrDefault(Attribute.VIDA, 0);
		double vida = 20.0 + (nivel * Attribute.VIDA.getPerLevelRate());
		for (ItemStack item : p.getInventory().getArmorContents()) {
			if (item == null)
				continue;
			vida += Attribute.VIDA.get(item);
		}
		p.setMaxHealth(vida);
	}

	public void setRpgClass(RpgClass rpgClass) {
		this.rpgClass = rpgClass;
	}

	public Map<Attribute, Integer> getAttributeLevels() {
		return attributeLevels;
	}

	public void setAttributeLevels(Map<Attribute, Integer> attributeLevels) {
		this.attributeLevels = attributeLevels;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getLevelExperience() {
		return levelExperience;
	}

	public void setLevelExperience(long levelExperience) {
		this.levelExperience = levelExperience;
	}

	public long getTotalExperience() {
		return totalExperience;
	}

	public void setTotalExperience(long totalExperience) {
		this.totalExperience = totalExperience;
	}

	public Map<Profession, Integer> getProfessionLevels() {
		return professionLevels;
	}

	public void setProfessionLevels(Map<Profession, Integer> professionLevels) {
		this.professionLevels = professionLevels;
	}

	public Map<Profession, Double> getProfessionExperience() {
		return professionExperience;
	}

	public void setProfessionExperience(Map<Profession, Double> professionExperience) {
		this.professionExperience = professionExperience;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public ItemStack[] getInventory() {
		return inventory;
	}

	public void setInventory(ItemStack[] inventory) {
		this.inventory = inventory;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
