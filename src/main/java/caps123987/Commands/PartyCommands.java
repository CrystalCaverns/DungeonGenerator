package caps123987.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import caps123987.DungeonGenerator.DungeonGenerator;
import caps123987.Managers.PartyManager;

public class PartyCommands implements CommandExecutor{
	public DungeonGenerator instance;
	public PartyManager partyManager;
	
	public PartyCommands(DungeonGenerator instance) {
		this.instance = instance;
		partyManager = instance.partyManager;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("You must be player to use this command");
		}
		Player p = (Player) sender;
		
		String subCommand = args[0];
		String playerInput = args[1];
		
		if(subCommand.equals("create")) {
			if(partyManager.createParty(p)) {
				p.sendMessage("Party Created");
			}else {
				p.sendMessage("Party already exists");
			}
		}
		
		if(subCommand.equals("add")) {
			if(playerInput==null) {
				p.sendMessage("Player can not be null");
				return true;
			}
			
			Player player = Bukkit.getPlayer(playerInput);
			if(player == null) {
				p.sendMessage("We coudln't find this player");
				return true;
			}
			
			partyManager.addPlayerRequest(p, player);
			
			p.sendMessage("Request was send to: "+player.getName());
		}
		
		
		return true;
	}
}
