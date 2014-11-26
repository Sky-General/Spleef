package me.skyGeneral.spleef;

import java.util.UUID;

import me.skyGeneral.spleef.utils.Arena;
import me.skyGeneral.spleef.utils.Colors;
import me.skyGeneral.spleef.utils.SpleefPlayer;
import me.skyGeneral.spleef.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ListenerClass implements Listener {
	Main plugin;
	public ListenerClass(Main plugin){
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	@EventHandler
	public void onPlayerChangeBlock(BlockBreakEvent e){
		if(Utils.inGame(e.getPlayer().getUniqueId())){
			SpleefPlayer splayer = Utils.getSpleefPlayer(e.getPlayer().getUniqueId());
			Arena arena = splayer.getArena();
			arena.updateBlocks(e.getBlock().getState());
		}
	}
	@EventHandler
	public void onSignEdit(SignChangeEvent e){
		//If this is an arena sign
		if(ChatColor.stripColor(e.getLine(0)).equalsIgnoreCase("[SPLEEF]")){
			//If player has the permission, sign changes colors, and is made into an Arena Sign.
			if(e.getPlayer().hasPermission("spleef.create.sign")){
				e.setLine(0, Colors.color("&8[&9Spleef&8]"));
				if(Utils.getArenas().getString("Arena." + e.getLine(1).toLowerCase() + ".Location") == null){
					e.setLine(1, Colors.color("&c" + e.getLine(1)));
				}
				else e.setLine(1, Colors.color("&6" + e.getLine(1)));
				e.setLine(2, Colors.color("&6Max&8: &6" + Utils.getArenaMaxSize(Utils.getArena(ChatColor.stripColor(e.getLine(1)))) + ""));
			}
		}
	}
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player player = (Player) e.getEntity();
			if(e.getCause().equals(DamageCause.FIRE) || e.getCause().equals(DamageCause.LAVA)){
				Utils.makeSpectator(player);
				player.teleport(Utils.getMainSpawnLocation(Utils.getSpleefPlayer(player.getUniqueId()).getArena()));
			}
		}
		e.setCancelled(true);
	}
	@EventHandler
	public void onBlockDamager(BlockDamageEvent e){
		if(e.getBlock().getType().equals(Material.STAINED_CLAY) && Utils.inGame(e.getPlayer().getUniqueId())){
				e.setInstaBreak(true);
		}
	}
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e){
		if(e.getClickedBlock() == null) return;
		if(!(e.getClickedBlock().getState() instanceof Sign)) return;
		Sign sign = (Sign) e.getClickedBlock().getState();
		if(!ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[spleef]")) return;
		try{
			Arena a = Utils.getArena(ChatColor.stripColor(sign.getLine(1)));
			if(a.hasStarted()){
				e.getPlayer().sendMessage(Colors.color("&8[&9Spleef&8] &e" + ChatColor.stripColor(sign.getLine(1)) + "&6 has already started."));
				return;
			}
			if(a.isFull()){
				e.getPlayer().sendMessage(Colors.color("&8[&9Spleef&8] &e" + ChatColor.stripColor(sign.getLine(1)) + "&6 is full."));
				return;
			}
			SpleefPlayer splayer = Utils.getSpleefPlayer(e.getPlayer().getUniqueId());
			a.addPlayer(e.getPlayer().getUniqueId());
			a.addPlayer__DO_NOT_USE__(e.getPlayer().getUniqueId());
			splayer.setArena(a);
			Utils.inGame(e.getPlayer().getUniqueId(), true);
			String loc = Utils.getArenas().getString("Arena." + ChatColor.stripColor(sign.getLine(1)).toLowerCase() + ".Location");
			String[] locc = loc.split(":");
			World world = Bukkit.getWorld(locc[0]);
			int x = Integer.parseInt(locc[1]);
			int y = Integer.parseInt(locc[2]);
			int z = Integer.parseInt(locc[3]);
			e.getPlayer().teleport(new Location(world,x,y,z));
			splayer.getPlayer().sendMessage(Colors.color("&8[&9Spleef&8]&6 You have joined &e" + ChatColor.stripColor(sign.getLine(1)) + "&6."));
			for(UUID id : a.getPlayers()) Bukkit.getPlayer(id).sendMessage(Colors.color("&8[&9Spleef&8]&e " + e.getPlayer().getName() + "&6 has joined &e" + a.getName() + "&6. &8(&6" + a.getPlayers().size() + "&8/&6" + Utils.getArenaMaxSize(a) + "&8)"));				
			if(a.getPlayers().size() >= 2 && !a.isStarting()){
				Utils.startArena(a);
			}
			if(a.getPlayers().size() == Utils.getArenaMaxSize(a)){
				a.isFull(true);
			}
		} catch(NullPointerException ex){
			e.getPlayer().sendMessage(Colors.color("&8[&9Spleef&8]&6 No arena matches &e" + ChatColor.stripColor(sign.getLine(1)) + "&6."));
		}
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		SpleefPlayer splayer = Utils.getSpleefPlayer(e.getWhoClicked().getUniqueId());
		if(splayer.isSpectator()){
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e){
		Utils.resetPlayer(e.getPlayer());
	}
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e){
		e.setFoodLevel(20);
	}
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		e.setDeathMessage(null);
		e.getEntity().setHealth(2.0D);
		e.getEntity().setFireTicks(0);
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		e.getPlayer().teleport(Utils.getMainSpawnLocation());
		e.getPlayer().setMaxHealth(2.0D);
		e.getPlayer().setHealth(2.0D);
		
	}
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e){
		if(e.getMessage().equalsIgnoreCase("reset")){
			Utils.getSpleefPlayer(e.getPlayer().getUniqueId()).getArena().resetArena(e.getPlayer().getWorld().getSpawnLocation());
		}
	}
	
}
