package caps123987.Types;

public class SimpleChunk {
	
	boolean[][][] blocks = new boolean[16][384][16];
	
	public boolean getBlock(int x,int y,int z) {
		return blocks[x][y][z];
	}
	public void setBlock(int x,int y,int z, boolean bool) {
		blocks[x][y][z] = bool;
	}
}
