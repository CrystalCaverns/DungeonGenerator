package caps123987.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import caps123987.DungeonGenerator.DungeonGenerator;
import caps123987.Types.ItemWRarity;

public class LootTable {
	final List<ItemWRarity> itemsList;
	final int maxItems;
	final int maxPercent;
	
	public LootTable() {
		itemsList = DungeonGenerator.invMap.get("floor_1").get("DECORATED_POT");
		
		maxItems = 10;

		int maxPercent = 0;
		for(ItemWRarity i: itemsList) {
			maxPercent += i.getRarity();
		}
		this.maxPercent = maxPercent;

		
	}
	public LootTable(String name, int maxItems, World world){

		itemsList = DungeonGenerator.invMap.get(world.getName()).get(name);

		if(maxItems>4) {
			this.maxItems = DunUtils.getRandomValue(maxItems - 3, maxItems);
		}else{
			this.maxItems = maxItems;
		}

		int maxPercent = 0;
		for(ItemWRarity i: itemsList) {
			maxPercent += i.getRarity();
		}
		this.maxPercent = maxPercent;
	}

	
	public List<ItemStack> generate(){
		
		List<ItemStack> items = new ArrayList<ItemStack>();
		
		for(int i = 0;i!=maxItems;i++) {
			int value = DunUtils.getRandomValue(0,maxPercent);
			
			
			int current = 0;
			for(ItemWRarity item: itemsList) {
				
				if(isInRange(value,current,current+item.getRarity())) {
					items.add(item.getItem());
					break;
				}
				current+=item.getRarity();
				
			}
			
		}
		
		return items;
	}
	
	private boolean isInRange(int value, int min, int max) {
		return (value>=min&&max>=value);
	}
	
}
