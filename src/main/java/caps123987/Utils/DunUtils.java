package caps123987.Utils;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class DunUtils {
	public static int getRandomValue(int Min, int Max)
    {
        return ThreadLocalRandom.current().nextInt(Min, Max + 1);
    }
	
	public static Block getRelative(Block b,Vector v) {
		return b.getRelative(v.getBlockX(),v.getBlockY(),v.getBlockZ());
	}
	public static Block getRelative(Block b, newVector v) {
		return b.getRelative(v.getX(),v.getY(),v.getZ());
	}
	
	public static Vector rotate(Vector v,int degree) {
		switch(degree) {
		case 90:
			return new Vector(v.getBlockZ(),v.getBlockY(),-v.getBlockX());
		case 180:
			return v.clone().multiply(new Vector(-1,1,-1));
		case 270:
			return new Vector(-v.getBlockZ(),v.getBlockY(),v.getBlockX());
		case 0:
			return v;
		default:
			return v;
		}
		
	}					
	
}
