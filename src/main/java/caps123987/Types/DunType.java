package caps123987.Types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.util.Vector;

import caps123987.Utils.newVector;

public enum DunType {
	END(true,Material.COAL_BLOCK,new Vector(0,0,-4)),
	END1(true,Material.COAL_BLOCK,new Vector(0,0,-4)),
	END2(true,Material.COAL_BLOCK,new Vector(0,0,-4)),
	STRAIGHT(true,Material.IRON_BLOCK,new Vector(0,0,-4),new newVector(0,0,4,0)),
	RIGHT(true,Material.REDSTONE_BLOCK,new Vector(0,0,-4),new newVector(4,0,0,270)),
	LEFT(true,Material.LAPIS_BLOCK,new Vector(0,0,-4),new newVector(-4,0,0,90)),
	TCORNER(true,Material.BONE_BLOCK,new Vector(0,0,-4),new newVector(-4,0,0,90),new newVector(4,0,0,270)),
	CROSSSECTION(true,Material.WHITE_WOOL,new Vector(0,0,-4),new newVector(0,0,4,0),new newVector(4,0,0,270),new newVector(-4,0,0,90)),
	MAIN(false,Material.GOLD_BLOCK,new Vector(0,0,-4),new newVector(0,0,4,0),new newVector(4,0,0,90),new newVector(-4,0,0,270),new newVector(0,0,-4,180));
	
	private boolean enabled;
	
	private List<newVector> entrances = new ArrayList<newVector>();
	
	private Material m;
	private Vector entrance;
	
	private DunType(boolean enabled,Material m,Vector entrance,newVector... entrances) {
		this.enabled = enabled;
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
	
	public boolean isEnabled() {
		return enabled;
	}
	
}
