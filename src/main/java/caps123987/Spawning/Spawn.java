package caps123987.Spawning;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

import caps123987.DungeonGenerator.DungeonGenerator;

public class Spawn {
	private final int maxLimit = 30;
	private final int maxSize = 30;
	private final int minLimit = 10;
	private final int maxZombie = 50;
	
	public Spawn() {
		
		List<Entity> entities = Bukkit.getWorld("world").getEntities();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			if(entities.size()<maxZombie) {
				for(Location spawn:DungeonGenerator.spawns) {
					int distance = (int) player.getLocation().distance(spawn);
					
					int rozdil = (int) (spawn.getY()-player.getLocation().getY());
					
					boolean skip = false;
					for(Entity e:entities) {
						if(e instanceof Player)continue;
						
						int distance2 = (int) spawn.distance(e.getLocation());
						
						if(distance2>5) {
							skip = true;
						}
					}
					if(rozdil<-4||rozdil>4) {
						skip = true;
					}
					
					if(!skip) {
						if(distance<maxLimit&&distance>minLimit) {
							
							Zombie z = (Zombie) spawn.getWorld().spawnEntity(spawn.clone().add(0,1, 0), EntityType.ZOMBIE);
							z.setTarget(player);
						}
					}
				}
			}
			
			for(Entity e:entities) {
				if(e instanceof Player)continue;
				
				int distance = (int) player.getLocation().distance(e.getLocation());
				
				if(distance>maxSize) {
					e.remove();
				}
			}
			
		}
	}
}
