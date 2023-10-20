package caps123987.Managers;

import org.bukkit.block.Block;

import caps123987.Types.SimpleChunk;

public class SimpleBlockManager {
	
	SimpleChunk[][] blocks;
	
	public SimpleBlockManager(int distance) {
		blocks = new SimpleChunk[(distance/16*2)+16][(distance/16*2)+16];
	}
	
	public boolean getBlock(int x,int y, int z) {
		
		SimpleChunk chunk = blocks[Math.floorMod(x, 16)][Math.floorMod(y, 16)];
		
		if(chunk==null) {
			chunk = new SimpleChunk();
			blocks[Math.floorMod(x, 16)][Math.floorMod(y, 16)] = chunk;
		}
		
		return chunk.getBlock(x, y, z);
	}
	
	public void setBlock(int x,int y, int z, boolean bool) {
		
		SimpleChunk chunk = blocks[Math.floorMod(x, 16)][Math.floorMod(y, 16)];
		
		if(chunk==null) {
			chunk = new SimpleChunk();
			blocks[Math.floorMod(x, 16)][Math.floorMod(y, 16)] = chunk;
		}
		
		chunk.setBlock(x, y, z, bool);
	}
	
	public boolean getBlock(Block b) {
		return getBlock(b.getX(),b.getY(),b.getZ());
	}
	
	public void setBlock(Block b, boolean bool) {
		setBlock(b.getX(),b.getY(),b.getZ(),bool);
	}

}
