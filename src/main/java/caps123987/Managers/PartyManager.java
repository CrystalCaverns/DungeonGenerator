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
	
	public boolean removeParty(Player admin){
		if(!partyMap.containsKey(admin))return false;
		
		partyMap.remove(admin);
		return true;
	}
	
	public Map<Player, List<Player>> getMap(){
		return partyMap;
	}
	
	public boolean isPartyAdmin(Player admin) {
		return partyMap.containsKey(admin);
	}
	
	public boolean isInParty(Player player) {
		for(Map.Entry<Player,List<Player>> entry : partyMap.entrySet()) {
			for(Player listPlayer : entry.getValue()) {
				if(listPlayer.getName().equals(player.getName())) {
					return true;
				}
			}
		}
		return false;
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
	
	public Player removeMe(Player player) {
		if(partyMap.containsKey(player)) {
			return null;
		}
		
		for(Map.Entry<Player,List<Player>> entry : partyMap.entrySet()) {
			for(Player listPlayer : entry.getValue()) {
				if(listPlayer.getName().equals(player.getName())) {
					removePlayer(entry.getKey(),listPlayer);
					return entry.getKey();
				}
			}
		}
		return null;
	}
	
	public boolean partyContainsPlayer(Player admin, Player player) {
		return getPlayerList(admin).contains(player);
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
	
	public boolean hasPartyRequest(Player player) {
		return requestBuffer.containsKey(player);
	}
	
	public Player getRequestAdmin(Player player) {
		return requestBuffer.get(player);
	}
	
}
