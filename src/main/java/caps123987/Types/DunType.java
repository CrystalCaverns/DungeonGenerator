package caps123987.Types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.util.Vector;

import caps123987.Utils.BoudingBox;
import caps123987.Utils.newVector;

public enum DunType {
	
	END(true,80,Material.COAL_BLOCK,new BoudingBox(new Vector(-4,0,-4),new Vector(4,5,4)),new Vector(0,0,-4)),
	END1(false,1,Material.GREEN_CONCRETE,new BoudingBox(new Vector(-4,0,-4),new Vector(4,0,4)),new Vector(0,0,-4)),
	END2(false,0,Material.COAL_BLOCK,new BoudingBox(new Vector(-4,0,-4),new Vector(4,0,4)),new Vector(0,0,-4)),
	BIG(false,1,Material.ACACIA_LOG,new BoudingBox(new Vector(-7,0,-7),new Vector(7,0,7)),new Vector(0,0,-7),new newVector(0,0,7,0),new newVector(7,0,0,270),new newVector(-7,0,0,90)),
	STRAIGHT(true,60,Material.IRON_BLOCK,new BoudingBox(new Vector(-4,0,-4),new Vector(4,6,4)),new Vector(0,0,-4),new newVector(0,0,4,0)),
	DEADSTRAIGHT(true,40,Material.DIAMOND_BLOCK,new BoudingBox(new Vector(-4,0,-4),new Vector(4,6,4)),new Vector(0,0,-4),new newVector(0,0,4,0)),
	STRAIGHTLONG(false,5,Material.IRON_BLOCK,new BoudingBox(new Vector(-4,0,-6),new Vector(4,0,6)),new Vector(0,0,-6),new newVector(0,0,6,0)),
	LEFT(true,100,Material.REDSTONE_BLOCK,new BoudingBox(new Vector(-4,0,-4),new Vector(4,4,4)),new Vector(0,0,-4),new newVector(4,0,0,270)),
	RIGHT(true,100,Material.LAPIS_BLOCK,new BoudingBox(new Vector(-4,0,-4),new Vector(4,4,4)),new Vector(0,0,-4),new newVector(-4,0,0,90)),//right
	TCORNER(true,100,Material.BONE_BLOCK,new BoudingBox(new Vector(-4,0,-4),new Vector(4,4,4)),new Vector(0,0,-4),new newVector(-4,0,0,90),new newVector(4,0,0,270)),//left
	CROSSSECTION(true,100,Material.WHITE_WOOL,new BoudingBox(new Vector(-4,0,-4),new Vector(4,4,4)),new Vector(0,0,-4),new newVector(0,0,4,0),new newVector(4,0,0,270),new newVector(-4,0,0,90)),
	STRAIGHTRIGHT(true,90,Material.RED_WOOL,new BoudingBox(new Vector(-4,0,-4),new Vector(4,4,4)),new Vector(0,0,-4),new newVector(-4,0,0,90),new newVector(0,0,4,0)),
	STRAIGHTLEFT(true,90,Material.BLUE_WOOL,new BoudingBox(new Vector(-4,0,-4),new Vector(4,4,4)),new Vector(0,0,-4),new newVector(4,0,0,270),new newVector(0,0,4,0)),
	UP(false,30,Material.BLACK_WOOL,new BoudingBox(new Vector(-4,0,-4),new Vector(4,24,4)),new Vector(0,0,-4),new newVector(0,0,4,0),new newVector(0,19,-4,180)),
	NETHER(true,40,Material.STONE_BRICKS,new BoudingBox(new Vector(-6,0,-6),new Vector(6,7,6)),new Vector(0,0,-6),new newVector(-6,0,0,90)),
	
	
	
	//Suggested
	PLAY__ER1(true,30,Material.REDSTONE_BLOCK, new BoudingBox(new Vector(-8,0,-10),new Vector(8,9,10)),new Vector(0,1,-10), new newVector(-8,1,-2,90)),
	PLAY__ER2(true,40, Material.STONE_BRICKS, new BoudingBox(new Vector(-8,0,-6),new Vector(8,7,6)), new Vector(0,0,-6), new newVector(0,0,6,0)),
	PLAY__ER5(true,20,Material.STONE_BRICKS,new BoudingBox(new Vector(-10,0,-10), new Vector(10,30,10)), new Vector(0,0,-10),new newVector(-10,0,0,90),new newVector(10,0,0,270),new newVector(0,0,10,0),new newVector(0,23,-10,180),new newVector(0,23,10,0),new newVector(-10,23,0,90),new newVector(10,23,0,270)),
	
	RUINED(true,30,Material.GOLD_BLOCK,new BoudingBox(new Vector(-8,0,-7),new Vector(8,12,7)),new Vector(0,0,-7)),
	BRIDGE(true,30,Material.DIAMOND_BLOCK, new BoudingBox(new Vector(-6,0,-8), new Vector(6,12,8)), new Vector(-3,5,-8),new newVector(-3,5,8,0)),
	DOOR(true,50,Material.STONE_BRICKS, new BoudingBox(new Vector(-3,0,-2),new Vector(3,5,2)),new Vector(0,0,-2), new newVector(0,0,2,0)),
	LIBRARYTOWER(true,20,Material.BLACK_WOOL,new BoudingBox(new Vector(-10,0,-9),new Vector(10,25,9)),new Vector(-2,0,-9),new newVector(-2,0,9,0),new newVector(-10,20,0,90),new newVector(10,20,0,270)),
	
	
	//DO NOT ENABLE
	EMERGENCYSTOPWALL(false,0,Material.YELLOW_CONCRETE,new BoudingBox(new Vector(-4,0,0),new Vector(4,0,1)),new Vector(0,0,1)),
	MAIN(false,0,Material.GOLD_BLOCK,new BoudingBox(new Vector(-4,0,-4),new Vector(4,0,4)),new Vector(0,0,-4),new newVector(0,0,4,0),new newVector(4,0,0,90),new newVector(-4,0,0,270),new newVector(0,0,-4,180));
	
	private boolean enabled;
	
	private int rare;
	
	private List<newVector> entrances = new ArrayList<newVector>();
	
	private Material m;
	private Vector entrance;
	
	private BoudingBox boudingBox;
	
	
	
	private DunType(final boolean enabled,int rare,final Material m,final BoudingBox bouding,final Vector entrance,final newVector... entrances) {
		this.enabled = enabled;
		this.rare = rare;
		this.m = m;
		this.entrance = entrance;
		this.boudingBox = bouding;
		for(newVector entrance2:entrances) {
			this.entrances.add(entrance2);
		}
	}
	public int getRare() {
		return rare;
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
