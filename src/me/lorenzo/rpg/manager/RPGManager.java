package me.lorenzo.rpg.manager;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import me.lorenzo.rpg.util.RPGUtil;
import net.eduard.api.lib.Mine;
import net.eduard.api.lib.modules.Extra;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.lorenzo.rpg.Main;
import net.eduard.api.lib.manager.DBManager;

public class RPGManager {

	private DBManager database = new DBManager();
	private transient ArrayList<Account> accounts = new ArrayList<Account>();

	public Account getAccount(Player player) {
		for (Account account : accounts) {
			if (account.getName().contentEquals(player.getName())) {
				return account;
			}
		}
		return null;
	}



	public void reload() {
		DBManager db = getDatabase();
		try {
			db.setHost("localhost");
			db.setUser("lorenzo");
			db.setPass("lorenzo");
			db.setPort("3306");
			db.openConnection();
			db.createDatabase("rpg_database");
			db.useDatabase("rpg_database");
			db.createTable("contas", "nome varchar(16), uuid varchar(50), password varchar(100)");
			db.createTable("personagens_profissoes",
					"id_personagem int, prof_miner int, prof_miner_xp double, prof_pescador int, prof_pescador_xp double, prof_lenhador int, prof_lenhador_xp double, prof_cultivador int, prof_cultivador_xp double, prof_escriba int, prof_escriba_xp double, prof_joalheiro int, prof_joalheiro_xp double, prof_armeiro int, prof_armeiro_xp double, prof_ferreiro int, prof_ferreiro_xp double, prof_artesao int, prof_artesao_xp double, prof_alquimista int, prof_alquimista_xp double, prof_cozinheiro int, prof_cozinheiro_xp double");
			db.createTable("characters",
					"conta_id int, classe varchar(100), el_terra int, el_gelo int, el_ar int, el_agua int, "
							+ "el_fogo int, el_raio int, mana int, vida int, hp_regen int, mp_regen int, agilidade int, "
							+ "dextreza int, forca int, inteligencia int, defence int, nivel int, exp_atual long, exp_total long, "
							+ "location varchar(350), inventario text, armadura text");
			Bukkit.getConsoleSender().sendMessage("§a[DB] Conectado à base de dados com sucesso!");
			ResultSet result = db.select("select * from contas");
			while (result.next()) {

				Account account = new Account();
				account.setId(result.getInt("id"));
				account.setName(result.getString("nome"));
				Main.getManager().getAccounts().add(account);
			}
			result.close();
			result = db.select("select * from personagens");
			while (result.next()) {
				Character character = new Character();
				int idDaConta = result.getInt("conta_id");
				Account account = Main.getManager().getAccount(idDaConta);
				character.setLevel(result.getInt("nivel"));
				character.setRpgClass(RpgClass.valueOf(result.getString("classe")));
				character.setId(result.getInt("ID"));
				try {
					if (result.getString("inventario") != null) {
						ItemStack[] items = Mine.fromBase64toItems(result.getString("inventario"));
						if (items == null) {
							character.setInventory(new ItemStack[4 * 9]);
						} else {
							character.setInventory(items);
						}

					} else {
						character.setInventory(new ItemStack[4 * 9]);
					}
				} catch (Exception ex){
					ex.printStackTrace();
				}
				account.deselectCharacter();
				try {
					String textoLocation = result.getString("location");
					if (!textoLocation.equals("NULL")) {
						String[] textoDividido = textoLocation.split(";");

						Location loc = new Location(Bukkit.getWorld("world"), Extra.toDouble(textoDividido[0]),
								Extra.toDouble(textoDividido[1]), Extra.toDouble(textoDividido[2]),
								Extra.toFloat(textoDividido[3]), Extra.toFloat(textoDividido[4]));
						character.setLocation(loc);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				// CARREGAR ATRIBUTOS

				character.getAttributeLevels().put(Attribute.DEXTREZA, result.getInt("dextreza"));
				character.getAttributeLevels().put(Attribute.FORCA, result.getInt("forca"));
				character.getAttributeLevels().put(Attribute.AGILIDADE, result.getInt("agilidade"));
				character.getAttributeLevels().put(Attribute.INTELIGENCIA, result.getInt("inteligencia"));
				character.getAttributeLevels().put(Attribute.DEFESA, result.getInt("defence"));
				character.getAttributeLevels().put(Attribute.MANA, result.getInt("mana"));
				character.getAttributeLevels().put(Attribute.VIDA, result.getInt("vida"));
				character.getAttributeLevels().put(Attribute.HP_REGEN, result.getInt("hp_regen"));
				character.getAttributeLevels().put(Attribute.MP_REGEN, result.getInt("mp_regen"));

				// CARREGAR ELEMENTOS

				character.getAttributeLevels().put(Attribute.EL_TERRA, result.getInt("el_terra"));
				character.getAttributeLevels().put(Attribute.EL_GELO, result.getInt("el_gelo"));
				character.getAttributeLevels().put(Attribute.EL_FOGO, result.getInt("el_fogo"));
				character.getAttributeLevels().put(Attribute.EL_AR, result.getInt("el_ar"));
				character.getAttributeLevels().put(Attribute.EL_AGUA, result.getInt("el_agua"));
				character.getAttributeLevels().put(Attribute.EL_RAIO, result.getInt("el_raio"));

				account.getCharacters().add(character);
			}
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage("§4[DB] §cNão foi possivel se conectar à base de dados " + db.getDatabase() + ".");
			Bukkit.getPluginManager().disablePlugin(Main.getInstance());
		}
	}

	public boolean haveAccount(Player player) {
		return getAccount(player) != null;
	}

	public Account createAccount(Player player) {
		Account account = new Account();
		DBManager db = Main.getManager().getDatabase();
		int idDaConta = db.insert("contas", player.getName(),player.getUniqueId(),UUID.randomUUID());
		account.setId(idDaConta);
		account.setName(player.getName());
		accounts.add(account);
		return account;
	}

	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}

	public DBManager getDatabase() {
		return database;
	}

	public void setDatabase(DBManager database) {
		this.database = database;
	}

	public Account getAccount(int id) {
		return accounts.stream().filter(conta -> conta.getId() == id).findFirst().orElse(null);
	}//vlw dnv ksksks essa aq é bem diferente skks

}
