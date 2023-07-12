package caps123987.Types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.util.Vector;

public enum DunType {
	END(0,Material.COAL_BLOCK,new Vector(-4,0,0)),
	STRAIGHT(1,Material.IRON_BLOCK,new Vector(4,0,0),new Vector(-4,0,0)),
	RIGHT(2,Material.REDSTONE_BLOCK,new Vector(-4,0,0),new Vector(0,0,4)),
	LEFT(3,Material.LAPIS_BLOCK,new Vector(-4,0,0),new Vector(0,0,-4)),
	TCORNER(3,Material.BONE_BLOCK,new Vector(-4,0,0),new Vector(0,0,4),new Vector(0,0,-4)),
	CROSSSECTION(4,Material.GOLD_BLOCK,new Vector(-4,0,0),new Vector(0,0,4),new Vector(0,0,-4),new Vector(4,0,0));
	
	private int id;
	
	private List<Vector> entrances = new ArrayList<Vector>();
	
	private Material m;
	
	private DunType(int id,Material m,Vector... entrances) {
		this.id = id;
		this.m = m;
		for(Vector entrance:entrances) {
			this.entrances.add(entrance);
		}
	}
	
	public Material getMaterial() {
		return m;
	}
	
	public List<Vector> getEntrances(){
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
