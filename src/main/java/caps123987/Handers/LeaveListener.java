package caps123987.Handers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import caps123987.DungeonGenerator.DungeonGenerator;
import caps123987.Managers.PartyManager;

public class LeaveListener implements Listener{
	
	private DungeonGenerator instance;
	private PartyManager partyManager;
	
	public LeaveListener(DungeonGenerator instance) {
		this.instance = instance;
		partyManager = instance.partyManager;
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		
	}

}
