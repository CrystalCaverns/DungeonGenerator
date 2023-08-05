package caps123987.DungeonGenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import caps123987.Commands.CommListener;
import caps123987.Commands.TabC;
import caps123987.Handers.ChestHandler;
import caps123987.Handers.JoinHandler;
import caps123987.Managers.ChestManager;
import caps123987.Utils.RegChest;

public class DungeonGenerator extends JavaPlugin{
	
	
	public Logger logger;
	
	public static DungeonGenerator instance;
	public ChestManager chestManager;
	
	public File invFile;
	
	public static List<Location> spawns = new ArrayList<Location>();
	
	@Override
	public void onEnable() {
		
		/**To eddit Room you need to edit it in DunType enum
		*firts you need to add your own "section"
		*first argument is if section is enabled 
		*second is material for dubuging
		*third is bounding box-area of section-must be 1 block smalle on side where entrance is
		*fourth is entrance usualy 0 0 -4
		*
		*fifth are exits from section
		*then you put newVector class witch is youst like Vector but fourth parameter is degree
		* - set only only to 90 180 270, for examle: 4,0,0,270 means 4 block from center to right and the dungeon neeto to rotate 90 degees
		**/
		if(!this.getDataFolder().exists()) {
			this.getDataFolder().mkdir();
		}
		logger = super.getLogger();
		instance = this;
		
		ConfigurationSerialization.registerClass(RegChest.class);
		
		invFile = new File(instance.getDataFolder(),"inv");
		
		if(!invFile.exists()) {
			invFile.mkdir();
		}

		
		loadSpawns();
		
		chestManager = new ChestManager(instance);
		
		this.getServer().getPluginManager().registerEvents(new JoinHandler(),this);
		this.getServer().getPluginManager().registerEvents(new ChestHandler(instance),this);

		getCommand("DungeonGenerator").setExecutor(new CommListener());
		getCommand("DungeonGenerator").setTabCompleter(new TabC());
		
		logger.log(Level.INFO,spawns.size()+"");
	}
	
	@SuppressWarnings("unchecked")
	public void loadSpawns() {
		File file = new File(DungeonGenerator.instance.getDataFolder(),"Spawns.yml");
		
		if(!file.exists()) {
			spawns.add(Bukkit.getWorld("world").getSpawnLocation());
			try {
				file.createNewFile();
			} catch (IOException e) {}
			return;
		}
		
		try {
			FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
			spawns = (List<Location>) yaml.getList("Spawns");
			saveFile(file,yaml);
		}catch(Exception e) {
			spawns.add(Bukkit.getWorld("world").getSpawnLocation());
		}
	}
	
	public static void saveFile(File file,FileConfiguration yaml) {
		try {
			yaml.save(file);
		} catch (IOException e) {}
	}
	
	public static DungeonGenerator getInstance() {
		return instance;
	}
}
