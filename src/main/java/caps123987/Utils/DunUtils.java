package caps123987.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import caps123987.DungeonGenerator.DungeonGenerator;
import caps123987.Room.Room;
import caps123987.Types.DunMater;
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
	public static File getVarFile(String name, int iteration,DunMater m, DungeonGenerator instance) {
		List<File> listF=new ArrayList<File>();
		for(int i = 1;i<iteration;i++) {
			
			File f =new File(instance.getDataFolder(),name+"_"+m.name()+"_"+i+".nbt");
			
			
			if(f.exists()) {
				listF.add(f);
			}
		}
		
		
		return listF.get(getRandomValue(0,listF.size()-1));
	}
	public static List<DunType> getRandomDunTypeList() {
		DunType[] types = DunType.values();

		
		List<DunType> typesTo = new ArrayList<DunType>();
		for(DunType type:types) {
			if(!type.isEnabled()) {
				continue;
			}
			for(int i = 0;i<type.getRare();i++) {
				typesTo.add(type);
			}
			
		}
		
		return typesTo;
	}
	
}
