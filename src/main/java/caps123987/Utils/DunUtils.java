package caps123987.Utils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import caps123987.Room.Room;
import caps123987.Types.DunType;

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
	
	public static Block getRelativeByRot(Block block,int Rot) {
		switch(Rot) {
		case 0:
			return block.getRelative(0, 0, 1);
		case 90:
			return block.getRelative(1, 0, 0);
		case 180:
			return block.getRelative(0, 0, -1);
		case 270:
			return block.getRelative(-1, 0, 0);
		}
		return block;
	}
	public static Block getCenter(DunType type,Block entrance,int rot) {
		Block b =DunUtils.getRelative(entrance, DunUtils.rotate(type.getEntrance().multiply(-1),rot));
		
		type.getEntrance().multiply(-1);
		return b;
	}
	
	public boolean canGenerate() {
		return false;
	}
	
	public static Room getRoomByEntrance(List<Room> list, Block entrance) {
		for(Room r:list) {
			if(r.getEntrance().equals(entrance)) {
				return r;
			}
		}
		return null;
	}
	
}
