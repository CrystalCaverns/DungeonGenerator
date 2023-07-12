package caps123987.Room;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import caps123987.Types.DunType;
import caps123987.Utils.DunUtils;
import caps123987.Utils.newVector;

public class Room {
	DunType type;
	Block block;
	Map<Block,newVector> entrances = new HashMap<Block,newVector>();
	int Rot;
	
	
	
	public Room(DunType type,Block entrance,int rot,boolean debug) {
		this.type=type;
		this.Rot = rot;
		
		//if(debug)Bukkit.broadcastMessage(type.name()+"  "+type.getEntrance().toString()+" "+rot+"  "+DunUtils.rotate(type.getEntrance(),Rot));
		
		if(type.equals(DunType.MAIN)) {
			this.block = entrance;
			
			for(newVector v: type.getEntrances()) {
				this.entrances.put(DunUtils.getRelative(block, v)
						,new newVector(
						DunUtils.rotate(new Vector(v.getX(),v.getY(),v.getZ()), v.getRot())
							,v.getRot())) ;
			}
			
			Bukkit.broadcastMessage("was Main");
			block.setType(type.getMaterial());
			return;
		}else {
			this.block = DunUtils.getRelative(entrance, DunUtils.rotate(type.getEntrance().multiply(-1),Rot));
			
			if(debug)Bukkit.broadcastMessage(Rot+" ");
			
			type.getEntrance().multiply(-1);
			
			
		}
		
		//this.block = DunUtils.getRelative(entrance, DunUtils.rotate(type.getEntrance().clone().multiply(-1),Rot));
		//this.block = entrance;
		
		block.getRelative(0, 1, 0).setType(Material.BEDROCK);
		
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
					,new newVector(v.getVector(),newRot)) ;
			
			
			DunUtils.getRelative(block, rotated).setType(Material.SANDSTONE);
			
			if(debug)Bukkit.broadcastMessage(newRot+" ");
			
		}
		
		
		block.setType(type.getMaterial());
		
		
	}
	
	public Map<Block,newVector> getEntrances(){
		return entrances;
	}
	
	public void applyRoom() {
		
	}
}
