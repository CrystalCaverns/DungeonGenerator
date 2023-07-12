package caps123987.Room;

import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import caps123987.Types.DunType;

public class Room {
	DunType type;
	Block block;
	Map<Block,Vector> entrances;
	
	
	public Room(DunType type,Block block) {
		this.type=type;
		this.block = block;
		block.setType(type.getMaterial());
		for(Vector v: type.getEntrances()) {
			this.entrances.put(block.getRelative(v.getBlockX(),v.getBlockY(),v.getBlockZ()),v) ;
		}
	}
	
	public Map<Block,Vector> getEntrances(){
		return entrances;
	}
	
	public void applyRoom() {
		
	}
}
