package caps123987.Handers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import caps123987.DungeonGenerator.DungeonGenerator;
import caps123987.Managers.ChestManager;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ChestHandler implements Listener{
	
	private DungeonGenerator plugin;
	private ChestManager chestManager;
	private Map<Integer,String> customModelDataMap = new HashMap<Integer,String>();
	
	public ChestHandler(DungeonGenerator p) {
		plugin = p;
		chestManager = p.getChestManager();

		customModelDataMap.put(334,"rare;\uDBC7\uDCB8\uDBE3\uDC1A rare");
		customModelDataMap.put(335,"epic;\uDBC7\uDCB8\uDBD3\uDD8B epic");
		customModelDataMap.put(336,"legendary;\uDBC7\uDCB8\uDBEA\uDC8C legendary");
	}
	
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent e) {
		normal(e);
		pot(e);
	}
	
	@EventHandler
	public void playerBreakEvent(BlockBreakEvent e) {
		Block b = e.getBlock();
		
		if(!(b.getType().equals(Material.DECORATED_POT))) {
			return;
		}

		e.setDropItems(false);

		breakPot(b);
	}

	private void normal(PlayerInteractEvent e){
		if(e.getClickedBlock()==null) {
			return;
		}

		if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}

		if(!(e.getClickedBlock().getType().equals(Material.BARRIER))){
			return;
		}

		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()->open(e),1);
	}

	private void pot(PlayerInteractEvent e){
		if(!Objects.equals(e.getHand(), EquipmentSlot.HAND)){
			return;
		}

		if(!(e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))){
			return;
		}

		if(e.getClickedBlock()==null){
			return;
		}

		if(!e.getClickedBlock().getType().equals(Material.DECORATED_POT)){
			return;
		}

		e.setCancelled(true);

		if(!e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
			return;
		}

		Block b = e.getClickedBlock();

		breakPot(b);

		b.setType(Material.AIR);

		b.getWorld().spawnParticle(org.bukkit.Particle.BLOCK_CRACK, b.getLocation().clone().add(.5,.5,.5), 60,.30,.30,.30, Bukkit.createBlockData(Material.DECORATED_POT));
		b.getWorld().playSound(b.getLocation(), Sound.BLOCK_DECORATED_POT_SHATTER, 1, 1);
		//b.breakNaturally(true);

		//Bukkit.getPluginManager().callEvent(new BlockBreakEvent(b, e.getPlayer()));

	}

	@EventHandler
	public void projectileHitEvent(ProjectileHitEvent e) {


		Block b = e.getHitBlock();

		if(b==null) {
			return;
		}

		if(!b.getType().equals(Material.DECORATED_POT)) {
			return;
		}

		DungeonGenerator.instance.getChestHandler().breakPot(b);
	}

	public void open(PlayerInteractEvent e) {

		Player p = e.getPlayer();
		Block b = e.getClickedBlock();


		p.closeInventory();

		Optional<ItemDisplay> optionalDisplay = b.getLocation().clone().add(.5,.5,.5).getNearbyEntitiesByType(ItemDisplay.class, 1).stream().findFirst();

		if(!optionalDisplay.isPresent()){
			return;
		}

		ItemDisplay display = optionalDisplay.get();

		ItemMeta meta = display.getItemStack().getItemMeta();

		if(meta==null) {
			return;
		}

		if(!meta.hasCustomModelData()) {
			return;
		}

		String[] encodedName = customModelDataMap.get(meta.getCustomModelData()).split(";");

		if(encodedName[0] != null) {
			p.openInventory(chestManager.getInventory(b, encodedName[0], encodedName[1]));
		}


	}

	public void breakPot(Block b) {
		Inventory inv = chestManager.getInventory(b,"DECORATED_POT","");

		inv.forEach((ItemStack item)->{
			if(item!=null) {
				b.getWorld().dropItemNaturally(b.getLocation(), item);
			}
		});

		inv.clear();
	}
}
