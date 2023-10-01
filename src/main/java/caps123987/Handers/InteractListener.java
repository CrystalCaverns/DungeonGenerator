package caps123987.Handers;

import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import caps123987.Managers.EasyRoomHandler;

public class InteractListener implements Listener{
	
	EasyRoomHandler handler;
	
	public InteractListener(EasyRoomHandler handler) {
		this.handler = handler;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		tools(e);
		
		key(e);
		
	}
	
	private void key(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		
		if(!(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)
				||e.getAction().equals(Action.RIGHT_CLICK_AIR))) {
			return;
		}
		
		ItemStack item = p.getInventory().getItemInMainHand();
		
		ItemMeta meta = item.getItemMeta();
		
		if(meta==null) {
			return;
		}
		
		if(!meta.hasCustomModelData()) {
			return;
		}
		
		if(meta.getCustomModelData()==8) {
			List<Entity> entities = p.getNearbyEntities(3, 2, 3);
			for(Entity entity:entities) {
				if(entity.getScoreboardTags().contains("Door")) {
					
					Block origin = entity.getLocation().getBlock();
					
					for(int x = -1; x != 2; x++) {
						for(int y = 0; y != 3; y++) {
							for(int z = -1; z != 2; z++) {
								Block newBlock = origin.getRelative(x, y, z);
								if(newBlock.getType().equals(Material.IRON_BARS)) {
									newBlock.setType(Material.AIR);
								}
							}
						}
					}
					
					entity.remove();
					
					return;	
				}
			}
			e.setCancelled(true);
		}
	}
	
	private void tools(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		
		
		if(!p.hasPermission("DungeonGenerator.admin")) {
			
			return;
		}
		if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		
		ItemStack item = p.getInventory().getItemInMainHand();
		
		ItemMeta meta = item.getItemMeta();
		
		if(meta==null) {
			return;
		}
		
		if(!meta.hasCustomModelData()) {
			return;
		}
		
		
		String name = meta.getDisplayName();
		
		Block clicked = e.getClickedBlock();
		
		if(name.equals("�rEntrance")) {
			
			spawnArmor(p,clicked,"Entrance",Material.MAGENTA_GLAZED_TERRACOTTA);
			
			handler.setEntrance(clicked);
			
			p.sendMessage("Entrance: " + clicked.getLocation());
			
		}
		
		if(name.equals("�rExit")){
			
			ArmorStand armor = spawnArmor(p,clicked,"Exit",Material.MAGENTA_GLAZED_TERRACOTTA);

			handler.addExit(clicked,(int) armor.getLocation().getYaw());
			
			p.sendMessage("Exit: " + clicked.getLocation());
			
		}
		
		if(name.equals("�rCorner")){
			
			if(handler.getCorner1()==null) {
				
				spawnArmor(p,clicked,"Corner1",Material.GREEN_CONCRETE);

				p.sendMessage("1 Corner: " + clicked.getLocation());
				
				handler.setCorner1(clicked);
				
				handler.setCorner2(null);
			}else if(handler.getCorner2()==null){
				
				spawnArmor(p,clicked,"Corner2",Material.RED_CONCRETE);
				
				p.sendMessage("2 Corner: " + clicked.getLocation());
				
				handler.setCorner2(clicked);
			}else {
				p.sendMessage("please remove other corners");
			}
			
			
		}
		e.setCancelled(true);
	}
	
	
	@EventHandler
	public void PlayerDealDamage(EntityDamageByEntityEvent e) {
		
		if(!e.getEntity().getType().equals(EntityType.ARMOR_STAND)) {
			return;
		}
		
		if(!e.getDamager().getType().equals(EntityType.PLAYER)) {
			return;
		}
		
		Set<String> tags = e.getEntity().getScoreboardTags();
		
		if(tags.contains("Entrance")) {
			e.getDamager().sendMessage("Entrance clear");
			handler.setEntrance(null);
		}
		
		if(tags.contains("Exit")) {
			e.getDamager().sendMessage("Exit removed");
			handler.removeExit(e.getEntity().getLocation().getBlock());
		}
		
		if(tags.contains("Corner1")) {
			e.getDamager().sendMessage("Corner1");
			handler.setCorner1(null);
		}
		
		if(tags.contains("Corner2")) {
			e.getDamager().sendMessage("Corner2");
			handler.setCorner2(null);
		}
		
		
	}
	
	private ArmorStand spawnArmor(Player p,Block b, String tag, Material m) {
		int rotation = Math.round(p.getLocation().getYaw() / 90) * 90;
		
		Location loc = b.getLocation().add(.5, 0, .5); 
		
		ArmorStand armor = (ArmorStand) b.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		armor.setRotation(rotation,0);
		
		armor.addScoreboardTag(tag);
		armor.setCustomName(tag);
		armor.setCustomNameVisible(true);
		armor.getEquipment().setHelmet(new ItemStack(m));
		armor.setGravity(false);
		
		return armor;
	}
}
