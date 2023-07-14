package caps123987.Generator;

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
import caps123987.DungeonGenerator.DungeonGenerator;
import caps123987.Room.Room;
import caps123987.Types.DunType;
import caps123987.Utils.DunUtils;
import caps123987.Utils.newVector;

public class GenStart {
	Location startPos;
	Block startBlock;
	int size;
	
	public DungeonGenerator instance = DungeonGenerator.getInstance();
	
	private List<Room> roomList=new ArrayList<Room>();
	private Map<Block,newVector> entrances=new HashMap<Block,newVector>();
	
	private int limitMax = 150;
	private int limitMin = 100;
	
	
	public GenStart(Location startPos,int size) {
		this.size=size;
		this.startPos = startPos;
		this.startBlock = startPos.getBlock();
		start();
	}
	
	public void start() {
		
		/*List<RoomMap> map = new ArrayList<RoomMap>();
		for(newVector v:DunType.MAIN.getEntrances()) {
			DunType[] types = DunType.values();
			
			int id = DunUtils.getRandomValue(0, types.length-1);
			
			DunType type = types[id];
			while(!types[id].isEnabled()) {
				id = DunUtils.getRandomValue(0, types.length-1);
				
				type = types[id];
			}
			
		}
		
		new RoomMap(DunType.MAIN);*/
		
		
		
		
		/*DunUtils.getRelative(startBlock,  new Vector(1,0,0)).setType(Material.OAK_PLANKS); //east
		DunUtils.getRelative(startBlock, DunUtils.rotate( new Vector(1,0,0), 90)).setType(Material.ACACIA_PLANKS);//south
		DunUtils.getRelative(startBlock, DunUtils.rotate( new Vector(1,0,0), 180)).setType(Material.DARK_OAK_PLANKS);//west
		DunUtils.getRelative(startBlock, DunUtils.rotate( new Vector(1,0,0), 270)).setType(Material.MANGROVE_PLANKS);//north*/
		
		Room starterRoom = createRoom(DunType.MAIN,startBlock,0,false,new ArrayList<>());

		for(Map.Entry<Block, newVector> entry : starterRoom.getEntrances().entrySet()) {
			Block b=entry.getKey();
				
			Room r = createRoom(DunType.STRAIGHTLONG,b,entry.getValue().getRot(),false,new ArrayList<>());
				
			
			DunType[] types = DunType.values();

			for(Map.Entry<Block, newVector> entry2:r.getEntrances().entrySet()) {
				
				
				int id = DunUtils.getRandomValue(0, types.length-1);
				
				DunType type = types[id];
				while(!types[id].isEnabled()) {
					id = DunUtils.getRandomValue(0, types.length-1);
					
					type = types[id];
				}
					
				Room r3 = createRoom(type,entry2.getKey(),entry2.getValue().getRot(),false,null);
				entrances.putAll(r3.getEntrances());
				
			}
		}
		
		DunType[] types = DunType.values();
		
		while(!entrances.isEmpty()) {
			
			List<Block> tempList = new ArrayList<Block>();
			Map<Block,newVector> tempMap=new HashMap<Block,newVector>();
			
			for(Map.Entry<Block, newVector> entry:entrances.entrySet()) {
				
				int id = DunUtils.getRandomValue(0, types.length-1);
				
				DunType type = types[id];
				while(!types[id].isEnabled()) {
					id = DunUtils.getRandomValue(0, types.length-1);
					
					type = types[id];
				}
				int distance = (int) entry.getKey().getLocation().distance(startPos);
				
				
				if(type.equals(DunType.END)&&distance<limitMin) {
					while(!types[id].isEnabled()||type.equals(DunType.END)) {
						id = DunUtils.getRandomValue(0, types.length-1);
						
						type = types[id];
					}
				}
				
				if(distance>limitMax) {
					type = DunType.END;
				}
				
				Room r = createRoom(type,entry.getKey(),entry.getValue().getRot(),false,tempList);
				tempMap.putAll(r.getEntrances());
				tempList.add(entry.getKey());
			}
			entrances.putAll(tempMap);
			tempMap.clear();
			tempList.forEach((Block b)->{
				entrances.remove(b);
			});
			
			tempList.clear();
			
			
		}
		
		
		
		List<Room> temp = new ArrayList<>();
		
		for(Room r:roomList) {
			
			for(Map.Entry<Block, newVector> entry:r.getEntrances().entrySet()) {
				Block b = entry.getKey();
				newVector v = entry.getValue();
				
				Block newB = DunUtils.getRelativeByRot(b, v.getRot());
				if(newB.getType().equals(Material.AIR)&&newB.getRelative(0,-1,0).getType().equals(Material.AIR)) {
						
					Room toGen = DunUtils.getRoomByEntrance(roomList, b);
						
					Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, ()->{
						b.getWorld().spawnParticle(Particle.REDSTONE, b.getLocation().getX(),b.getLocation().getY() +10,b.getLocation().getZ()
								, 5, 0.1 , 0.1 , 0.1 ,new DustOptions(Color.GREEN,1));
					}, 5, 5);
					
					if(toGen==null) {
						Room room = new Room(DunType.EMERGENCYSTOPWALL,b,v.getRot(),false);
						temp.add(room);
						continue;
					}
					
					toGen.setType(DunType.EMERGENCYSTOPWALL);
					
					temp.add(toGen);
				}
					
				//newB.getRelative(0, 8, 0).setType(Material.COMMAND_BLOCK);
			}
			
			if(!r.getType().equals(DunType.EMERGENCYSTOPWALL)) {
				r.applyRoom();
			}else {
				temp.add(r);
			}
		}
		
		
		
		temp.forEach((Room r)->{
			Bukkit.getScheduler().scheduleSyncDelayedTask(instance, ()->{
				r.applyRoom();
			},10);
		});
		
	}
	
	public Room createRoom(DunType type,Block entrance, int rot,boolean debug,List<Block> tempList) {
		Room room = new Room(type,entrance,rot,debug);
		
		
		boolean needToRegen = false;
		for(Block b :room.getBoudingBox().getBlockList(room.getBlock(), room.getRot())) {
			
			if(!b.getType().equals(Material.AIR)) {
				needToRegen = true;
			}
			
			/*for(Room r:roomList) {
				Block roomCenter =r.getBlock();
				if(origoCenter.getLocation().distance(roomCenter.getLocation())<=20) {
					List<Block> list2 = r.getBoudingBox().getBlockList(r.getEntrance(), r.getRot());
					if(list2.contains(b)) {
						needToRegen = true;
					}
				}
			}*/
		}
		
		
		/*for(Map.Entry<Block, newVector> entry:room.getEntrances().entrySet()) {
			Block b = entry.getKey();
			for(Room r:roomList) {
				List<Block> list2 =  r.getType().getBoudingBox().getBlockList(r.getBlock(), r.getRot());
				if(list2.contains(b.getRelative(0, -1, 0))||list2.contains(b.getRelative(0, -1, 0))||list2.contains(b.getRelative(0, -1, 0))) {
					needToRegen = true;
					Bukkit.broadcastMessage("needToRegen");
				}
			}
		}*/
		if(needToRegen) {
			room.getEntrances().forEach((Block b,newVector v)->{
				tempList.add(b);
			});
			
			room.setType(DunType.EMERGENCYSTOPWALL);
		}
		
		room.generatePlatfort();
		roomList.add(room);
		return room;
	}
	
}
