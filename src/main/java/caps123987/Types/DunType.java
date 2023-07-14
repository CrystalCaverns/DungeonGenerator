package caps123987.Types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.util.Vector;

import caps123987.Utils.BoudingBox;
import caps123987.Utils.newVector;

public enum DunType {
	
	END(true,Material.COAL_BLOCK,new BoudingBox(new Vector(-4,0,-4),new Vector(4,0,4)),new Vector(0,0,-4)),
	END1(false,Material.COAL_BLOCK,new BoudingBox(new Vector(-4,0,-4),new Vector(4,0,4)),new Vector(0,0,-4)),
	END2(false,Material.COAL_BLOCK,new BoudingBox(new Vector(-4,0,-4),new Vector(4,0,4)),new Vector(0,0,-4)),
	STRAIGHT(true,Material.IRON_BLOCK,new BoudingBox(new Vector(-4,0,-4),new Vector(4,0,4)),new Vector(0,0,-4),new newVector(0,0,4,0)),
	STRAIGHTLONG(true,Material.IRON_BLOCK,new BoudingBox(new Vector(-4,0,-6),new Vector(4,0,6)),new Vector(0,0,-6),new newVector(0,0,6,0)),
	LEFT(true,Material.REDSTONE_BLOCK,new BoudingBox(new Vector(-4,0,-4),new Vector(4,0,4)),new Vector(0,0,-4),new newVector(4,0,0,270)),
	RIGHT(true,Material.LAPIS_BLOCK,new BoudingBox(new Vector(-4,0,-4),new Vector(4,0,4)),new Vector(0,0,-4),new newVector(-4,0,0,90)),
	TCORNER(true,Material.BONE_BLOCK,new BoudingBox(new Vector(-4,0,-4),new Vector(4,0,4)),new Vector(0,0,-4),new newVector(-4,0,0,90),new newVector(4,0,0,270)),
	CROSSSECTION(true,Material.WHITE_WOOL,new BoudingBox(new Vector(-4,0,-4),new Vector(4,0,4)),new Vector(0,0,-4),new newVector(0,0,4,0),new newVector(4,0,0,270),new newVector(-4,0,0,90)),
	STRAIGHTRIGHT(true,Material.RED_WOOL,new BoudingBox(new Vector(-4,0,-4),new Vector(4,0,4)),new Vector(0,0,-4),new newVector(-4,0,0,90),new newVector(0,0,4,0)),
	STRAIGHTLEFT(false,Material.BLUE_WOOL,new BoudingBox(new Vector(-4,0,-4),new Vector(4,0,4)),new Vector(0,0,-4),new newVector(4,0,0,270),new newVector(0,0,4,0)),
	
	//DO NOT ENABLE
	EMERGENCYSTOPWALL(false,Material.YELLOW_CONCRETE,new BoudingBox(new Vector(-4,0,0),new Vector(4,0,1)),new Vector(0,0,2)),
	MAIN(false,Material.GOLD_BLOCK,new BoudingBox(new Vector(-4,0,-4),new Vector(4,0,4)),new Vector(0,0,-4),new newVector(0,0,4,0),new newVector(4,0,0,90),new newVector(-4,0,0,270),new newVector(0,0,-4,180));
	
	private boolean enabled;
	
	private List<newVector> entrances = new ArrayList<newVector>();
	
	private Material m;
	private Vector entrance;
	
	private BoudingBox boudingBox;
	
	private DunType(final boolean enabled,final Material m,final BoudingBox bouding,final Vector entrance,final newVector... entrances) {
		this.enabled = enabled;
		this.m = m;
		this.entrance = entrance;
		this.boudingBox = bouding;
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
	public BoudingBox getBoudingBox() {
		return boudingBox;
	}
	
}
