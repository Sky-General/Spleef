package me.skyGeneral.spleef.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.skyGeneral.spleef.Main;
import me.skyGeneral.spleef.runnables.Timer;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Utils {
	private static Location spawn = Bukkit.getWorld("world").getSpawnLocation();
	private static Main plugin;
	private static Map<UUID, SpleefPlayer> spleefPlayers = new HashMap<UUID, SpleefPlayer>();
	private static List<UUID> ingame = new ArrayList<UUID>();
	private static Map<String, Arena> arenas = new HashMap<String, Arena>();
	private static YamlConfiguration yml = YamlConfiguration.loadConfiguration(new File("plugins/Spleef/Arenas.yml"));
	public static YamlConfiguration getArenas(){
		return yml;
	}
	public static void saveArenas(){
		try {
			yml.save(new File("plugins/Spleef/Arenas.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static boolean inGame(UUID id){
		if(ingame.contains(id)) return true;
		return false;
	}
	public static SpleefPlayer getSpleefPlayer(UUID id){
		if(!spleefPlayers.containsKey(id)) return createSpleefPlayer(id);
		else return spleefPlayers.get(id);	
	}
	public static void inGame(UUID id, Boolean f){
		if(f) ingame.add(id);
		if(!f) ingame.remove(id);
	}
	public static SpleefPlayer createSpleefPlayer(UUID id){
		if(!spleefPlayers.containsKey(id)){
			spleefPlayers.put(id, new SpleefPlayer(Bukkit.getPlayer(id)));
			return spleefPlayers.get(id);
		} else {
			Logger.getLogger("minecraft").log(Level.WARNING, "SpleefPlayer already exists");
			return spleefPlayers.get(id);
		}
	}
	public static Arena getArena(String arena) {
		if(!arenas.containsKey(arena)){
			Arena a = new Arena(arena);
			arenas.put(arena, a);
			return a;
		}
		return arenas.get(arena);
	}
	public static void startArena(Arena a) {
		for(UUID id : a.getPlayers()) Bukkit.getPlayer(id).sendMessage(Colors.color("&8[&9Spleef&8]&e " + a.getName() + " &6is starting in &e30&6 seconds.."));
		Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Timer(plugin,a,5), 600);
	}
	public static Main getPlugin() {
		return plugin;
	}
	public static void setPlugin(Main p) {
		plugin = p;
	}
	public static void makeSpectator(Player player) {
		player.setGameMode(GameMode.CREATIVE);
		Utils.getSpleefPlayer(player.getUniqueId()).setSpectator(true);
		player.sendMessage(Colors.color("&8[&9Spleef&8] &6You just died!"));
		Arena a = getSpleefPlayer(player.getUniqueId()).getArena();
		a.removePlayer(player.getUniqueId());
		SpleefPlayer splayer = getSpleefPlayer(player.getUniqueId());
		inGame(player.getUniqueId(), false);
		splayer.sendMessage("&8[&9Spleef&8]&e You&6 are now spectating.");
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10000000, 1));
		checkArena(a);
		for(UUID id : a.getPlayers()) Bukkit.getPlayer(id).sendMessage(Colors.color("&8[&9Spleef&8]&e " + player.getName() + "&6 just died! &e" + a.getPlayers().size() + "&6 players left!"));
		
	}
	public static int getArenaMaxSize(Arena a) {
		return getArenas().getInt("Arena." + a.getName().toLowerCase() + ".MaxPlayers");
	}
	public static Location getMainSpawnLocation() {
		return spawn;
	}
	public static void setMainSpawnLocation(Location loc){
		spawn = loc;
	}
	public static void resetPlayer(Player player) {
		SpleefPlayer splayer = Utils.getSpleefPlayer(player.getUniqueId());
		splayer.setArena(null);
		splayer.setSpectator(false);
		try{
			splayer.getArena().removePlayer(player.getUniqueId());
		} catch(Exception ex){
			
		}
		Utils.inGame(player.getUniqueId(), false);
		player.setGameMode(GameMode.SURVIVAL);
		player.getInventory().clear();
		player.setMaxHealth(2.0D);
		player.setHealth(2.0D);
		player.setFireTicks(0);
		for(PotionEffect e : player.getActivePotionEffects()) player.removePotionEffect(e.getType());
	}
	public static Location getMainSpawnLocation(Arena arena) {
		String[] d = getArenas().getString("Arena." + arena.getName().toLowerCase() + ".Location").split(":");
		int x = Integer.parseInt(d[1]);
		int y = Integer.parseInt(d[2]);
		int z = Integer.parseInt(d[3]);
		World w = Bukkit.getWorld(d[0]);
		return new Location(w,x,y,z);
	}
	public static List<String> getPlayerSpawns(Arena a){
		return getArenas().getStringList("Arena." + a.getName().toLowerCase() + ".PlayerSpawns");
	}
	public static void addPlayerSpawn(Arena a, Location loc){
		List<String> l = getArenas().getStringList("Arena." + a.getName().toLowerCase() + ".PlayerSpawns");
		String w = loc.getWorld().getName();
		int x = (int) loc.getX();
		int y = (int) loc.getY();
		int z = (int) loc.getZ();
		l.add(w + ":" + x + ":" + y + ":" + z);
		getArenas().set("Arena." + a.getName().toLowerCase() + ".PlayerSpawns", l);
		saveArenas();
	}
	public static void checkArena(Arena a){
		if(a.getPlayers().size() != 1) return;
		Player player = null;
		for(UUID id : a.getPlayers()) player = Bukkit.getPlayer(id);
		Bukkit.broadcastMessage(Colors.color("&8[&9Spleef&8]&e " + player.getName() + "&6 has won on arena &e" + a.getName() + "&6!"));
		a.resetArena(getMainSpawnLocation());
	}

}
