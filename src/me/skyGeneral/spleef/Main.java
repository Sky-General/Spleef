package me.skyGeneral.spleef;

import java.io.File;

import me.skyGeneral.spleef.commands.SpleefCommand;
import me.skyGeneral.spleef.utils.Utils;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public void onEnable(){
		File file = new File("plugins/Spleef");
		if(!file.isDirectory()){
			file.mkdir();
		}
		File log = new File("plugins/Spleef/Arenas.yml");
		if(!log.exists()){
			try {
				log.createNewFile();		
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ERROR! Was not able to create ARENAS file!");	
			}	
		}
		new ListenerClass(this);
		new SpleefCommand(this, "spleef");
		Utils.setPlugin(this);
	}

}
