package caps123987.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import caps123987.DungeonGenerator.DungeonGenerator;
import caps123987.Types.ItemWRarity;

public class LootTable {
	final List<ItemWRarity> list;
	final int maxItems;
	final int maxPercent;
	
	public LootTable() {
		list = DungeonGenerator.itemsList;
		
		maxItems = 10;
		
		int maxPercent = 0;
		for(ItemWRarity i: list) {
			maxPercent += i.getRarity();
		}
		this.maxPercent = maxPercent;

		
	}
	public LootTable(int maxItems) {
		list = DungeonGenerator.itemsList;
		this.maxItems = maxItems;
		
		int maxPercent = 0;
		for(ItemWRarity i: list) {
			maxPercent += i.getRarity();
		}
		this.maxPercent = maxPercent;
	}
	
	public List<ItemStack> generate(){
		
		List<ItemStack> items = new ArrayList<ItemStack>();
		
		for(int i = 0;i!=maxItems;i++) {
			int value = DunUtils.getRandomValue(0,maxPercent);
			
			
			int current = 0;
			for(ItemWRarity item: list) {
				
				if(inRange(value,current,current+item.getRarity())) {
					items.add(item.getItem());
					break;
				}
				current+=item.getRarity();
				
			}
			
		}
		
		return items;
	}
	
	private boolean inRange(int value,int min,int max) {
		return (value>=min&&max>=value);
	}
	
}
