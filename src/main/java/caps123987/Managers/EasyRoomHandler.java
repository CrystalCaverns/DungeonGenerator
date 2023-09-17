package caps123987.Managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;

public class EasyRoomHandler {
	private Block entrance;
	
	private List<Block> exits = new ArrayList<Block>();

	public Block getEntrance() {
		return entrance;
	}

	public void setEntrance(Block entrance) {
		this.entrance = entrance;
	}

	public List<Block> getExits() {
		return exits;
	}

	public void setExits(List<Block> exits) {
		this.exits = exits;
	}
	
	public void addExit(Block b) {
		if(!exits.contains(b)) {
			exits.add(b);
		}
	}
	
	public void removeExit(Block b) {
		if(exits.contains(b)){
			exits.remove(b);
		}
	}
}
