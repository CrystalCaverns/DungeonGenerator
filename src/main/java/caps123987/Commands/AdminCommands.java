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
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import caps123987.DungeonGenerator.DungeonGenerator;
import caps123987.Generator.GenStart;
import caps123987.Managers.ChestManager;
import caps123987.Managers.EasyRoomHandler;
import caps123987.Managers.PartyManager;
import caps123987.Types.DunMater;
import caps123987.Types.DunType;
import caps123987.Types.ItemWRarity;
import caps123987.Utils.DunUtils;
import net.md_5.bungee.api.ChatColor;

public class AdminCommands implements CommandExecutor{

	public DungeonGenerator instance;
	private EasyRoomHandler handler;
	private PartyManager partyManager;
	
	public AdminCommands(DungeonGenerator instance) {
		this.instance = instance;
		handler = instance.easyRoomHandler;
		partyManager = instance.partyManager;
	}
	
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
			respawn(p);
			return true;
		}
		
		if(subCommand.equals("start")) {
			sender.sendMessage("gen");
			if(args.length == 1) {
				new GenStart(p.getLocation()).superStart();
			}else if(args.length == 2){
				new GenStart(p.getLocation(),Integer.parseInt(args[1])).superStart();
			}else if(args.length == 3) {
				new GenStart(p.getLocation(),Integer.parseInt(args[1]),Integer.parseInt(args[2])).superStart();
			}else if(args.length == 4) {
				new GenStart(p.getLocation(),Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3])).superStart();
			}
			
			return true;
		}
		
		if(subCommand.equals("addItemToLoot")) {
			sender.sendMessage("addItemToLoot");
			createInv(args[1],p,args[2]);
			return true;
		}
		
		if(subCommand.equals("stopRun")) {
			sender.sendMessage("stopRun");
			
			instance.asyncGenID.cancel();
			Bukkit.getScheduler().cancelTask(instance.asyncGenID.getTaskId());
			
			return true;
		}
		
		if(subCommand.equals("uploadSch")) {
			uploadSch(sender);
			
			return true;
		}
		
		if(subCommand.equals("creatorTools")) {
			creatorTools(p);
			return true;
		}
		
		if(subCommand.equals("roomCalculate")) {
			handler.generate();
			return true;
		}
		
		return true;
	}
	public void creatorTools(Player p) {
		ItemStack entranceItem = new ItemStack(Material.ARMOR_STAND,1);
		ItemMeta entranceItemMeta = entranceItem.getItemMeta();
		entranceItemMeta.setDisplayName("�rEntrance"); 
		entranceItemMeta.setCustomModelData(1);
		entranceItem.setItemMeta(entranceItemMeta);
		
		ItemStack exitItem = new ItemStack(Material.ARMOR_STAND,1);
		ItemMeta exitItemMeta = exitItem.getItemMeta();
		exitItemMeta.setDisplayName("�rExit"); 
		exitItemMeta.setCustomModelData(1);
		exitItem.setItemMeta(exitItemMeta);
		
		ItemStack cornerItem = new ItemStack(Material.ARMOR_STAND,1);
		ItemMeta cornerItemMeta = cornerItem.getItemMeta();
		cornerItemMeta.setDisplayName("�rCorner"); 
		cornerItemMeta.setCustomModelData(1);
		cornerItem.setItemMeta(cornerItemMeta);
		
		ItemStack keyItem = new ItemStack(Material.KNOWLEDGE_BOOK,1);
		ItemMeta keyItemMeta = keyItem.getItemMeta();
		keyItemMeta.setDisplayName(ChatColor.DARK_PURPLE+"Key"); 
		keyItemMeta.setCustomModelData(8);
		keyItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_POTION_EFFECTS,ItemFlag.HIDE_DYE);
		keyItem.setItemMeta(keyItemMeta);
		
		p.getInventory().addItem(entranceItem,exitItem,cornerItem,keyItem);
		
	}
	
	public void respawn(Player p) {
		
		boolean isAdmin = partyManager.isPartyAdmin(p);
		
		if(!(isAdmin||(!isAdmin&&!partyManager.isInParty(p)))) {
			p.sendMessage("Sorry but you can't be teleported");
			return;
		}
		
		List<Location> list = DungeonGenerator.instance.spawns;
		
		Location finalLoc = null;
		
		if(list.size()==1) {
			finalLoc = list.get(0);
		}else {
			finalLoc = list.get(DunUtils.getRandomValue(0, list.size()-1));
		}
		finalLoc = finalLoc.add(0, 1, 0);
		
		
		for(Player player:partyManager.getPlayerList(p)) {
			player.teleport(finalLoc);
		}
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
	@SuppressWarnings("unused")
	public void createInv(String material, Player p, String args) {
		
		ItemStack item = p.getInventory().getItemInMainHand();
		
		FileConfiguration yaml1=YamlConfiguration.loadConfiguration(instance.items);
		
		List<ItemWRarity> list = null;

        switch (material) {
            case "trappedchest":
                list = DungeonGenerator.trappedChestItemsList;
                break;
            case "barrel":
                list = DungeonGenerator.barrelItemsList;
                break;
            case "pot":
                list = DungeonGenerator.potItemsList;
                break;
            default:
                list = DungeonGenerator.chestItemsList;
                break;
        }
		
		list.add(new ItemWRarity(item,Integer.parseInt(args)));
		


		switch (material) {
			case "trappedchest":
				yaml1.set("trappedChestItems", list);
				DungeonGenerator.trappedChestItemsList = list;
				break;
			case "barrel":
				yaml1.set("barrelItems", list);
				DungeonGenerator.barrelItemsList = list;
				break;
			case "pot":
				yaml1.set("potItems", list);
				DungeonGenerator.potItemsList = list;
				break;
			default:
				yaml1.set("chestItems", list);
				DungeonGenerator.chestItemsList = list;
				break;
		}


		try {
			yaml1.save(instance.items);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		if(false) {
			Container ch = (Container) p.getTargetBlockExact(5).getState();
			Inventory inv = ch.getInventory();
			
			FileConfiguration yaml2=YamlConfiguration.loadConfiguration(instance.invFile);
			yaml2.set("Items", inv.getContents());
			
			int size = ChestManager.getInvs(instance.maxInv, instance.invFile).size()+1;
			
			try {
				yaml2.save(new File(instance.invFile,size+".yml"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
