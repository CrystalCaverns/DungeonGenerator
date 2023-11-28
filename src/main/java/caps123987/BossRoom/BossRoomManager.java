package caps123987.BossRoom;

import caps123987.DungeonGenerator.DungeonGenerator;
import com.github.shynixn.structureblocklib.api.bukkit.StructureBlockLibApi;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BossRoomManager implements CommandExecutor {

	/*
		true=full
		false=empty
	 */

	public int size = 10;
	public Location origin;
	public List<Boolean> roomList = new ArrayList<>();
	DungeonGenerator instance;
	public BossRoomManager(int size, Location origin, DungeonGenerator instance) {
		this.size = size;
		this.origin = origin;
		this.instance = instance;
	}

	public void setorigin(Location origin){
		this.origin = origin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.hasPermission("DungeonGenerator.admin")) {
			sender.sendMessage(ChatColor.RED+"You don't have permissions to use this command");
			return true;
		}

		String player = args[0];
		String arg = args[1];


		Player p = Bukkit.getPlayer(player);
		if(arg.equals("tptoroom")){
			int roomId = findEmpty();
			Location loc = setUpRoom(roomId);
			p.teleport(loc);
		}

		if(arg.equals("list")){
			sender.sendMessage("Registered rooms:");
			int idx = 0;
			for (boolean b : roomList) {
				sender.sendMessage("  idx: "+idx+" state: "+b);
				idx++;
			}
		}

		if(arg.equals("set")) {
			String arg2 = args[2];
			String arg3 = args[3];
			int idx = Integer.parseInt(arg2);
			roomList.set(idx,Boolean.getBoolean(arg3));
		}

		return true;
    }

	public int findEmpty(){
		int idx = 0;

		while(idx<roomList.size()){
			if(!roomList.get(idx)){
				return idx;
			}
			idx++;
		}
		return roomList.size();
	}

	public Location setUpRoom(int roomId){
		if(roomId == roomList.size()){
			roomList.add(true);
		}else{
			roomList.set(roomId,true);
		}

		Location roomLoc = getXY(roomId);

		StructureBlockLibApi.INSTANCE
				.loadStructure(instance)
				.at(roomLoc)
				.includeEntities(true)
				.loadFromFile(instance.boss)
				.onException((Throwable t)->{Bukkit.broadcastMessage("Error While generation boss Room "+t);});
		return roomLoc;
	}

	public Location getXY(int id){
		id++;
		int k = (int) Math.ceil((double)(Math.sqrt((double)id) - 1) / 2.0);
		int t = 2 * k + 1;
		int m = t*t;
		t = t - 1;
		if (id >= (m - t)) {
			return origin.clone().add(size * (-k),-size-10,size * (k - (m - id)));
		}else{
			m=m-t;
			if (id >= (m - t)) {
				return origin.clone().add(size * (-k + (m - id)),-size-10,size * (-k));
			}else {
				m = m - t;
				if(id >= (m - t)) {
					return origin.clone().add(size * (k),-size-10,size * (-k + (m - id)));
				}else {
					return origin.clone().add(size * (k - ((m - t)- id)),-size-10,size * (k));
				}
			}
		}
	}

	public void reset(){
		roomList = new ArrayList<Boolean>();
	}
}
