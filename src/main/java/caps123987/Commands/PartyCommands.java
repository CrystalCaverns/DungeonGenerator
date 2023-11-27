package caps123987.Commands;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import caps123987.DungeonGenerator.DungeonGenerator;
import caps123987.Managers.PartyManager;
import net.md_5.bungee.api.ChatColor;

public class PartyCommands implements CommandExecutor{
	private DungeonGenerator instance;
	private PartyManager partyManager;
	
	public PartyCommands(DungeonGenerator instance) {
		this.instance = instance;
		partyManager = instance.partyManager;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		Player p = Bukkit.getPlayer(args[0]);



		String subCommand = args[1];
		
		String playerInput = null;
		try {
			playerInput = args[2];
		}catch(Exception e) {}
		
		
		if(subCommand.equals("create")) {
			if(partyManager.createParty(p)) {
				p.sendMessage("Party Created");
			}else {
				p.sendMessage("You already have party");
			}
		}
		
		if(subCommand.equals("remove")) {
			if(partyManager.removeParty(p)) {
				p.sendMessage("Party removed");
			}else {
				p.sendMessage("You don't have any party");
			}
		}
		
		if(subCommand.equals("addPlayer")) {
			if(playerInput==null) {
				p.sendMessage("Player can not be null");
				return true;
			}
			
			Player player = Bukkit.getPlayer(playerInput);
			if(player == null) {
				p.sendMessage("We coudln't find this player");
				return true;
			}
			
			if(partyManager.isPartyAdmin(player)) {
				p.sendMessage("Player is admin of a party");
				return true;
			}
			
			if(partyManager.isPartyAdmin(p)) {
				p.sendMessage("You don't have active party");
				return true;
			}
			
			if(partyManager.isInParty(player)) {
				p.sendMessage("Player is another party");
				return true;
			}
			
			if(partyManager.hasPartyRequest(player)) {
				p.sendMessage("You can't send party request, player has other pending requests");
				return true;
			}
			
			partyManager.addPlayerRequest(p, player);
			
			p.sendMessage("Request was send to: "+player.getName());
			
			player.sendMessage("You recived party request from: "+ChatColor.RED+p.getName());
		}
		
		if(subCommand.equals("removePlayer")) {
			if(playerInput==null) {
				p.sendMessage("Player can not be null");
				return true;
			}
			
			Player player = Bukkit.getPlayer(playerInput);
			if(player == null) {
				p.sendMessage("We coudln't find this player");
				return true;
			}
			
			if(partyManager.isPartyAdmin(p)) {
				p.sendMessage("You don't have active party");
				return true;
			}
			
			if(!partyManager.partyContainsPlayer(p, player)) {
				p.sendMessage("Your party doesn't have this player");
				return true;
			}
			
			partyManager.removePlayer(p, player);
			p.sendMessage("You removed player "+player.getName());
			player.sendMessage("You have been removed from "+ChatColor.RED+p.getName()+ChatColor.RESET+"'s party");
		}
		
		if(subCommand.equals("removeMe")) {
			Player admin = partyManager.removeMe(p);
			if(admin!=null) {
				p.sendMessage("You have been removed from party");
				admin.sendMessage("Player "+ChatColor.RED+p.getName()+ChatColor.RESET+" left your party");
			}else {
				p.sendMessage("You can't be removed from party");
			}
		}
		
		if(subCommand.equals("confirm")) {
			if(!partyManager.hasPartyRequest(p)) {
				p.sendMessage("You don't have any pending requests");
				return true;
			}
			
			Player admin = partyManager.getRequestAdmin(p);
			
			partyManager.addPlayerToParty(admin, p);
			partyManager.completeRequest(p);
			admin.sendMessage("Player "+ChatColor.RED+p.getName()+ChatColor.RESET+" accepted your party request");
			p.sendMessage("You accepted party request");
		}
		
		if(subCommand.equals("decline")) {
			if(!partyManager.hasPartyRequest(p)) {
				p.sendMessage("You don't have any pending requests");
				return true;
			}
			
			Player admin = partyManager.getRequestAdmin(p);
			
			partyManager.completeRequest(p);
			admin.sendMessage("Player "+ ChatColor.RED +p.getName()+ChatColor.RESET+" declined your party request");
			p.sendMessage("You declined party request");
		}
		
		if(subCommand.equals("listAll")) {
			for(Map.Entry<Player,List<Player>> entry : partyManager.getMap().entrySet()) {
				p.sendMessage(entry.getKey().getName());
				for(Player listPlayer : entry.getValue()) {
					p.sendMessage("     "+listPlayer.getName());
				}
			}
		}
		
		return true;
	}
}
