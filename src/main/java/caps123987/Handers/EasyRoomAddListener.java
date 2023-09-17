package caps123987.Handers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import caps123987.Managers.EasyRoomHandler;

public class EasyRoomAddListener implements Listener{
	
	EasyRoomHandler handler;
	
	public EasyRoomAddListener(EasyRoomHandler handler) {
		this.handler = handler;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		
		Player p = e.getPlayer();
		
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		
		ItemStack item = p.getItemInHand();
		
		ItemMeta meta = item.getItemMeta();
		
		if(meta==null) {
			return;
		}
		
		if(!meta.hasCustomModelData()) {
			return;
		}
		
		
		String name = meta.getDisplayName();
		
		
		if(name.equals("§rEntrance")) {
			
			
			p.sendMessage("entrance");
		}
		if(name.equals("§rExit")){
			
			
			p.sendMessage("exit");
		}
		
		e.setCancelled(true);
		
	}

}
