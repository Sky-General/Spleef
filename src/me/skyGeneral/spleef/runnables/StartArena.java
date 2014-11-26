package me.skyGeneral.spleef.runnables;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.skyGeneral.spleef.utils.Arena;
import me.skyGeneral.spleef.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StartArena implements Runnable {
	Arena a;
	public StartArena(Arena a){
		this.a = a;
	}
	@Override
	public void run() {
		a.hasStarted(true);
		a.isStarting(false);
		List<Location> locs = new ArrayList<Location>();
		for(String location : Utils.getPlayerSpawns(a)){
			String[] r = location.split(":");
			locs.add(new Location(Bukkit.getWorld(r[0]), Integer.parseInt(r[1]), Integer.parseInt(r[2]), Integer.parseInt(r[3])));
		}
		int i=0;
		for(UUID id : a.getPlayers()){
			Bukkit.getPlayer(id).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000000, 1));
			Bukkit.getPlayer(id).teleport(locs.get(i));
			
			i=i+1;
			
		}

	}

}
