package caps123987.Managers;

import org.bukkit.block.Block;

import caps123987.Types.SimpleChunk;

public class SimpleBlockManager {
	
	SimpleChunk[][] blocks;
	
	Block center;
	
	int distance;
	
	public SimpleBlockManager(int distance, Block center) {
		blocks = new SimpleChunk[(distance/16*2)+16][(distance/16*2)+16];
		this.center = center;
		this.distance = distance;
	}
	
	public boolean getBlock(int x,int y, int z) {
		
		SimpleChunk chunk = blocks[(x-xOffset())/16][(y-yOffset())/16];
		
		if(chunk==null) {
			chunk = new SimpleChunk();
			blocks[(x-xOffset())/16][(y-yOffset())/16] = chunk;
		}
		
		return chunk.getBlock((x-xOffset())%16, (y-yOffset())%16, z);
	}
	
	public void setBlock(int x,int y, int z, boolean bool) {
		
		SimpleChunk chunk = blocks[(x-xOffset())/16][(y-yOffset())/16];
		
		if(chunk==null) {
			chunk = new SimpleChunk();
			blocks[(x-xOffset())/16][(y-yOffset())/16] = chunk;
		}
		
		chunk.setBlock((x-xOffset())%16, (y-yOffset())%16, z, bool);
	}
	
	public boolean getBlock(Block b) {
		return getBlock(b.getX(),b.getY(),b.getZ());
	}
	
	public void setBlock(Block b, boolean bool) {
		setBlock(b.getX(),b.getY(),b.getZ(),bool);
	}
	
	private int xOffset() {
		return center.getX() - distance;
	}
	
	private int yOffset() {
		return center.getY() - distance;
	}

}
