package caps123987.Room;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import com.github.shynixn.structureblocklib.api.bukkit.StructureBlockLibApi;
import com.github.shynixn.structureblocklib.api.enumeration.StructureRotation;

import caps123987.DungeonGenerator.DungeonGenerator;
import caps123987.Managers.SimpleBlockManager;
import caps123987.Types.DunMater;
import caps123987.Types.DunType;
import caps123987.Utils.BoudingBox;
import caps123987.Utils.DunUtils;
import caps123987.Utils.newVector;

public class Room {
	private DunType type;
	private	Block block;
	private Map<Block,newVector> entrances = new HashMap<Block,newVector>();
	private int Rot;
	private BoudingBox boudingBox;
	private Block entrance;
	
	public DungeonGenerator instance = DungeonGenerator.getInstance();
	
	Path path;
	
	
	public Room(DunType type,Block entrance,int rot,boolean debug) {
		this.type =type;
		this.Rot = rot;
		
		this.setBoudingBox(type.getBoudingBox());
		
		this.entrance = entrance;
		//if(debug)Bukkit.broadcastMessage(type.name()+"  "+type.getEntrance().toString()+" "+rot+"  "+DunUtils.rotate(type.getEntrance(),Rot));
		
		
		if(type.equals(DunType.MAIN)) {
			this.block = entrance;
			
			for(newVector v: type.getEntrances()) {
				this.entrances.put(DunUtils.getRelative(block, v)
						,new newVector(
						DunUtils.rotate(new Vector(v.getX(),v.getY(),v.getZ()), v.getRot())
							,v.getRot())) ;
			}
			
			return;
		}else {
			saveBlock(entrance);
		}
		
		
		//this.block = DunUtils.getRelative(entrance, DunUtils.rotate(type.getEntrance().clone().multiply(-1),Rot));
		//this.block = entrance;
		
		
		calculateEntrances(rot);
		
	}
	
	private void saveBlock(Block entrance) {
		this.block = DunUtils.getCenter(type, entrance, Rot); 
		
		//DunUtils.getRelative(entrance, DunUtils.rotate(type.getEntrance().multiply(-1),Rot));
		
		this.block = DunUtils.getRelativeByRot(block, Rot);
	}
	
	public void calculateEntrances(int rot) {
		for(newVector v: type.getEntrances()) {
			
			int newRot = rot - v.getRot();
			if(newRot>=360) {
				newRot -=360;
			}
			if(newRot<0) {
				newRot +=360;
			}
			//Bukkit.broadcastMessage(rot+" "+v.getRot()+" "+newRot);
			
			Vector rotated = DunUtils.rotate(v.getVector(), rot);
			
			this.entrances.put(DunUtils.getRelative(block, rotated)
					,new newVector(rotated,newRot)) ;
			
		}
	}
	
	public void setType(DunType type) {
		this.boudingBox=type.getBoudingBox();
		this.entrances.clear();
		this.type = type;
		
		
		/*Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, ()->{
			block.getWorld().spawnParticle(Particle.REDSTONE, block.getLocation().getX(),block.getLocation().getY() +5,block.getLocation().getZ()
					, 5, 0.1 , 0.1 , 0.1 ,new DustOptions(Color.RED,1));
		}, 5, 5);*/
		
		saveBlock(entrance);
		calculateEntrances(Rot);
		
	}
	
	public Block getEntrance() {
		return entrance;
	}
	
	public void generateSimplePlatfort(SimpleBlockManager mana) {
		generatePlatform(type.getMaterial(), false, mana);
	}
	
	public void generatePlatfort() {
		generatePlatform(type.getMaterial(), true, null);
	}
	
	public void generatePlatform(Material m) {
		generatePlatform(m, true, null);
	}
	
	public void generatePlatform(Material m, boolean bool, SimpleBlockManager mana) {
		
		boudingBox.getBlockList(block,Rot).forEach((Block b)->{
			entrances.forEach((Block b2, newVector v)->{
				Block b3 = b.getRelative(0, b2.getY()-b.getY()-1, 0);
				if(bool) {
					b3.setType(m);
				}else {
					mana.setBlock(b3, true);
				}
				
			});
			if(bool) {
				b.setType(m);
			}else {
				mana.setBlock(b, true);
			}
		});
	}
	
	public Map<Block,newVector> getEntrances(){
		return entrances;
	}
	
	public void applyRoom() {
		
		this.path = instance.getDataFolder().toPath().resolve(type.name()+".nbt");
		
		File f =DunUtils.getVarFile(type.name(), 6,DunMater.COBBLESTONE,instance);
				
		
		Block newB = getConer(block);
		StructureBlockLibApi.INSTANCE
		.loadStructure(instance)
		.at(new Location(block.getWorld(),newB.getX() , newB.getY(), newB.getZ()))
		.rotation(structureRotationFromInt(Rot))
		.includeEntities(true)
		.loadFromFile(f).onException((Throwable t)->{Bukkit.broadcastMessage("Error While generation "+t);});
	}
	
	private Block getConer(Block b) {
		switch(Rot) {
		case 180:
			return DunUtils.getRelative(b, type.getBoudingBox().getCorner1());
		case 0:
			return DunUtils.getRelative(b, type.getBoudingBox().getCorner2().clone().setY(type.getBoudingBox().getCorner1().getBlockY()));
		case 90:
			return DunUtils.getRelative(b,new Vector(-type.getBoudingBox().getCorner1().getBlockZ(),0,type.getBoudingBox().getCorner1().getBlockX()));
		case 270:
			return DunUtils.getRelative(b,new Vector(type.getBoudingBox().getCorner1().getBlockZ(),0,-type.getBoudingBox().getCorner1().getBlockX()));
		}
		return b;
	}
	
	
	private StructureRotation structureRotationFromInt(int d) {
		switch (d){
		case 90:
			return StructureRotation.ROTATION_90;
		case 180:
			return StructureRotation.NONE;
		case 270:
			return StructureRotation.ROTATION_270;
		case 0:
			return StructureRotation.ROTATION_180;
		}
		return StructureRotation.NONE;
		
	}

	public DunType getType() {
		return type;
	}
	
	public Block getBlock() {
		return block;
	}
	
	public int getRot() {
		return Rot;
	}



	public BoudingBox getBoudingBox() {
		return boudingBox;
	}

	public void setBoudingBox(BoudingBox boudingBox) {
		this.boudingBox = boudingBox;
	}
}
