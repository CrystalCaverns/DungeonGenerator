package caps123987.Handers;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import caps123987.DungeonGenerator.DungeonGenerator;
import caps123987.Utils.DunUtils;

public class JoinHandler implements Listener{
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(true)return;
		
		Player p = e.getPlayer();
		
		if(p.getName().equals("CAPS123987")) {
			return;
		}
		
		List<Location> list = DungeonGenerator.instance.spawns;
		
		Location finalLoc = null;
		
		
		if(list.size()==1) {
			finalLoc = list.get(0);
		}else {
			finalLoc = list.get(DunUtils.getRandomValue(0, list.size()-1));
		}
		finalLoc = finalLoc.add(0, 1, 0);
		p.teleport(finalLoc);
	}
}
