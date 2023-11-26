package caps123987.Commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class AdminTabC implements TabCompleter{

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

		if(!(sender instanceof Player)) {
			return Collections.emptyList();
		}
		
		if(!sender.hasPermission("DungeonGenerator.admin")) {
			return Collections.emptyList();
		}
		
		int lenght = args.length;
		
		
		if(lenght==1) {
			List<String> list = new ArrayList<String>();
			list.add("uploadSch");
			list.add("start");
			list.add("addItemToLoot");
			list.add("creatorTools");
			list.add("respawn");
			list.add("roomCalculate");
			list.add("stopRun");
			return list;
		}
		
		if(lenght==2) {
			String subCommand = args[0];
			List<String> list = new ArrayList<String>();
			
			if(subCommand.equals("addItemToLoot")) {
				list.add("chest");
				list.add("trappedchest");
				list.add("barrel");
				list.add("pot");
				return list;
			}
			
			if(subCommand.equals("start")) {
				list.add("max height");
				return list;
			}
			
		}
		
		if(lenght==3) {
			String subCommand = args[0];
			List<String> list = new ArrayList<String>();

			if(subCommand.equals("addItemToLoot")) {
				list.add("Number As Rarity");
				return list;
			}

			if(subCommand.equals("start")) {
				list.add("max distance");
				return list;
			}
			
		}
		
		if(lenght==4) {
			String subCommand = args[0];
			List<String> list = new ArrayList<String>();
			
			if(subCommand.equals("start")) {
				list.add("min Rooms");
				return list;
			}
			
		}
		
		return Collections.emptyList();
	}
	
}
