package caps123987.Utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.Inventory;

/*
 * WIP
 */

public class RegChest implements ConfigurationSerializable{
	public boolean enabled;
	public Block chest;
	public int rarity;
	
	public RegChest(Block b) {
		chest = b;
	}
	public RegChest(Block b, boolean boo) {
		chest = b;
		enabled = boo;
	}
	public RegChest(Block b,boolean boo, int r) {
		chest = b;
		enabled = boo;
		rarity = r;
	}
	
	
	@Override
	public Map<String, Object> serialize() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("bloc", chest.getLocation());
		// TODO Auto-generated method stub
		return map;
	}
	
	public static RegChest deserialize(Map<String, Object> args) {
		
		return null;//new RegChest(((Location)args.get("bloc")).getBlock(),(Inventory)args.get("inventor"));
	
	}
}
