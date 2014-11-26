package me.skyGeneral.spleef.commands;

import me.skyGeneral.spleef.Main;
import me.skyGeneral.spleef.utils.Colors;
import me.skyGeneral.spleef.utils.Utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpleefCommand implements CommandExecutor {
	Main plugin;
	public SpleefCommand(Main plugin, String cmd){
		this.plugin = plugin;
		plugin.getCommand(cmd).setExecutor(this);
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player){
			Player player = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("spleef")){
				if(args.length>=1){
					if(args[0].equalsIgnoreCase("create")){
						if(args.length == 3){
							Utils.getArenas().set("Arena." + args[1].toLowerCase() + ".MaxPlayers", Integer.parseInt(args[2]));
							Utils.getArenas().set("Arena." + args[1].toLowerCase() + ".Location", player.getWorld().getName() + ":" + (int) player.getLocation().getX() + ":" + (int) player.getLocation().getY() + ":" + (int) player.getLocation().getZ());
							Utils.saveArenas();
							player.sendMessage(Colors.color("&8[&9Spleef&8]&6 Created arena &e" + args[1] + "&9."));
							return true;
						}
					}
					if(args[0].equalsIgnoreCase("addspawn")){
						if(args.length == 2){
							if(Utils.getArenas().getString("Arena." + args[1].toLowerCase() + ".Location") == null){
								player.sendMessage(Colors.color("&8[&9Spleef&8]&e " + args[1] + "&6 is not a known arena."));
								return true;
							} else {
								Utils.addPlayerSpawn(Utils.getArena(args[1]), player.getLocation());
								player.sendMessage(Colors.color("&8[&9Spleef&8]&6 Added spawn point for &e" + args[1]));
								return true;
							}
							
						}
					}
				}
				player.sendMessage(Colors.color("&6------&e------&8------[&9Spleef&8]------&e------&6------"));
				player.sendMessage("");
				player.sendMessage("");
				player.sendMessage(Colors.color("&6Create Arena&8:&e /spleef create [arena] [maxplayers]"));
				player.sendMessage("");
				player.sendMessage("");
				player.sendMessage(Colors.color("&6Add Player Spawn&8:&e /spleef addspawn [arena]"));
				player.sendMessage("");
				player.sendMessage("");
				player.sendMessage(Colors.color("&6------&e------&8-------------------&e------&6------"));
			}
		}
		return true;
	}

}
