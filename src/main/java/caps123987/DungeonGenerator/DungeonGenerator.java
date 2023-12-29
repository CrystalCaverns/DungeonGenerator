package caps123987.DungeonGenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import caps123987.ChunkGeneration.VoidChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
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

	public File bossFile;
	public File boss;
	public List<Location> floor1Spawns = new ArrayList<Location>();
	public List<Location> floor2Spawns = new ArrayList<Location>();
	public List<Location> floor3Spawns = new ArrayList<Location>();

	public static Map<String, Map<String, List<ItemWRarity>>> invMap = new HashMap<>();
	
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

		boss = new File(bossFile,"bossroom.nbt");

		if(!boss.exists()) {
			try {
				boss.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		createWorlds();
		loadItems();
		loadSpawns();

		partyManager = new PartyManager();
		chestManager = new ChestManager(instance);
		easyRoomHandler = new EasyRoomHandler();
		bossRoomManager = new BossRoomManager(config.getInt("bossRoomSize"),new Location(Bukkit.getWorld("boss_room"),0,0,0),this);
		
		this.getServer().getPluginManager().registerEvents(new JoinHandler(),this);
		this.getServer().getPluginManager().registerEvents(new ChestHandler(instance),this);
		this.getServer().getPluginManager().registerEvents(new InteractListener(easyRoomHandler), this);
		this.getServer().getPluginManager().registerEvents(new LeaveListener(this), this);

		getCommand("DungeonGenerator").setExecutor(new AdminCommands(this));
		getCommand("party").setExecutor(new PartyCommands(this));
		getCommand("boss").setExecutor(bossRoomManager);
		
		getCommand("DungeonGenerator").setTabCompleter(new AdminTabC());
		
		logger.log(Level.INFO,"Floor1 Loaded: "+floor1Spawns.size()+" rooms");
		logger.log(Level.INFO,"Floor2 Loaded: "+floor2Spawns.size()+" rooms");
		logger.log(Level.INFO,"Floor3 Loaded: "+floor3Spawns.size()+" rooms");

		//Bukkit.getScheduler().runTaskTimer(instance, ()->new Spawn(), 100L, 100L);
		
		
	}
	public void createWorlds(){
		String[] worlds = {"floor_1","floor_2","floor_3","boss_room"};

		for(String worldName:worlds) {
			WorldCreator worldCreator = new WorldCreator(worldName);
			worldCreator.generator(new VoidChunkGenerator());
			worldCreator.createWorld();

			Bukkit.getWorld(worldName).setGameRule(GameRule.DO_MOB_SPAWNING, false);
			Bukkit.getWorld(worldName).setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
			Bukkit.getWorld(worldName).setGameRule(GameRule.DO_WEATHER_CYCLE, false);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadItems() {

		File floor1ItemsFile = new File(DungeonGenerator.instance.getDataFolder(),"items/floor_1.yml");
		File floor2ItemsFile = new File(DungeonGenerator.instance.getDataFolder(),"items/floor_2.yml");
		File floor3ItemsFile = new File(DungeonGenerator.instance.getDataFolder(),"items/floor_3.yml");

		loadSpecificItems(floor1ItemsFile, "floor_1");
		loadSpecificItems(floor2ItemsFile, "floor_2");
		loadSpecificItems(floor3ItemsFile, "floor_3");



	}
	
	@SuppressWarnings("unchecked")
	public void loadSpawns() {
		File file1Spawns = new File(DungeonGenerator.instance.getDataFolder(),"floor_1.yml");
		File file2Spawns = new File(DungeonGenerator.instance.getDataFolder(),"floor_2.yml");
		File file3Spawns = new File(DungeonGenerator.instance.getDataFolder(),"floor_3.yml");

		loadSpecificSpawns(file1Spawns,floor1Spawns);
		loadSpecificSpawns(file2Spawns,floor2Spawns);
		loadSpecificSpawns(file3Spawns,floor3Spawns);
	}
	
	public static void saveFile(File file,FileConfiguration yaml) {
		try {
			yaml.save(file);
		} catch (IOException e) {}
	}
	
	public static DungeonGenerator getInstance() {
		return instance;
	}

	private void loadSpecificSpawns(File file, List<Location> list) {

		if(!file.exists()) {
			list.add(Bukkit.getWorld("world").getSpawnLocation());
			try {
				file.createNewFile();
			} catch (IOException e) {}
			return;
		}

		try {
			FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
			list = (List<Location>) yaml.getList("Spawns");
			saveFile(file,yaml);
		}catch(Exception e) {
			list.add(Bukkit.getWorld("world").getSpawnLocation());
		}
	}

	private void loadSpecificItems(File file, String key) {
		FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);

		Map<String, List<ItemWRarity>> map = new HashMap<>();

		if(yaml.contains("chestItems")) {
			map.put("CHEST",(List<ItemWRarity>) yaml.getList("chestItems"));
		}else {
			map.put("CHEST", new ArrayList<ItemWRarity>());
			yaml.set("chestItems", map.get("CHEST"));
		}

		if(yaml.contains("trappedChestItems")) {
			map.put("TRAPPED_CHEST",(List<ItemWRarity>) yaml.getList("trappedChestItems"));
		}else {
			map.put("TRAPPED_CHEST", new ArrayList<ItemWRarity>());
			yaml.set("trappedChestItems", map.get("TRAPPED_CHEST"));
		}


		if(yaml.contains("potItems")) {
			map.put("DECORATED_POT",(List<ItemWRarity>) yaml.getList("potItems"));
		}else {
			map.put("DECORATED_POT",new ArrayList<ItemWRarity>());
			yaml.set("potItems", map.get("DECORATED_POT"));
		}
		try {
			yaml.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		invMap.put(key,map);
	}
}

//execute as @s in minecraft:floor_1 run tp 0 0 0
