package caps123987.Generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import caps123987.DungeonGenerator.DungeonGenerator;
import caps123987.Room.Room;
import caps123987.Types.DunType;
import caps123987.Utils.DunUtils;
import caps123987.Utils.newVector;

public class GenStart {
	Location startPos;
	Block startBlock;
	int size;
	
	private List<Room> roomList=new ArrayList<Room>();
	private Map<Block,newVector> entrances=new HashMap<Block,newVector>();
	
	public GenStart(Location startPos,int size) {
		this.size=size;
		this.startPos = startPos;
		this.startBlock = startPos.getBlock();
		start();
	}
	
	public void start() {
		
		/*DunUtils.getRelative(startBlock,  new Vector(1,0,0)).setType(Material.OAK_PLANKS); //east
		DunUtils.getRelative(startBlock, DunUtils.rotate( new Vector(1,0,0), 90)).setType(Material.ACACIA_PLANKS);//south
		DunUtils.getRelative(startBlock, DunUtils.rotate( new Vector(1,0,0), 180)).setType(Material.DARK_OAK_PLANKS);//west
		DunUtils.getRelative(startBlock, DunUtils.rotate( new Vector(1,0,0), 270)).setType(Material.MANGROVE_PLANKS);//north*/
		
		Room starterRoom = createRoom(DunType.MAIN,startBlock,0,false);

		for(Map.Entry<Block, newVector> entry : starterRoom.getEntrances().entrySet()) {
			Block b=entry.getKey();
				
			Room r = createRoom(DunType.STRAIGHT,b,entry.getValue().getRot(),true);
				
			
			DunType[] types = DunType.values();

			for(Map.Entry<Block, newVector> entry2:r.getEntrances().entrySet()) {
				
				
				int id = DunUtils.getRandomValue(0, types.length-1);
				
				DunType type = types[id];
				while(!types[id].isEnabled()) {
					id = DunUtils.getRandomValue(0, types.length-1);
					
					type = types[id];
				}
					
				Room r3 = createRoom(type,entry2.getKey(),entry2.getValue().getRot(),false);
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
				Room r = createRoom(type,entry.getKey(),entry.getValue().getRot(),false);
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
		
		
	}
	
	public Room createRoom(DunType type,Block entrance, int rot,boolean debug) {
		Room room = new Room(type,entrance,rot,debug);
		roomList.add(room);
		return room;
	}
	
}
