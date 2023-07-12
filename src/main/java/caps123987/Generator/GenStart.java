package caps123987.Generator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class GenStart {
	Location startPos;
	Block startBlock;
	int size;
	
	public GenStart(Location startPos,int size) {
		this.size=size;
		this.startPos = startPos;
		this.startBlock = startPos.getBlock();
		start();
	}
	
	public void start() {
		startBlock.setType(Material.GOLD_BLOCK);
		
		
		
	}
	
	
}
