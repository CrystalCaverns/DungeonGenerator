package caps123987.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import caps123987.Types.DunMater;

public class BoudingBox {
	private Vector corner1;
	private Vector corner2;
	
	private DunMater mater;
	
	private List<Vector> vectors =new ArrayList<Vector>();
	
	public BoudingBox(Vector corner1,Vector corner2) {
		setCorner1(corner1);
		setCorner2(corner2);
		
		
		int x1 = corner1.clone().getBlockX();
		int z1 = corner1.clone().getBlockZ();
		
		int x2 = corner2.clone().getBlockX();
		int z2 = corner2.clone().getBlockZ();
		
		for(int X = x1; X!=x2+1;X++) {
			for(int Z = z1; Z!=z2+1;Z++) {
				vectors.add(new Vector(X,-1,Z));
			}
		}
	}
	
	
	public List<Vector> getList(){
		return vectors;
	}
	
	public List<Block> getBlockList(Block b, int d){
		
		List<Block> list = new ArrayList<Block>();
		vectors.forEach((Vector v)->{
			list.add(DunUtils.getRelative(b, DunUtils.rotate(v, d)));
		});
		return list;
	}
	

	public Vector getCorner1() {
		return corner1;
	}

	public void setCorner1(Vector corner1) {
		this.corner1 = corner1;
	}

	public Vector getCorner2() {
		return corner2;
	}

	public void setCorner2(Vector corner2) {
		this.corner2 = corner2;
	}
}
