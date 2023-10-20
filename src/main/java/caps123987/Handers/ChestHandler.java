package caps123987.Handers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import caps123987.DungeonGenerator.DungeonGenerator;
import caps123987.Managers.ChestManager;

public class ChestHandler implements Listener{
	
	private DungeonGenerator plugin;
	private ChestManager chestManager;
	
	public ChestHandler(DungeonGenerator p) {
		plugin = p;
		chestManager = p.chestManager;
	}
	
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent e) {
		if(e.getClickedBlock()==null) {
			return;
		}
		
		if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		
		if(!(e.getClickedBlock().getType().equals(Material.CHEST)||e.getClickedBlock().getType().equals(Material.BARREL))) {
			return;
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()->open(e),1);
		
	}
	
	@EventHandler
	public void playerBreakEvent(BlockBreakEvent e) {
		Block b = e.getBlock();
		
		if(!(b.getType().equals(Material.CHEST)||b.getType().equals(Material.BARREL)||b.getType().equals(Material.DECORATED_POT))) {
			return;
		}
		
		
		Inventory inv = chestManager.getInventory(b);
		
		inv.forEach((ItemStack item)->{
			if(item!=null) {
				b.getWorld().dropItemNaturally(b.getLocation(), item);
			}
		});
		
		
		inv.clear();
		
	}
	
	public void open(PlayerInteractEvent e) {
		
		Player p = e.getPlayer();
		Block b = e.getClickedBlock();
		
		
		p.closeInventory();
		
		p.openInventory(chestManager.getInventory(b));
		
		
		
		/*if(!chestManager.isEnabled(b)) {
			return;
		}*/
		
		
		
		/*Inventory i = Bukkit.createInventory(p, 9,"test");
		
		p.openInventory(i);	*/
	}
}
