package caps123987.Generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.structure.Structure;

import caps123987.DungeonGenerator.DungeonGenerator;
import caps123987.Room.Room;
import caps123987.Types.DunType;
import caps123987.Utils.DunUtils;
import caps123987.Utils.newVector;

public class GenStart {
	Location startPos;
	Block startBlock;
	
	public DungeonGenerator instance = DungeonGenerator.getInstance();
	
	private List<Room> roomList=new ArrayList<Room>();
	private Map<Block,newVector> entrances=new HashMap<Block,newVector>();
	//private List<Block> notSpace = new ArrayList<Block>();
	
	private int limitMax = 400;
	private int limitMin = 100;
	private int maxY = 220;
	private int minRooms = 5000;
	
	public GenStart(Location startPos) {
		this.startPos = startPos;
		this.startBlock = startPos.getBlock();
		//notSpace = new ArrayList<Block>();
		
	}
	
	public GenStart(Location startPos, int maxY) {
		this.maxY = maxY;
		this.startPos = startPos;
		this.startBlock = startPos.getBlock();
		//notSpace = new ArrayList<Block>();
		
	}
	
	public GenStart(Location startPos, int maxY, int maxRange) {
		this.limitMax = maxRange;
		this.maxY = maxY;
		this.startPos = startPos;
		this.startBlock = startPos.getBlock();
		//notSpace = new ArrayList<Block>();
		
		
	}
	
	public GenStart(Location startPos, int maxY, int maxRange, int minRooms) {
		this.limitMax = maxRange;
		this.maxY = maxY;
		this.startPos = startPos;
		this.startBlock = startPos.getBlock();
		this.minRooms  = minRooms;
		//notSpace = new ArrayList<Block>();
		
		
	}
	
	public void superStart() {
		start();
		
		setNewSpawns();
	}
	
	public void start() {
		
		
		generateMain();
		
		
		
		List<DunType> types = DunUtils.getRandomDunTypeList();
		//generate entrances until empty
		int run = 1;
		while(!entrances.isEmpty()) {
			Bukkit.broadcastMessage("run: "+run);
			List<Block> tempList = new ArrayList<Block>();
			Map<Block,newVector> tempMap = new HashMap<Block,newVector>();
			
			
			revolutionRun(tempList,tempMap,types);
			
			
			entrances.putAll(tempMap);
			tempMap.clear();
			tempList.forEach((Block b)->{
				entrances.remove(b);
			});
			
			tempList.clear();
			
			run ++;
			
		}
		
		if(roomList.size()<minRooms) {
			Bukkit.broadcastMessage("too small, try again (size: "+roomList.size()+") Cleaning please wait");
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(instance, ()->{
				for(Room r:roomList) {
					r.generatePlatform(Material.AIR);
				}
				Bukkit.broadcastMessage("Cleaned");
			},1L);
			
			return;
		}
		
		Bukkit.broadcastMessage("size: "+roomList.size());
		
		List<Room> temp = new ArrayList<>();
		
		run =1;
		//apply room
		
		int countRoom = 1;

		
		for(Room r:roomList) {
			
			
			
			if(!r.getType().equals(DunType.EMERGENCYSTOPWALL)) {
				
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(instance, ()->{
					r.applyRoom();
				}, 
					(int) (Math.floor((double)countRoom/1000.0)*2)+1
				);
				
				Bukkit.broadcastMessage("fill run: "+(int) (Math.floor((double)countRoom/1000.0)*2)+1);
				
				countRoom++;
				
			}else {
				temp.add(r);
			}
			run++;
		}
		
		/*
		 * it may work
		 */
		
		//repair run
		
		int wait = (int) Math.floor((double)countRoom/1000.0)*2 + 10;
		
		Bukkit.broadcastMessage(""+wait);
		
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(instance, ()->{
			repair(temp);
			for(Room r:temp){
				r.generatePlatfort();
				r.applyRoom();
			}
			
		},wait);
		
		
	}
	
	public void repair(List<Room> temp) {
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
				
				
				
				if(newB.getRelative(0,0,0).getType().equals(Material.AIR)||newB.getRelative(0,0,0).getType().equals(Material.YELLOW_CONCRETE)/*||newB.getType().isSolid()*/) {
						
					
					
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
	
	public Room createRoom(DunType type,Block entrance, int rot,boolean debug,List<Block> tempList) {
		
		Room room = new Room(type,entrance,rot,debug);
		
		boolean needToRegen = false;
		
		for(Block b :room.getBoudingBox().getBlockList(room.getBlock(), room.getRot())) {

			
			if(!b.getType().equals(Material.AIR)) { 
				needToRegen = true;
			}
			
		}
		
		if(needToRegen) {
			room.getEntrances().forEach((Block b,newVector v)->{
				tempList.add(b);
			});
			
			room.setType(DunType.EMERGENCYSTOPWALL);
		}
		
		room.generatePlatfort();
		//room.applyRoom();
		roomList.add(room);
		return room;
	}
	
	public void setNewSpawns() {
		File file = new File(DungeonGenerator.instance.getDataFolder(),"Spawns.yml");
		FileConfiguration yaml=YamlConfiguration.loadConfiguration(file);
		
		List<Location> toSpawn = new ArrayList<Location>();
		
		for(Room r:roomList) {
			DunType type = r.getType();
			if(!(type.equals(DunType.EMERGENCYSTOPWALL)||type.equals(DunType.BRIDGE)||type.equals(DunType.DOOR)||type.equals(DunType.PLAY__ER5))) {
				toSpawn.add(r.getBlock().getLocation());
			}
		}
		
		yaml.set("Spawns", toSpawn);
		try {
			yaml.save(file);
		} catch (IOException e) {}
		
		DungeonGenerator.instance.loadSpawns();
		
	}
	
	private void generateMain() {
		Room starterRoom = createRoom(DunType.MAIN,startBlock,0,false,new ArrayList<>());


		for(Map.Entry<Block, newVector> entry : starterRoom.getEntrances().entrySet()) {
			Block b=entry.getKey();
				
			Room r1 = createRoom(DunType.STRAIGHT,b,entry.getValue().getRot(),false,new ArrayList<>());
			
			DunType[] types = DunType.values();

			for(Map.Entry<Block, newVector> entry2:r1.getEntrances().entrySet()) {
				
				
				int id = DunUtils.getRandomValue(0, types.length-1);
				
				DunType type = types[id];
				
				while(!types[id].isEnabled()) {
					id = DunUtils.getRandomValue(0, types.length-1);
					
					type = types[id];
				}
					
				Room r2 = createRoom(type,entry2.getKey(),entry2.getValue().getRot(),false,null);
				entrances.putAll(r2.getEntrances());
				
			}
		}
	}
	private void revolutionRun(List<Block> tempList, Map<Block,newVector> tempMap, List<DunType> types) {
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
			
			Room r = createRoom(type,entry.getKey(),entry.getValue().getRot(),false,tempList);
			
			
			tempMap.putAll(r.getEntrances());
			tempList.add(entry.getKey());
			
		}
	}
	
}
