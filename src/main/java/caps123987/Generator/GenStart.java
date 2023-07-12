package caps123987.Generator;

import java.util.ArrayList;
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

		for(final Map.Entry<Block, newVector> entry : starterRoom.getEntrances().entrySet()) {
			Block b=entry.getKey();
				
			Room r = createRoom(DunType.RIGHT,b,entry.getValue().getRot(),true);
				
			b.setType(Material.ANDESITE);
				
				
			for(Map.Entry<Block, newVector> entry2:r.getEntrances().entrySet()) {
					
				entry2.getKey().setType(Material.GRANITE);
				Room r2 = createRoom(DunType.STRAIGHT,entry2.getKey(),entry2.getValue().getRot(),false);
					
				b.setType(Material.ANDESITE);
			}
		}
	}
	
	public Room createRoom(DunType type,Block entrance, int rot,boolean debug) {
		Room room = new Room(type,entrance,rot,debug);
		roomList.add(room);
		return room;
	}
	
}
