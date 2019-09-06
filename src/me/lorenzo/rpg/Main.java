package me.lorenzo.rpg;

import me.lorenzo.rpg.util.RPGUtil;
import me.lorenzo.rpg.util.ReflectionUtil;
import me.lorenzo.rpg.util.Variables;
import net.eduard.api.lib.manager.CommandManager;
import net.eduard.api.lib.modules.Extra;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.lorenzo.rpg.manager.Account;
import me.lorenzo.rpg.manager.Character;
import me.lorenzo.rpg.manager.RPGManager;
import net.eduard.api.lib.Mine;
import net.eduard.api.lib.config.Config;
import net.eduard.api.lib.manager.DBManager;

public class Main extends JavaPlugin {

    private static RPGManager manager;
    private static RPGUtil rpgUtil;
    private static ReflectionUtil reflectionUtil;

    private static Config configuration;
    private static Config messages;
    private static Main instance;

    @Override
    public void onEnable() {
        setInstance(this);
        setManager(new RPGManager());
        setReflectionUtil(new ReflectionUtil());
        Main.setRpgUtil(new RPGUtil());
        setConfiguration(new Config(Main.getInstance(), "configuration.yml"));
        setMessages(new Config(Main.getInstance(), "messages.yml"));
        registerCommands();
        registerListeners();
        getConfigs().saveConfig();
        Variables.reload();
        getManager().reload();
    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Account account = getManager().getAccount(p);
            Character character = account.getActualCharacter();
            if (character != null) {
                character.setLocation(p.getLocation());
                character.setInventory(p.getInventory().getContents());
                Location loc = p.getLocation();
                String local = loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";"
                        + loc.getPitch();
                String inventario = Mine.fromItemsToBase64(character.getInventory());
                DBManager db = Main.getManager().getDatabase();
                getRpgUtil().saveLocation(p, character, local, inventario, db);
            }
        }
    }

    private void registerListeners() {
        try {
            for (Class claz : Extra.getClasses(this.getClass(), this.getClass().getPackage().getName())) {
                try {
                    Bukkit.getPluginManager().registerEvents((Listener) claz.newInstance(), Main.getInstance());
                    System.out.println("[" + getName() + "] ++ REGISTERED LISTENER " + claz.getSimpleName());
                } catch (Exception ex) {
                    continue;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void registerCommands() {
        try {
            for (Class claz : Extra.getClasses(this.getClass(), this.getClass().getPackage().getName())) {
                if (claz.getSuperclass() == null) continue;
                if (claz.getSuperclass().getName().equals("net.eduard.api.lib.manager.CommandManager")) {
                    CommandManager cmd = (CommandManager) claz.newInstance();
                    cmd.register();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static RPGManager getManager() {
        return manager;
    }

    public static void setManager(RPGManager manager) {
        Main.manager = manager;
    }

    public static Config getConfigs() {
        return configuration;
    }

    public static void setConfiguration(Config configuration) {
        Main.configuration = configuration;
    }

    public static Main getInstance() {
        return instance;
    }

    public static void setInstance(Main instance) {
        Main.instance = instance;
    }

    public static RPGUtil getRpgUtil() {
        return rpgUtil;
    }

    public static void setRpgUtil(RPGUtil rpgUtil) {
        Main.rpgUtil = rpgUtil;
    }

    public static Config getMessages() {
        return messages;
    }

    public static void setMessages(Config messages) {
        Main.messages = messages;
    }

    public static ReflectionUtil getReflectionUtil() {
        return reflectionUtil;
    }

    public static void setReflectionUtil(ReflectionUtil reflectionUtil) {
        Main.reflectionUtil = reflectionUtil;
    }

}
