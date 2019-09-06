package me.lorenzo.rpg.manager;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.Mine;

public enum Attribute {

	DANO("§cDANO: §f", 1, 2),
	DEFESA("§cDEFESA: §f", 2, 5),
	VIDA("§aVIDA: §f", 4, 10),

	FORCA("§cFORÇA: §f", 1, 2),
	DEXTREZA("§cDEXTREZA: §f", 1, 2),
	INTELIGENCIA("§cINTELIGENCIA: §f", 1, 2),
	AGILIDADE("§cAGILIDADE: §f", 1, 2),
	MANA("§cMANA: §f", 1, 2),
	HP_REGEN("§cHPREGEN: §f", 1, 2),
	MP_REGEN("§cMPREGEN: §f", 1, 2),
	
	EL_TERRA("§cTERRA: §f", 1, 2),
	EL_FOGO("§cFOGO: §f", 1, 2),
	EL_AR("§cAR: §f", 1, 2),
	EL_AGUA("§cAGUA: §f", 1, 2),
	EL_RAIO("§cRAIO: §f", 1, 2),
	EL_GELO("§cGELO: §f", 1, 2);

	private String prefix;
	private int lorePosition;
	private boolean percent;
	private ItemStack material;
	private double perLevelRate = 3;

	Attribute(String prefix, int posicao) {
		setPrefix(prefix);
		setLorePosition(posicao);
	}

	Attribute(String prefix, int posicao, double perLevelRate) {
		setPrefix(prefix);
		setLorePosition(posicao);
		setPerLevelRate(perLevelRate);
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public List<String> set(List<String> lore, double amount) {
		String novaLinha = prefix;
		if (percent) {
			novaLinha += (amount * 100) + "%";
		} else {
			novaLinha += amount;
		}
		if (has(lore)) {
			int linha = getPosition(lore);

			lore.set(linha, novaLinha);
		} else
			lore.add(novaLinha);
		return lore;
	}

	public double get(List<String> lore) {
		int posicao = getPosition(lore);
		if (posicao == -1)
			return 0;
		String linha = lore.get(posicao);

		linha = linha.replaceFirst(prefix, "");
		if (percent) {
			linha = linha.replaceFirst("%", "");
			double valor = Double.parseDouble(linha);
			valor = valor / 100;
			return valor;
		} else {
			double valor = Double.parseDouble(linha);
			return valor;
		}
	}

	public int getPosition(List<String> lore) {
		for (int id = 0; id < lore.size(); id++) {
			String linha = lore.get(id);
			if (linha.startsWith(prefix)) {
				return id;
			}
		}
		return -1;
	}

	public ItemStack set(ItemStack item, double amount) {
		List<String> lore = Mine.getLore(item);
		set(lore, amount);
		Mine.setLore(item, lore);
		return item;
	}

	public boolean has(List<String> lore) {
		for (String line : lore) {
			if (line.startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}

	public int getLorePosition() {
		return lorePosition;
	}

	public void setLorePosition(int lorePosition) {
		this.lorePosition = lorePosition;
	}

	public boolean isPercent() {
		return percent;
	}

	public void setPercent(boolean percent) {
		this.percent = percent;
	}

	public double get(ItemStack item) {

		return get(Mine.getLore(item));
	}

	public double getPerLevelRate() {
		return perLevelRate;
	}

	public void setPerLevelRate(double perLevelRate) {
		this.perLevelRate = perLevelRate;
	}

	public ItemStack getMaterial() {
		return material;
	}

	public void setMaterial(ItemStack material) {
		this.material = material;
	}
}
