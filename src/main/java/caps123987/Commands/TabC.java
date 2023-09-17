package caps123987.Commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class TabC implements TabCompleter{

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
			return list;
		}
		
		if(lenght==2) {
			String subCommand = args[1];
			List<String> list = new ArrayList<String>();
			
			if(subCommand.equals("addItemToLoot")) {
				list.add("Number As Rarity");
				return list;
			}
			
			if(subCommand.equals("start")) {
				list.add("can_generate_small_dungeon");
				return list;
			}
			
		}
		
		
		return Collections.emptyList();
	}
	
}
