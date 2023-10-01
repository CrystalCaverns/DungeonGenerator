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
		Bukkit.broadcastMessage("new BoudingBox(new Vector(-"+((sizeX-1)/2)+",0,-"+((sizeZ-1)/2)+"), new Vector("+((sizeX-1)/2)+","+(sizeY-1)+","+((sizeZ-1)/2)+"))");
		
		if(entrance==null) {
			Bukkit.broadcastMessage("Entrance is null");
		}
		
		Bukkit.broadcastMessage("new Vector("+(center.getX()-entrance.getX())+","+(entrance.getY()-center.getY())+","+(center.getZ()-entrance.getZ())+")");
		
		for(Map.Entry<Block, Integer> entry :exits.entrySet()) {
			Block exit = entry.getKey();
			
			int rot = entry.getValue();
			
			Bukkit.broadcastMessage("new newVector("+(center.getX()-exit.getX())+","+(exit.getY()-center.getY())+","+(center.getZ()-exit.getZ())+","+getRot(rot)+")");
		}
		
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
