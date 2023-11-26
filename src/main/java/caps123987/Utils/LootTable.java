package caps123987.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import caps123987.DungeonGenerator.DungeonGenerator;
import caps123987.Types.ItemWRarity;

public class LootTable {
	final List<ItemWRarity> itemsList;
	final int maxItems;
	final int maxPercent;
	final Material material;
	
	public LootTable() {
		itemsList = DungeonGenerator.chestItemsList;
		
		maxItems = 10;

		material = Material.CHEST;

		int maxPercent = 0;
		for(ItemWRarity i: itemsList) {
			maxPercent += i.getRarity();
		}
		this.maxPercent = maxPercent;

		
	}
	public LootTable(Material m, int maxItems){

		if(m.equals(Material.TRAPPED_CHEST)){
			itemsList = DungeonGenerator.trappedChestItemsList;
		}else if(m.equals(Material.BARREL)){
			itemsList = DungeonGenerator.barrelItemsList;
		}else if(m.equals(Material.DECORATED_POT)){
			itemsList = DungeonGenerator.potItemsList;;
		}else {
			itemsList = DungeonGenerator.chestItemsList;
		}

		if(maxItems>4) {
			this.maxItems = DunUtils.getRandomValue(maxItems - 3, maxItems);
		}else{
			this.maxItems = maxItems;
		}
		material = m;

		int maxPercent = 0;
		for(ItemWRarity i: itemsList) {
			maxPercent += i.getRarity();
		}
		this.maxPercent = maxPercent;
	}

	public LootTable(int maxItems) {
		itemsList = DungeonGenerator.chestItemsList;

		this.maxItems = maxItems;

		material = Material.CHEST;

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
