package caps123987.Managers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import caps123987.DungeonGenerator.DungeonGenerator;
import caps123987.Utils.DunUtils;

public class ChestManager {

	private DungeonGenerator plugin;
	
	private int time = 1;
	
	public Map<Block,Inventory> chests=new HashMap<Block,Inventory>();
	
	private Inventory emptyInv = Bukkit.createInventory(null, 27);
	

	public ChestManager(DungeonGenerator p) {
		plugin = p;
	}
	
	public boolean exists(Block chest) {
		return chests.containsKey(chest);
	}
	
	public void register(Block chest,Inventory i) {
		if(!exists(chest)) {
			chests.put(chest, i);
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()->chests.remove(chest),1200*time);
		}
	}
	
	public Inventory openInventory(Block b) {
		if(!exists(b)) {
			register(b,getRandomInventory(15));
		}
		
		return chests.get(b);
	}
	
	public Inventory getRandomInventory(int iterator) {
		File parent =  plugin.invFile;
		
		List<File> files = new ArrayList<File>();
		
		for(int i = 1;i<=iterator;i++) {
			File inv = new File(parent,i+".yml");
			if(inv.exists()) {
				files.add(inv);
			}
		}
		File finalF = files.get(DunUtils.getRandomValue(0, files.size()-1));
		FileConfiguration yaml=YamlConfiguration.loadConfiguration(finalF);
		
		Inventory inv = Bukkit.createInventory(null, 27);
		
		List<ItemStack> list =(List<ItemStack>) yaml.get("Items");
		for(ItemStack item: list) {
			if(item!=null) {
				inv.addItem(item);
			}
		}
		
		return inv;
		
	}
}
