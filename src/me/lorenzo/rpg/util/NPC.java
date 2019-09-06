package me.lorenzo.rpg.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class NPC {
    private String name;
    private Location loc;
    private Class<?> EntityPlayerClass;
    private Class<?> PacketPlayOutPlayerInfo;
    private Class<?> EnumPlayerInfoAction;
    private Constructor<?> EntityPlayerConstructor;
    private Object EntityPlayer;
    private Object MinecraftServer;
    private int id;
    private Object WorldServer;
    private Object World;
    private Object PlayerInteractManager;
    private Object SpawnPacket;
    private Object TabPacket;
    private Method setLocation;
    private Object ADD_PLAYER;
    private Object packetOutEntity;
    private Object packetHead;
    private UUID uuid;
    private String skin;
    private GameProfile GameProfile;

    public String getSkin() {
        return this.skin;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public Location getLocation() {
        return this.loc;
    }

    public void destroy() {
        try {
            this.removeEntityFromWorld(this.loc.getWorld());
            final Object REMOVE_PLAYER = this.EnumPlayerInfoAction.getField("REMOVE_PLAYER").get(null);
            final Object array = this.getArray(this.EntityPlayerClass, this.EntityPlayer);
            final Constructor<?> Constructor = this.PacketPlayOutPlayerInfo.getConstructor(REMOVE_PLAYER.getClass(),
                    array.getClass());
            this.TabPacket = Constructor.newInstance(REMOVE_PLAYER,
                    this.getArray(this.EntityPlayerClass, this.EntityPlayer));
            this.SpawnPacket = this.getNMSClass("PacketPlayOutEntityDestroy").getConstructor(int[].class)
                    .newInstance(this.getArray(Integer.TYPE, this.id));
            for (final Player p : Bukkit.getOnlinePlayers()) {
                this.sendPackets(p, this.SpawnPacket, this.TabPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte getFixRotation(final float yawpitch) {
        return (byte) (yawpitch * 256.0f / 360.0f);
    }

    private Object getArray(final Class<?> type, final Object... objects) {
        final Object array = Array.newInstance(type, objects.length);
        try {
            Integer contador = 0;
            for (final Object o : objects) {
                Array.set(array, contador, o);
                ++contador;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }

    public void setCustomName(final String name) {
        try {
            this.destroy();
            this.GameProfile = new GameProfile(this.uuid, name);
            skin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSkin(final String name) {
        try {
            this.destroy();
            this.GameProfile = new ProfileLoader(this.uuid.toString(), this.name, name).loadProfile();
            skin();
            this.skin = name;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void skin() throws InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException {
        this.EntityPlayer = this.EntityPlayerConstructor.newInstance(this.MinecraftServer, this.WorldServer,
                this.GameProfile, this.PlayerInteractManager);
        this.spawn();
        this.id = (int) this.EntityPlayer.getClass().getMethod("getId", (Class<?>[]) new Class[0])
                .invoke(this.EntityPlayer, new Object[0]);
        this.setGameMode(GameMode.SURVIVAL);
        this.setGameMode(GameMode.CREATIVE);
    }

    public String getName() {
        return this.name;
    }

    public void spawn() {
        try {
            final Object array = this.getArray(this.EntityPlayerClass, this.EntityPlayer);
            Constructor<?> Constructor = this.PacketPlayOutPlayerInfo.getConstructor(this.ADD_PLAYER.getClass(),
                    array.getClass());
            this.TabPacket = Constructor.newInstance(this.ADD_PLAYER,
                    this.getArray(this.EntityPlayerClass, this.EntityPlayer));
            this.SpawnPacket = this.getNMSClass("PacketPlayOutNamedEntitySpawn")
                    .getConstructor(this.EntityPlayer.getClass().getSuperclass()).newInstance(this.EntityPlayer);
            this.setLocation.invoke(this.EntityPlayer, this.loc.getX(), this.loc.getY(), this.loc.getZ(),
                    this.loc.getYaw(), this.loc.getPitch());
            this.packetOutEntity = this.getNMSClass("PacketPlayOutEntity").getClasses()[0].getDeclaredConstructors()[1]
                    .newInstance(this.id, this.getFixRotation(this.loc.getYaw()),
                            this.getFixRotation(this.loc.getPitch()), true);
            Constructor = this.getNMSClass("PacketPlayOutEntityHeadRotation")
                    .getConstructor(this.EntityPlayerClass.getSuperclass().getSuperclass().getSuperclass(), Byte.TYPE);
            this.packetHead = Constructor.newInstance(this.EntityPlayer, this.getFixRotation(this.loc.getYaw()));
            for (final Player p : Bukkit.getOnlinePlayers()) {
                this.sendPackets(p, this.SpawnPacket, this.TabPacket);
            }
            this.addPlayerConnection();
            this.addEntityToWorld(this.loc.getWorld());
            for (final Player p : Bukkit.getOnlinePlayers()) {
                this.sendPackets(p, this.packetOutEntity, this.packetHead);
            }
            this.setGameMode(GameMode.SURVIVAL);
            this.setGameMode(GameMode.CREATIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getGameMode() {
        try {
            final Object CraftPlayer = this.EntityPlayerClass.getMethod("getBukkitEntity", (Class<?>[]) new Class[0])
                    .invoke(this.EntityPlayer, new Object[0]);
            final Method getGameMode = CraftPlayer.getClass().getMethod("getGameMode", (Class<?>[]) new Class[0]);
            final Object GameMode = getGameMode.invoke(CraftPlayer, new Object[0]);
            return GameMode;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setPassenger(final Entity entity) {
        try {
            final Object CraftPlayer = this.EntityPlayerClass.getMethod("getBukkitEntity", (Class<?>[]) new Class[0])
                    .invoke(this.EntityPlayer, new Object[0]);
            final Method setPassenger = CraftPlayer.getClass().getMethod("setPassenger", Entity.class);
            setPassenger.invoke(CraftPlayer, entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeEntityFromWorld(final World world) {
        try {
            final Method removeEntity = this.WorldServer.getClass().getMethod("removeEntity",
                    this.EntityPlayerClass.getSuperclass().getSuperclass().getSuperclass());
            removeEntity.invoke(this.WorldServer, this.EntityPlayer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addEntityToWorld(final World world) {
        try {
            final Object nmsWorld = this.WorldServer;
            final Method addEntity = nmsWorld.getClass().getSuperclass().getMethod("addEntity",
                    this.EntityPlayerClass.getSuperclass().getSuperclass().getSuperclass(),
                    CreatureSpawnEvent.SpawnReason.class);
            addEntity.invoke(nmsWorld, this.EntityPlayer, CreatureSpawnEvent.SpawnReason.CUSTOM);
            final Object NMSWorld = this.getWorldNMS(this.loc.getWorld());
            final Field getPlayers = NMSWorld.getClass().getField("players");
            getPlayers.setAccessible(true);
            final List<?> players = (List<?>) getPlayers.get(NMSWorld);
            players.remove(this.EntityPlayer);
            getPlayers.set(NMSWorld, players);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setGameMode(final GameMode gm) {
        try {
            final Object CraftPlayer = this.EntityPlayerClass.getMethod("getBukkitEntity", (Class<?>[]) new Class[0])
                    .invoke(this.EntityPlayer, new Object[0]);
            final Method setGameMode = CraftPlayer.getClass().getMethod("setGameMode", gm.getClass());
            setGameMode.invoke(CraftPlayer, gm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addPlayerConnection() {
        try {
            final Constructor<?> newPC = this.getNMSClass("PlayerConnection").getConstructors()[0];
            final Object EnumNetworkManager = this.getNMSClass("EnumProtocolDirection").getField("SERVERBOUND")
                    .get(null);
            final Object NetworkManager = this.getNMSClass("NetworkManager").getConstructors()[0]
                    .newInstance(EnumNetworkManager);
            newPC.newInstance(this.MinecraftServer, NetworkManager, this.EntityPlayer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NPC(final String name, final Location loc, final String skin) {
        try {
            this.skin = skin;
            this.name = name;
            this.loc = loc;
            this.EntityPlayerClass = this.getNMSClass("EntityPlayer");
            this.World = this.getWorldNMS(loc.getWorld());
            this.MinecraftServer = this.getMinecraftServerNMS();
            this.WorldServer = this.getWorldServerNMS();
            this.uuid = UUID.randomUUID();
            this.GameProfile = new ProfileLoader(this.uuid.toString(), this.name, skin).loadProfile();
            this.PlayerInteractManager = this.getNMSClass("PlayerInteractManager")
                    .getConstructor(this.World.getClass().getSuperclass()).newInstance(this.World);
            this.EntityPlayerConstructor = this.EntityPlayerClass.getConstructor(
                    this.MinecraftServer.getClass().getSuperclass(), this.WorldServer.getClass(),
                    this.GameProfile.getClass(), this.PlayerInteractManager.getClass());
            this.EntityPlayer = this.EntityPlayerConstructor.newInstance(this.MinecraftServer, this.WorldServer,
                    this.GameProfile, this.PlayerInteractManager);
            this.id = (int) this.EntityPlayer.getClass().getMethod("getId", (Class<?>[]) new Class[0])
                    .invoke(this.EntityPlayer, new Object[0]);
            (this.setLocation = this.EntityPlayerClass.getSuperclass().getSuperclass().getSuperclass()
                    .getMethod("setLocation", Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE)).invoke(
                    this.EntityPlayer, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            this.PacketPlayOutPlayerInfo = this.getNMSClass("PacketPlayOutPlayerInfo");
            this.EnumPlayerInfoAction = this.PacketPlayOutPlayerInfo.getClasses()[1];
            this.ADD_PLAYER = this.EnumPlayerInfoAction.getField("ADD_PLAYER").get(null);
            final Object array = this.getArray(this.EntityPlayerClass, this.EntityPlayer);
            Constructor<?> Constructor = this.PacketPlayOutPlayerInfo.getConstructor(this.ADD_PLAYER.getClass(),
                    array.getClass());
            this.TabPacket = Constructor.newInstance(this.ADD_PLAYER,
                    this.getArray(this.EntityPlayerClass, this.EntityPlayer));
            this.SpawnPacket = this.getNMSClass("PacketPlayOutNamedEntitySpawn")
                    .getConstructor(this.EntityPlayer.getClass().getSuperclass()).newInstance(this.EntityPlayer);
            this.packetOutEntity = this.getNMSClass("PacketPlayOutEntity").getClasses()[0].getDeclaredConstructors()[1]
                    .newInstance(this.id, this.getFixRotation(loc.getYaw()), this.getFixRotation(loc.getPitch()), true);
            Constructor = this.getNMSClass("PacketPlayOutEntityHeadRotation")
                    .getConstructor(this.EntityPlayerClass.getSuperclass().getSuperclass().getSuperclass(), Byte.TYPE);
            this.packetHead = Constructor.newInstance(this.EntityPlayer, this.getFixRotation(loc.getYaw()));
            for (final Player p : Bukkit.getOnlinePlayers()) {
                this.sendPackets(p, this.TabPacket, this.SpawnPacket);
            }
            this.addPlayerConnection();
            this.addEntityToWorld(loc.getWorld());
            for (final Player p : Bukkit.getOnlinePlayers()) {
                this.sendPackets(p, this.packetOutEntity, this.packetHead);
            }
            this.setGameMode(GameMode.CREATIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NPC(final String name, final Location loc) {
        this(name, loc, name);
    }

    public void show(final Player p) {
        this.sendPackets(p, this.TabPacket, this.SpawnPacket);
        this.sendPackets(p, this.packetOutEntity, this.packetHead);
    }

    public Integer getEntityID() {
        return this.id;
    }

    private void sendPackets(final Player player, final Object... object) {
        try {
            final Object handle = player.getClass().getMethod("getHandle", (Class<?>[]) new Class[0]).invoke(player,
                    new Object[0]);
            final Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            final Method send = playerConnection.getClass().getMethod("sendPacket", this.getNMSClass("Packet"));
            for (final Object o : object) {
                send.invoke(playerConnection, o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLocation(final Location loc) {
        try {
            this.setLocation.invoke(this.EntityPlayer, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(),
                    loc.getPitch());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object getWorldNMS(final World world) {
        try {
            final Method getHandle = world.getClass().getMethod("getHandle", (Class<?>[]) new Class[0]);
            final Object nmsWorld = getHandle.invoke(world, new Object[0]);
            final Object World = this.getNMSClass("World").cast(nmsWorld);
            return World;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object getWorldServerNMS() {
        try {
            final Method getHandle = this.loc.getWorld().getClass().getMethod("getHandle", (Class<?>[]) new Class[0]);
            return getHandle.invoke(this.loc.getWorld(), new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object getMinecraftServerNMS() {
        try {
            final Method getHandle = Bukkit.getServer().getClass().getMethod("getServer", (Class<?>[]) new Class[0]);
            return getHandle.invoke(Bukkit.getServer(), new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Class<?> getNMSClass(final String nmsClassString) {
        final String version = String
                .valueOf(Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3]) + ".";
        final String name = "net.minecraft.server." + version + nmsClassString;
        try {
            final Class<?> nmsClass = Class.forName(name);
            return nmsClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class ProfileLoader {
        private final String uuid;
        private final String name;
        private final String skinOwner;

        public ProfileLoader(final NPC npc, final String uuid, final String name) {
            this(uuid, name, name);
        }

        public ProfileLoader(final String uuid, final String name, final String skinOwner) {
            this.uuid = ((uuid == null) ? null : uuid.replaceAll("-", ""));
            final String displayName = ChatColor.translateAlternateColorCodes('&', name);
            this.name = ChatColor.stripColor(displayName);
            this.skinOwner = skinOwner;
        }

        public GameProfile loadProfile() {
            final UUID id = (this.uuid == null) ? this.parseUUID(this.getUUID(this.name)) : this.parseUUID(this.uuid);
            final GameProfile profile = new GameProfile(id, this.name);
            this.addProperties(profile);
            return profile;
        }

        private void addProperties(final GameProfile profile) {
            final String uuid = this.getUUID(this.skinOwner);
            try {
                final URL url = new URL(
                        "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
                final URLConnection uc = url.openConnection();
                uc.setUseCaches(false);
                uc.setDefaultUseCaches(false);
                uc.addRequestProperty("User-Agent", "Mozilla/5.0");
                uc.addRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
                uc.addRequestProperty("Pragma", "no-cache");
                @SuppressWarnings("resource") final String json = new Scanner(uc.getInputStream(), "UTF-8").useDelimiter("\\A").next();
                final JSONParser parser = new JSONParser();
                final Object obj = parser.parse(json);
                final JSONArray properties = (JSONArray) ((JSONObject) obj).get((Object) "properties");
                for (int i = 0; i < properties.size(); ++i) {
                    try {
                        final JSONObject property = (JSONObject) properties.get(i);
                        final String name = (String) property.get((Object) "name");
                        final String value = (String) property.get((Object) "value");
                        final String signature = property.containsKey((Object) "signature")
                                ? ((String) property.get((Object) "signature"))
                                : null;
                        if (signature != null) {
                            profile.getProperties().put(name, new Property(name, value, signature));
                        } else {
                            profile.getProperties().put(name, new Property(value, name));
                        }
                    } catch (Exception e) {
                        Bukkit.getLogger().log(Level.WARNING, "Failed to apply auth property", e);
                    }
                }
            } catch (Exception ex) {
            }
        }

        @SuppressWarnings("deprecation")
        private String getUUID(final String name) {
            return Bukkit.getOfflinePlayer(name).getUniqueId().toString().replaceAll("-", "");
        }

        private UUID parseUUID(final String uuidStr) {
            final String[] uuidComponents = {uuidStr.substring(0, 8), uuidStr.substring(8, 12),
                    uuidStr.substring(12, 16), uuidStr.substring(16, 20), uuidStr.substring(20, uuidStr.length())};
            final StringBuilder builder = new StringBuilder();
            String[] array;
            for (int length = (array = uuidComponents).length, i = 0; i < length; ++i) {
                final String component = array[i];
                builder.append(component).append('-');
            }
            builder.setLength(builder.length() - 1);
            return UUID.fromString(builder.toString());
        }
    }
}