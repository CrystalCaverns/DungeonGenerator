package caps123987.Generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import caps123987.Room.Room;
import caps123987.Types.DunType;

public class GenStart {
	Location startPos;
	Block startBlock;
	int size;
	
	private List<Room> roomList=new ArrayList<Room>();
	
	public GenStart(Location startPos,int size) {
		this.size=size;
		this.startPos = startPos;
		this.startBlock = startPos.getBlock();
		start();
	}
	
	public void start() {
		
		
		Room starterRoom = createRoom(DunType.CROSSSECTION,startBlock);
		
		for(Map.Entry<Block, Vector> entrance : starterRoom.getEntrances().entrySet()) {
			Block b=entrance.getKey();
			Vector v = entrance.getValue().multiply(-1);
			Block newBlock = b.getRelative(v.getBlockX(),v.getBlockY(),v.getBlockZ());
			createRoom(DunType.END,newBlock);
		}
	}
	
	public Room createRoom(DunType type,Block block) {
		Room room = new Room(type,block);
		roomList.add(room);
		return room;
	}
	
}
