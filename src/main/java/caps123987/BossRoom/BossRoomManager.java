package caps123987.BossRoom;

import caps123987.DungeonGenerator.DungeonGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class BossRoomManager implements CommandExecutor {

	public int size = 3;
	public Location origin;
	public List<Boolean> roomList = new ArrayList<>();
	public BossRoomManager(int size, Location origin) {
		this.size = size;
		this.origin = origin;
	}
	public void setNewOrigin(Location origin){
		this.origin = origin.clone().subtract(0,20,0);

		Bukkit.getScheduler().runTaskLater(DungeonGenerator.instance,()->{
			for (int i = 0; i < 1001; i++) {
				getXY(i).getBlock().setType(Material.YELLOW_CONCRETE);
			}
		},100L);

	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String subCommand = args[0];

        return true;
    }

	public Location getXY(int id){
		id++;
		int k = (int) Math.ceil((double)(Math.sqrt((double)id) - 1) / 2.0);
		int t = 2 * k + 1;
		int m = t*t;
		t = t - 1;
		if (id >= (m - t)) {
			return origin.clone().add(size * (-k),0,size * (k - (m - id)));
		}else{
			m=m-t;
			if (id >= (m - t)) {
				return origin.clone().add(size * (-k + (m - id)),0,size * (-k));
			}else {
				m = m - t;
				if(id >= (m - t)) {
					return origin.clone().add(size * (k),0,size * (-k + (m - id)));
				}else {
					return origin.clone().add(size * (k - ((m - t)- id)),0,size * (k));
				}
			}
		}
	}


}
