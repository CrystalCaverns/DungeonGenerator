package caps123987.Commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class TabC implements TabCompleter{

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

		if(!(sender instanceof Player)) {
			return Collections.emptyList();
		}
		
		
		
		
		int lenght = args.length;
		
		
		if(lenght==1) {
			List<String> list = new ArrayList<String>();
			list.add("uploadSch");
			list.add("start");
			return list;
		}
		
		
		return Collections.emptyList();
	}
	
}
