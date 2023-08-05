package caps123987.Commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import caps123987.DungeonGenerator.DungeonGenerator;
import caps123987.Generator.GenStart;
import caps123987.Types.DunMater;
import caps123987.Types.DunType;
import net.md_5.bungee.api.ChatColor;

public class CommListener implements CommandExecutor{

	public DungeonGenerator instance = DungeonGenerator.getInstance();
	
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
			sender.sendMessage("uploadSch");
			
			Path pa = instance.getServer().getWorldContainer().toPath().resolve("world").resolve("generated").resolve("minecraft").resolve("structures");
			
			for(DunType type : DunType.values()) {
				
				for(DunMater ma: DunMater.values()) {
					for(int i = 1;i<4;i++) {
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
			
			
			
			return true;
		}
		
		return true;
	}
	
	public static void copyFiles(File sourceLocation , File targetLocation)
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
		
		try {
			yaml.save(new File(instance.invFile,1+".yml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
