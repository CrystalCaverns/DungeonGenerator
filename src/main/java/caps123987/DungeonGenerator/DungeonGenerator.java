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
import org.bukkit.scheduler.BukkitTask;

import caps123987.BossRoom.BossRoomManager;
import caps123987.Commands.AdminCommands;
import caps123987.Commands.PartyCommands;
import caps123987.Commands.AdminTabC;
import caps123987.Handers.ChestHandler;
import caps123987.Handers.JoinHandler;
import caps123987.Handers.LeaveListener;
import caps123987.Handers.InteractListener;
import caps123987.Managers.ChestManager;
import caps123987.Managers.EasyRoomHandler;
import caps123987.Managers.PartyManager;
import caps123987.Types.ItemWRarity;
import caps123987.Utils.RegChest;

public class DungeonGenerator extends JavaPlugin{
	
	
	public Logger logger;
	
	public static DungeonGenerator instance;
	public ChestManager chestManager;
	public EasyRoomHandler easyRoomHandler;
	public PartyManager partyManager;
	public BossRoomManager bossRoomManager;
	
	public final int maxInv = 20;
	public int respawnLoot;
	public File invFile;
	public File items;

	public File bossFile;
	public File boss;
	public List<Location> spawns = new ArrayList<Location>();
	public static List<ItemWRarity> chestItemsList = new ArrayList<ItemWRarity>();
	public static List<ItemWRarity> trappedChestItemsList = new ArrayList<ItemWRarity>();
	public static List<ItemWRarity> barrelItemsList = new ArrayList<ItemWRarity>();
	public static List<ItemWRarity> potItemsList = new ArrayList<ItemWRarity>();
	
	public BukkitTask asyncGenID;
	
	
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
		this.saveDefaultConfig();
		if(!this.getDataFolder().exists()) {
			this.getDataFolder().mkdir();
		}


	    FileConfiguration config = this.getConfig();
		respawnLoot = config.getInt("respawnTime");

		logger = super.getLogger();
		instance = this;
		
		
		
		ConfigurationSerialization.registerClass(ItemWRarity.class);
		ConfigurationSerialization.registerClass(RegChest.class);
		
		invFile = new File(instance.getDataFolder(),"inv");
		
		if(!invFile.exists()) {
			invFile.mkdir();
		}

		bossFile = new File(instance.getDataFolder(),"boss");

		if(!bossFile.exists()) {
			bossFile.mkdir();
		}
		
		chestItemsList = new ArrayList<ItemWRarity>();
		
		items = new File(invFile,"items.yml");

		boss = new File(bossFile,"bossroom.nbt");
		
		if(!items.exists()) {
			try {
				items.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(!boss.exists()) {
			try {
				boss.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		loadItems();
		loadSpawns();
		
		partyManager = new PartyManager();
		chestManager = new ChestManager(instance);
		easyRoomHandler = new EasyRoomHandler();
		bossRoomManager = new BossRoomManager(config.getInt("bossRoomSize"),config.getLocation("origin"),this);
		
		this.getServer().getPluginManager().registerEvents(new JoinHandler(),this);
		this.getServer().getPluginManager().registerEvents(new ChestHandler(instance),this);
		this.getServer().getPluginManager().registerEvents(new InteractListener(easyRoomHandler), this);
		this.getServer().getPluginManager().registerEvents(new LeaveListener(this), this);

		getCommand("DungeonGenerator").setExecutor(new AdminCommands(this));
		getCommand("party").setExecutor(new PartyCommands(this));
		getCommand("boss").setExecutor(bossRoomManager);
		
		getCommand("DungeonGenerator").setTabCompleter(new AdminTabC());
		
		logger.log(Level.INFO,"Loaded: "+spawns.size()+" rooms");
		
		//Bukkit.getScheduler().runTaskTimer(instance, ()->new Spawn(), 100L, 100L);
		
		
	}
	
	@SuppressWarnings("unchecked")
	public void loadItems() {
		FileConfiguration yaml=YamlConfiguration.loadConfiguration(items);
		
		if(yaml.contains("chestItems")) {
			chestItemsList = (List<ItemWRarity>) yaml.getList("chestItems");
		}else {
			chestItemsList = new ArrayList<ItemWRarity>();
			yaml.set("chestItems", chestItemsList);
		}

		if(yaml.contains("trappedChestItems")) {
			trappedChestItemsList = (List<ItemWRarity>) yaml.getList("trappedChestItems");
		}else {
			trappedChestItemsList = new ArrayList<ItemWRarity>();
			yaml.set("trappedChestItems", trappedChestItemsList);
		}

		if(yaml.contains("barrelItems")) {
			barrelItemsList = (List<ItemWRarity>) yaml.getList("barrelItems");
		}else {
			barrelItemsList = new ArrayList<ItemWRarity>();
			yaml.set("barrelItems", barrelItemsList);
		}

		if(yaml.contains("potItems")) {
			potItemsList = (List<ItemWRarity>) yaml.getList("potItems");
		}else {
			potItemsList = new ArrayList<ItemWRarity>();
			yaml.set("potItems", potItemsList);
		}
		try {
			yaml.save(items);
		} catch (IOException e) {
			e.printStackTrace();
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

	public void setNewOrigin(Location origin){
		bossRoomManager.setorigin(origin);
		FileConfiguration config = this.getConfig();
		config.set("origin",origin);
		this.saveConfig();
	}
}
