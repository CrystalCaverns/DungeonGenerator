package caps123987.DungeonGenerator;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import caps123987.Commands.CommListener;

public class DungeonGenerator extends JavaPlugin{
	
	
	public Logger logger;
	
	@Override
	public void onEnable() {
		logger = super.getLogger();
		
		getCommand("DungeonGenerator").setExecutor(new CommListener());
	}
}
