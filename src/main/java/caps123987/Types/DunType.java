package caps123987.Types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.util.Vector;

import caps123987.Utils.newVector;

public enum DunType {
	END(0,Material.COAL_BLOCK,new Vector(0,0,-4)),
	STRAIGHT(1,Material.IRON_BLOCK,new Vector(0,0,-4),new newVector(0,0,4,0)),
	RIGHT(2,Material.REDSTONE_BLOCK,new Vector(0,0,-4),new newVector(4,0,0,90)),
	LEFT(3,Material.LAPIS_BLOCK,new Vector(0,0,-4),new newVector(-4,0,0,270)),
	TCORNER(3,Material.BONE_BLOCK,new Vector(0,0,-4),new newVector(-4,0,0,90),new newVector(4,0,0,270)),
	CROSSSECTION(4,Material.WHITE_WOOL,new Vector(0,0,-4),new newVector(0,0,4,0),new newVector(4,0,0,90),new newVector(-4,0,0,270)),
	MAIN(5,Material.GOLD_BLOCK,new Vector(0,0,-4),new newVector(0,0,4,0),new newVector(4,0,0,90),new newVector(-4,0,0,270),new newVector(0,0,-4,180));
	
	private int id;
	
	private List<newVector> entrances = new ArrayList<newVector>();
	
	private Material m;
	private Vector entrance;
	
	private DunType(int id,Material m,Vector entrance,newVector... entrances) {
		this.id = id;
		this.m = m;
		this.entrance = entrance;
		for(newVector entrance2:entrances) {
			this.entrances.add(entrance2);
		}
	}
	public Vector getEntrance() {
		return entrance;
	}
	public Material getMaterial() {
		return m;
	}
	
	public List<newVector> getEntrances(){
		return entrances;
	}
	
	public int getId() {
		return id;
	}
	
	public DunType getType(int id) {
		for(DunType type:values()) {
			if(type.getId()==id) {
				return type;
			}
		}
		return DunType.END;
	}
	
}
