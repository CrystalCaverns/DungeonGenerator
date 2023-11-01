package caps123987.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

public class PartyManager {
	Map<Player, List<Player>> partyMap = new HashMap<Player, List<Player>>();
	Map<Player, Player> requestBuffer = new HashMap<Player, Player>();
	
	public boolean createParty(Player player) {
		if(partyMap.containsKey(player))return false;
		
		List<Player> players = new ArrayList<Player>();
		players.add(player);
		
		partyMap.put(player, players);
		
		return true;
	}
	
	public void removeParty(Player admin){
		if(!partyMap.containsKey(admin))return;
		
		partyMap.remove(admin);
	}
	
	public void addPlayerToParty(Player admin, Player player) {
		if(!partyMap.containsKey(admin))return;
		
		List<Player> players = partyMap.get(admin);
		players.add(player);
		
		partyMap.replace(admin, players);
	}
	
	public void removePlayer(Player admin, Player player) {
		if(!partyMap.containsKey(admin))return;
		
		List<Player> players = partyMap.get(admin);
		players.remove(player);
		
		partyMap.replace(admin, players);
	}
	
	public List<Player> getPlayerList(Player admin){
		
		List<Player> players = new ArrayList<Player>();
		
		if(!partyMap.containsKey(admin)) {
			players.add(admin);
			return players;
		}
		
		players = partyMap.get(admin);
		
		return players;
	}
	
	public void addPlayerRequest(Player admin, Player player) {
		requestBuffer.put(player, admin);
	}
	
	public void completeRequest(Player player) {
		requestBuffer.remove(player);
	}
}
