package caps123987.Managers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class EasyRoomHandler {
	private Block entrance;
	private Block corner1;
	private Block corner2;
	private Block center;
	
	private int sizeX;
	private int sizeY;
	private int sizeZ;
	
	private Map<Block,Integer> exits = new HashMap<Block,Integer>();

	public Block getEntrance() {
		return entrance;
	}

	public void setEntrance(Block entrance) {
		this.entrance = entrance;
	}

	public Map<Block,Integer> getExits() {
		return exits;
	}

	public void setExits(Map<Block,Integer> exits) {
		this.exits = exits;
	}
	
	public void addExit(Block b,int rot) {
		if(!exits.containsKey(b)) {
			exits.put(b,rot);
		}
	}
	
	public void removeExit(Block b) {
		if(exits.containsKey(b)){
			exits.remove(b);
		}
	}

	public Block getCorner1() {
		return corner1;
	}

	public void setCorner1(Block corner1) {
		this.corner1 = corner1;
		cornerUpdate();
	}

	public Block getCorner2() {
		return corner2;
	}

	public void setCorner2(Block corner2) {
		this.corner2 = corner2;
		cornerUpdate();
	}
	
	private void cornerUpdate() {
		if(corner2==null||corner1==null) {
			return;
		}
		
		sizeX = (corner1.getX() - corner2.getX())+1;
		sizeY = (corner2.getY() - corner1.getY())+1;
		sizeZ = (corner1.getZ() - corner2.getZ())+1;
		
		int x = corner2.getX()+(corner1.getX() - corner2.getX())/2;
		int y = corner1.getY();
		int z = corner2.getZ()+(corner1.getZ() - corner2.getZ())/2;
		
		center = Bukkit.getWorld("world").getBlockAt(x,y,z);
		center.setType(Material.YELLOW_CONCRETE);
		
		Bukkit.broadcastMessage("X: "+sizeX+" Y: "+sizeY+" Z: "+sizeZ);
		
	}
	
	public void generate() {

		StringBuilder output = new StringBuilder();

		output.append("new BoudingBox(new Vector(-").append((sizeX - 1) / 2).append(",0,-").append((sizeZ - 1) / 2).append("), new Vector(").append((sizeX - 1) / 2).append(",").append(sizeY - 1).append(",").append((sizeZ - 1) / 2).append(")), ");

		if(entrance==null) {
			Bukkit.broadcastMessage("Entrance is null");
			return;
		}

		output.append("new Vector(").append(center.getX() - entrance.getX()).append(",").append(entrance.getY() - center.getY()).append(",").append(center.getZ() - entrance.getZ()).append(")");

		for(Map.Entry<Block, Integer> entry :exits.entrySet()) {
			Block exit = entry.getKey();
			
			int rot = entry.getValue();


			output.append(" ,new newVector(").append(center.getX() - exit.getX()).append(",").append(exit.getY() - center.getY()).append(",").append(center.getZ() - exit.getZ()).append(",").append(getRot(rot)).append(")");

		}

		Bukkit.broadcastMessage(output.toString());
		
	}
	
	private int getRot(int rot) {
		switch(rot) {
		case -180:
			return 0;
		case -90:
			return 90;
		case 90:
			return 270;
		case 0:
			return 180;
		default: 
			return 0;
		
		}
	}
}
