package caps123987.Types;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

public class ItemWRarity implements ConfigurationSerializable{
	private ItemStack item;
	private int rarity;
	
	public ItemWRarity() {
		
	}
	
	public ItemWRarity(ItemStack item, int rarity) {
		this.setItem(item);
		this.setRarity(rarity);
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("item", getItem());
		map.put("rarity", getRarity());
		
		return map;
	}
	
	public static ItemWRarity deserialize(Map<String, Object> args) {
		ItemWRarity itemWRarity = new ItemWRarity();
		itemWRarity.setItem((ItemStack) args.get("item"));
		itemWRarity.setRarity((int) args.get("rarity"));
		return itemWRarity;
		
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public int getRarity() {
		return rarity;
	}

	public void setRarity(int rarity) {
		this.rarity = rarity;
	}

}
