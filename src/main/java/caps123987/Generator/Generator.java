package caps123987.Generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import caps123987.DungeonGenerator.DungeonGenerator;
import caps123987.Managers.SimpleBlockManager;
import caps123987.Room.Room;
import caps123987.Types.DunType;
import caps123987.Utils.DunUtils;
import caps123987.Utils.newVector;

public class Generator {
	Location startPos;
	Block startBlock;
	
	public DungeonGenerator instance = DungeonGenerator.getInstance();
	public SimpleBlockManager blockManager;
	
	
	//private List<Block> notSpace = new ArrayList<Block>();
	
	private int limitMax = 400;
	private int limitMin = 100;
	private int maxY = 220;
	private int minRooms = 5000;
	
	public Generator(Location startPos) {
		this.startPos = startPos;
		this.startBlock = startPos.getBlock();
		blockManager = new SimpleBlockManager(limitMax, startPos.getBlock());
		//notSpace = new ArrayList<Block>();
		
	}
	
	public Generator(Location startPos, int maxY) {
		this.maxY = maxY;
		this.startPos = startPos;
		this.startBlock = startPos.getBlock();
		blockManager = new SimpleBlockManager(limitMax, startPos.getBlock());
		//notSpace = new ArrayList<Block>();
		
	}
	
	public Generator(Location startPos, int maxY, int maxRange) {
		this.limitMax = maxRange;
		this.maxY = maxY;
		this.startPos = startPos;
		this.startBlock = startPos.getBlock();
		blockManager = new SimpleBlockManager(maxRange, startPos.getBlock());
		//notSpace = new ArrayList<Block>();
		
		
	}
	
	public Generator(Location startPos, int maxY, int maxRange, int minRooms) {
		this.limitMax = maxRange;
		this.maxY = maxY;
		this.startPos = startPos;
		this.startBlock = startPos.getBlock();
		this.minRooms  = minRooms;
		blockManager = new SimpleBlockManager(maxRange, startPos.getBlock());
		//notSpace = new ArrayList<Block>();
		
		
	}
	
	public void superStart() {
		start();

		
	}
	
	public void start() {

			instance.asyncGenID = Bukkit.getScheduler().runTaskAsynchronously(instance, ()->{
				
				List<Room> roomList=new ArrayList<Room>();
				Map<Block,newVector> entrances=new HashMap<Block,newVector>();
				
				generateMain(entrances,roomList);
				
				
				List<DunType> types = DunUtils.getRandomDunTypeList();
				//generate entrances until empty
				int run = 1;
				while(!entrances.isEmpty()) {
					Bukkit.broadcastMessage("run: "+run);
					List<Block> tempList = new ArrayList<Block>();
					
					Map<Block,newVector> tempMap = new HashMap<Block,newVector>();
					
					revolutionRun(tempList,tempMap,types,entrances,roomList);
					
					
					entrances.putAll(tempMap);
					tempMap.clear();
					tempList.forEach((Block b)->
						entrances.remove(b)
					);
					
					tempList.clear();
					
					run ++;
					
				}
				
				
				
				if(roomList.size()<minRooms) {
					Bukkit.broadcastMessage("too small, try again (size: "+roomList.size()+")");

					blockManager = null;
					roomList.clear();
					return;
				}
			
				instance.setNewOrigin(startPos);
				instance.bossRoomManager.reset();

				Bukkit.broadcastMessage("size: "+roomList.size());
				
				List<Room> temp = new ArrayList<>();
				
				//apply room
				
				
		
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(instance, ()->{
					int countRoom = 1;
					
					for(Room r:roomList) {
						
						
						
						if(!r.getType().equals(DunType.EMERGENCYSTOPWALL)) {
							
							
							Bukkit.getScheduler().scheduleSyncDelayedTask(instance, ()->{
								Bukkit.broadcastMessage("run");
								r.generatePlatfort();
								r.applyRoom();
							}, 
								(int) (Math.floor(countRoom/100.0)*6)+1
							);
							
							//Bukkit.broadcastMessage("fill run: "+(int) (Math.floor(countRoom/100.0)*10)+1);
							
							countRoom++;
							
						}else {
							temp.add(r);
						}
					}
					
					/*
					 * it may work
					 */
					
					//repair run
					
					int wait = (int) Math.floor(countRoom/100.0)*6 + 60;
					
					Bukkit.broadcastMessage(""+wait);
					
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(instance, ()->{
						repair(temp,roomList);
						for(Room r:temp){
							r.generatePlatfort();
							r.applyRoom();
						}
						blockManager = null;
						roomList.clear();
					},wait);
					
					setNewSpawns(roomList);

				});
			
			});
		
	}
	
	public void repair(List<Room> temp, List<Room> roomList) {
		int run = 1;
		for(Room r: roomList) {
			Bukkit.broadcastMessage("repair run: "+run);
			
			for(Map.Entry<Block, newVector> entry:r.getEntrances().entrySet()) {
				Block b = entry.getKey();
				
				
				newVector v = entry.getValue();
				
				Block newB = DunUtils.getRelativeByRot(b, v.getRot());
				

				/*
				 * newB.getType().equals(Material.AIR)&&newB.getRelative(0,-1,0).getType().equals(Material.AIR)
						||(!newB.getRelative(0,-1,0).getType().equals(Material.AIR)&&!newB.getType().equals(Material.AIR))
						||newB.getType().equals(Material.RED_CONCRETE)
				 */
				
				
				
				if(!blockManager.getBlock(newB.getRelative(0,0,0))) {
						
					
					
					Bukkit.broadcastMessage("repair run: "+run+" success");
					
					Room toGen = DunUtils.getRoomByEntrance(roomList, b);
						
					/*Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, ()->{
						b.getWorld().spawnParticle(Particle.REDSTONE, b.getLocation().getX(),b.getLocation().getY() +10,b.getLocation().getZ()
								, 5, 0.1 , 0.1 , 0.1 ,new DustOptions(Color.GREEN,1));
					}, 5, 5);*/
					
					if(toGen==null) {
						Room room = new Room(DunType.EMERGENCYSTOPWALL,b,v.getRot(),false);
						temp.add(room);
						
					}else {
						toGen.setType(DunType.EMERGENCYSTOPWALL);
						temp.add(toGen);
					}
				}
			}
			run++;
		}
	}
	
	public Room createRoom(DunType type,Block entrance, int rot,boolean debug,List<Block> tempList, List<Room> roomList) {
		
		Room room = new Room(type,entrance,rot,debug);
		
		boolean needToRegen = false;
		
		for(Block b :room.getBoudingBox().getBlockList(room.getBlock(), room.getRot())) {

			
			if(blockManager.getBlock(b)) { 
				needToRegen = true;
			}
			
		}
		
		if(needToRegen) {
			room.getEntrances().forEach((Block b,newVector v)->
				tempList.add(b)
			);
			
			room.setType(DunType.EMERGENCYSTOPWALL);
		}
		
		room.generateSimplePlatfort(blockManager);
		//room.applyRoom();
		roomList.add(room);
		return room;
	}
	
	public void setNewSpawns(List<Room> roomList) {
		File file = new File(DungeonGenerator.instance.getDataFolder(),"Spawns.yml");
		FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		
		List<Location> toSpawn = new ArrayList<Location>();
		
		for(Room r:roomList) {
			DunType type = r.getType();
			if(!(type.equals(DunType.EMERGENCYSTOPWALL)||type.equals(DunType.BRIDGE)||type.equals(DunType.DOOR)||type.equals(DunType.PLAY__ER5)||type.equals(DunType.LAVA_ROOM))) {
				toSpawn.add(r.getBlock().getLocation());
			}
		}
		
		yaml.set("Spawns", toSpawn);
		try {
			yaml.save(file);
		} catch (IOException e) {
			DungeonGenerator.instance.getLogger().log(Level.SEVERE, "Can't save Spawns");
		}

		FileConfiguration config = DungeonGenerator.instance.getConfig();

		config.set("origin",startPos);
		DungeonGenerator.instance.saveConfig();
		DungeonGenerator.instance.loadSpawns();
		
	}
	
	private void generateMain(Map<Block,newVector> entrances, List<Room> roomList) {
		Room starterRoom = createRoom(DunType.MAIN,startBlock,0,false,new ArrayList<>(),roomList);


		for(Map.Entry<Block, newVector> entry : starterRoom.getEntrances().entrySet()) {
			Block b=entry.getKey();
				
			Room r1 = createRoom(DunType.STRAIGHT,b,entry.getValue().getRot(),false,new ArrayList<>(),roomList);
			
			DunType[] types = DunType.values();

			for(Map.Entry<Block, newVector> entry2:r1.getEntrances().entrySet()) {
				
				
				int id = DunUtils.getRandomValue(0, types.length-1);
				
				DunType type = types[id];
				
				while(!types[id].isEnabled()) {
					id = DunUtils.getRandomValue(0, types.length-1);
					
					type = types[id];
				}
					
				Room r2 = createRoom(type,entry2.getKey(),entry2.getValue().getRot(),false,null,roomList);
				entrances.putAll(r2.getEntrances());
				
			}
		}
	}
	private void revolutionRun(List<Block> tempList, Map<Block,newVector> tempMap, List<DunType> types, Map<Block,newVector> entrances, List<Room> roomList) {
		
		for(Map.Entry<Block, newVector> entry:entrances.entrySet()) {
			
			
			int id = DunUtils.getRandomValue(0, types.size()-1);
			
			DunType type = types.get(id);
			/*while(!types[id].isEnabled()) {
				id = DunUtils.getRandomValue(0, types.length-1);
				
				type = types[id];
			}*/
			
			int distance = (int) entry.getKey().getLocation().distance(startPos);
			
			
			if(type.equals(DunType.END)&&distance<limitMin) {
				while(type.equals(DunType.END)) {
					id = DunUtils.getRandomValue(0, types.size()-1);
					
					type = types.get(id);
				}
			}
			
			if(entry.getKey().getY()>startBlock.getY()+maxY&&(type.equals(DunType.LIBRARYTOWER)||type.equals(DunType.PLAY__ER5))) {
				while(type.equals(DunType.LIBRARYTOWER)||type.equals(DunType.PLAY__ER5)) {
					id = DunUtils.getRandomValue(0, types.size()-1);
					
					type = types.get(id);
				}
				//Bukkit.broadcastMessage("to high "+type.name()+" "+entry);
			}
			
			if(distance>limitMax) {
				type = DunType.END;
			}
			
			Room r = createRoom(type,entry.getKey(),entry.getValue().getRot(),false,tempList,roomList);
			
			
			tempMap.putAll(r.getEntrances());
			tempList.add(entry.getKey());
			
		}
	}
	
}
