package caps123987.Commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Container;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import com.github.shynixn.structureblocklib.api.bukkit.StructureBlockLibApi;

import caps123987.DungeonGenerator.DungeonGenerator;
import caps123987.Generator.GenStart;
import caps123987.Managers.ChestManager;
import caps123987.Types.DunMater;
import caps123987.Types.DunType;
import caps123987.Utils.DunUtils;
import net.md_5.bungee.api.ChatColor;

public class CommListener implements CommandExecutor{

	public DungeonGenerator instance = DungeonGenerator.getInstance();
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED+"You must be a Player to use this command");
			return true;
		}
		
		Player p = (Player)sender;
		
		String subCommand = args[0];
		
		if(!sender.hasPermission("DungeonGenerator.admin")) {
			sender.sendMessage(ChatColor.RED+"You don't have permissions to use this command");
			return true;
		}
		
		if(args.length <1) {
			sender.sendMessage(ChatColor.RED+"Not enough args");
			return true;
		}
		
		if(subCommand.equals("respawn")) {
			sender.sendMessage("Respawning");
			respawn(p);
			return true;
		}
		
		if(subCommand.equals("start")) {
			sender.sendMessage("generationg");
			new GenStart(p.getLocation(),5);
			return true;
		}
		
		if(subCommand.equals("geninv")) {
			sender.sendMessage("geninv");
			createInv(p);
			return true;
		}
		
		if(subCommand.equals("uploadSch")) {
			uploadSch(sender);
			
			return true;
		}
		
		return true;
	}
	public void respawn(Player p) {
		List<Location> list = DungeonGenerator.instance.spawns;
		
		Location finalLoc = null;
		
		
		if(list.size()==1) {
			finalLoc = list.get(0);
		}else {
			finalLoc = list.get(DunUtils.getRandomValue(0, list.size()-1));
		}
		finalLoc = finalLoc.add(0, 1, 0);
		p.teleport(finalLoc);
	}
	
	public void uploadSch(CommandSender sender) {
		sender.sendMessage("uploadSch");
		
		Path pa = instance.getServer().getWorldContainer().toPath().resolve("world").resolve("generated").resolve("minecraft").resolve("structures");
		
		for(DunType type : DunType.values()) {
			
			for(DunMater ma: DunMater.values()) {
				for(int i = 1;i<6;i++) {
					File f = pa.resolve(type.name()+"_"+ma.name()+"_"+i+".nbt").toFile();
					if(f.exists()) {
						try {
							copyFiles(f,instance.getDataFolder());
						} catch (IOException e) {
							sender.sendMessage("error while coping file: "+f.getName()+" with error: "+e.toString());
						}
					}
				}
			}
		}
	}
	
	public void copyFiles(File sourceLocation , File targetLocation)
	    throws IOException {

        if (!targetLocation.exists()) {
            targetLocation.mkdir();
        }
        
        InputStream in = new FileInputStream(sourceLocation);
        OutputStream out = new FileOutputStream(targetLocation+"/"+sourceLocation.getName());
        
        out.write(in.readAllBytes());
        
        in.close();
        out.close();        
	}
	public void createInv(Player p) {
		Container ch = (Container) p.getTargetBlockExact(5).getState();
		Inventory inv = ch.getInventory();
		
		FileConfiguration yaml=YamlConfiguration.loadConfiguration(instance.invFile);
		yaml.set("Items", inv.getContents());
		
		int size = ChestManager.getInvs(instance.maxInv, instance.invFile).size()+1;
		
		try {
			yaml.save(new File(instance.invFile,size+".yml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
