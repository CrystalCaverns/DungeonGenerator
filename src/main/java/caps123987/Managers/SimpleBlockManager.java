package caps123987.Managers;

import org.bukkit.block.Block;

public class SimpleBlockManager {
	
	//SimpleChunk[][] blocks;
	boolean[][][] blocks;
	
	Block center;
	
	int distance;
	
	public SimpleBlockManager(int distance, Block center) {
		//blocks = new SimpleChunk[(distance/16*2)+16][(distance/16*2)+16];
		blocks = new boolean[distance*2+600][400][distance*2+600];
		this.center = center;
		this.distance = distance;
	}
	
	public boolean getBlock(int x,int y, int z) {
		return blocks[x-xOffset()][y+64][z-zOffset()];
	}
	
	public void setBlock(int x,int y, int z, boolean bool) {
		
		blocks[x-xOffset()][y+64][z-zOffset()] = bool;
	}
	
	public boolean getBlock(Block b) {
		return getBlock(b.getX(),b.getY(),b.getZ());
	}
	
	public void setBlock(Block b, boolean bool) {
		setBlock(b.getX(),b.getY(),b.getZ(),bool);
	}
	
	private int xOffset() {
		return center.getX() - distance-300;
		
		//return center.getX() - distance-64;
	}
	
	private int zOffset() {
		return center.getZ() - distance-300;
		
		//return center.getZ() - distance-64;
	}

	public void reset() {
		blocks = null;
	}

}
