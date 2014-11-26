package me.skyGeneral.spleef.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;

public class Arena {
	 String arena;
	 int p;
	 boolean starting = false;
	 List<UUID> players = new ArrayList<UUID>();
	 List<UUID> playerss = new ArrayList<UUID>();
	 List<BlockState> blocks = new ArrayList<BlockState>();
	 boolean started;
	 boolean full;
	public Arena(String arena){
		this.arena = arena;
	}
	public boolean hasStarted(){
		return started;
	}
	public void hasStarted(Boolean t){
		this.started = t;
	}
	public boolean isFull(){
		return full;
	}
	public void isFull(Boolean t){
		this.full = t;
	}
	public String getName(){
		return arena;
	}
	public List<UUID> getPlayers(){
		return players;
	}
	public void addPlayer__DO_NOT_USE__(UUID id){
		playerss.add(id);
	}
	public void removePlayer__DO_NOT_USE__(UUID id){
		playerss.remove(id);
	}
	public void addPlayer(UUID id){
		if(!players.contains(id)){
			players.add(id);
		} else {
			Logger.getLogger("minecraft").log(Level.WARNING, "Player already in that arena!");
		}
	}
	public void resetArena(Location loc){
		players.clear();
		for(UUID id : playerss){
			Bukkit.getPlayer(id).teleport(loc);
			Utils.inGame(id, false);
			Utils.resetPlayer(Bukkit.getPlayer(id));
		}
		playerss.clear();
		for(BlockState state : blocks){
			state.update(true);
		}
		blocks.clear();
	}
	public void setPlayers(int p){
		this.p = p;
	}
	public void updateBlocks(BlockState blockState) {
		blocks.add(blockState);
	}
	public void removePlayer(UUID uniqueId) {
		players.remove(uniqueId);
		
	}
	public void isStarting(Boolean bool){
		this.starting = bool;
	}
	public boolean isStarting() {
		return starting;
	}

}
