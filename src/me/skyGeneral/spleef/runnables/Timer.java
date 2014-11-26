package me.skyGeneral.spleef.runnables;

import java.util.UUID;

import me.skyGeneral.spleef.Main;
import me.skyGeneral.spleef.utils.Arena;
import me.skyGeneral.spleef.utils.Colors;

import org.bukkit.Bukkit;

public class Timer implements Runnable {
	Main plugin;
	int i;
	Arena a;
	public Timer(Main pluign, Arena a, int i){
		this.plugin = pluign;
		this.a = a;
		this.i = i;
	}
	@Override
	public void run() {
		if(a.getPlayers().size()< 2){
			for(UUID id : a.getPlayers()) Bukkit.getPlayer(id).sendMessage(Colors.color("&8[&9Spleef&8] &6Not enough players. Count down has stopped."));
			return;
		}
		if(i>=2){
			for(UUID id : a.getPlayers()) Bukkit.getPlayer(id).sendMessage(Colors.color("&8[&9Spleef&8]&e " + a.getName() + "&6 is starting in &e" + i + "&6 seconds..."));
			Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Timer(plugin,a,(i-1)), 20);
			return;
		}
		if(i==1){
			a.isStarting(true);
			for(UUID id : a.getPlayers()) Bukkit.getPlayer(id).sendMessage(Colors.color("&8[&9Spleef&8]&e " + a.getName() + "&6 is starting in &e" + i + "&6 second..."));
			Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Timer(plugin,a,(i-1)), 20);
			return;
		}
		if(i==0){
			for(UUID id : a.getPlayers()) Bukkit.getPlayer(id).sendMessage(Colors.color("&8[&9Spleef&8]&e " + a.getName() + "&6 is starting now!"));
			Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new StartArena(a), 20);
			return;
		}
	}

}
