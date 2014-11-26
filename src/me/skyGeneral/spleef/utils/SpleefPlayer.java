package me.skyGeneral.spleef.utils;

import org.bukkit.entity.Player;

public class SpleefPlayer {
	private Player player;
	private boolean spectator;
	private Arena arena = null;
	public SpleefPlayer(Player player){
		this.player = player;
	}
	public Player getPlayer(){
		return player;
	}
	public void setArena(Arena arena){
		this.arena = arena;
	}
	public Arena getArena(){
		return arena;
	}
	public boolean isSpectator() {
		return spectator;
	}
	public void setSpectator(boolean spectator) {
		this.spectator = spectator;
	}
	public void sendMessage(String m){
		player.sendMessage(Colors.color(m));
	}

}
