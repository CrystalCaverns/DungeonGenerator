package caps123987.DungeonGenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import caps123987.Commands.CommListener;
import caps123987.Commands.TabC;
import caps123987.Handers.ChestHandler;
import caps123987.Handers.JoinHandler;
import caps123987.Handers.InteractListener;
import caps123987.Managers.ChestManager;
import caps123987.Managers.EasyRoomHandler;
import caps123987.Managers.SimpleBlockManager;
import caps123987.Types.ItemWRarity;
import caps123987.Utils.RegChest;

public class DungeonGenerator extends JavaPlugin{
	
	
	public Logger logger;
	
	public static DungeonGenerator instance;
	public ChestManager chestManager;
	public EasyRoomHandler easyRoomHandler;
	
	public final int maxInv = 20;
	public File invFile;
	public File items;
	
	public List<Location> spawns = new ArrayList<Location>();
	public static List<ItemWRarity> itemsList = new ArrayList<ItemWRarity>();
	
	
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
		
		
		
		ConfigurationSerialization.registerClass(ItemWRarity.class);
		ConfigurationSerialization.registerClass(RegChest.class);
		
		invFile = new File(instance.getDataFolder(),"inv");
		
		if(!invFile.exists()) {
			invFile.mkdir();
		}
		
		itemsList = new ArrayList<ItemWRarity>();
		
		items = new File(invFile,"items.yml");
		
		if(!items.exists()) {
			try {
				items.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		loadItems();
		loadSpawns();
		
		chestManager = new ChestManager(instance);
		easyRoomHandler = new EasyRoomHandler();
		
		this.getServer().getPluginManager().registerEvents(new JoinHandler(),this);
		this.getServer().getPluginManager().registerEvents(new ChestHandler(instance),this);
		this.getServer().getPluginManager().registerEvents(new InteractListener(easyRoomHandler), this);

		getCommand("DungeonGenerator").setExecutor(new CommListener(easyRoomHandler));
		
		getCommand("DungeonGenerator").setTabCompleter(new TabC());
		
		logger.log(Level.INFO,"Loaded: "+spawns.size()+" rooms");
		
		//Bukkit.getScheduler().runTaskTimer(instance, ()->new Spawn(), 100L, 100L);
		
		
	}
	
	@SuppressWarnings("unchecked")
	public void loadItems() {
		FileConfiguration yaml=YamlConfiguration.loadConfiguration(items);
		
		if(yaml.contains("items")) {
			itemsList = (List<ItemWRarity>) yaml.getList("items");
		}else {
			itemsList = new ArrayList<ItemWRarity>();
			yaml.set("items", itemsList);
			
					
		}
		
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
