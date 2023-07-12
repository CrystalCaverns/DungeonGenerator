package caps123987.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import caps123987.Generator.GenStart;
import net.md_5.bungee.api.ChatColor;

public class CommListener implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED+"You must be a Player to use this command");
			return true;
		}
		
		Player p = (Player)sender;
		
		if(!sender.hasPermission("DungeonGenerator.admin")) {
			sender.sendMessage(ChatColor.RED+"You don't have permissions to use this command");
			return true;
		}
		
		if(args.length <1) {
			sender.sendMessage(ChatColor.RED+"Not enough args");
			return true;
		}
		
		String subCommand = args[0];
		
		if(subCommand.equals("start")) {
			new GenStart(p.getLocation(),5);
			return true;
		}
		
		return true;
	}

}
