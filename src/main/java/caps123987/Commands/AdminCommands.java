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
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import caps123987.DungeonGenerator.DungeonGenerator;
import caps123987.Generator.Generator;
import caps123987.Managers.EasyRoomHandler;
import caps123987.Managers.PartyManager;
import caps123987.Types.DunMater;
import caps123987.Types.DunType;
import caps123987.Types.ItemWRarity;
import net.md_5.bungee.api.ChatColor;

public class AdminCommands implements CommandExecutor{

	public DungeonGenerator instance;
	private EasyRoomHandler handler;
	private PartyManager partyManager;
	
	public AdminCommands(DungeonGenerator instance) {
		this.instance = instance;
		handler = instance.getEasyRoomHandler();
		partyManager = instance.getPartyManager();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		

		
		String subCommand = args[0];

		if(!sender.hasPermission("DungeonGenerator.admin")) {
			sender.sendMessage(ChatColor.RED+"You don't have permissions to use this command");
			return true;
		}

		if(subCommand.equals("respawn")) {
			Player subPlayer = Bukkit.getPlayer(args[1]);
			String subWorld = args[2];
			instance.respawn(subPlayer, subWorld);
			return true;
		}

		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED+"You must be a Player to use this command");
			return true;
		}
		Player p = (Player)sender;

		if(args.length <1) {
			sender.sendMessage(ChatColor.RED+"Not enough args");
			return true;
		}
		

		if(subCommand.equals("start")) {
			sender.sendMessage("gen");
			if(args.length == 1) {
				new Generator(p.getLocation()).superStart();
			}else if(args.length == 2){
				new Generator(p.getLocation(),Integer.parseInt(args[1])).superStart();
			}else if(args.length == 3) {
				new Generator(p.getLocation(),Integer.parseInt(args[1]),Integer.parseInt(args[2])).superStart();
			}else if(args.length == 4) {
				new Generator(p.getLocation(),Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3])).superStart();
			}
			
			return true;
		}
		
		if(subCommand.equals("addItemToLoot")) {
			sender.sendMessage("addItemToLoot");
			createItem(args[1],p,args[2]);
			return true;
		}

		if(subCommand.equals("removeItem")) {
			removeItems(p,args[1], args[2]);
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

	public void createItem(String material, Player p, String args) {
		
		ItemStack item = p.getInventory().getItemInMainHand();

		File itemsF = new File(DungeonGenerator.getInstance().getDataFolder(),"items/"+p.getLocation().getWorld().getName()+".yml");

		FileConfiguration yaml1=YamlConfiguration.loadConfiguration(itemsF);
		
		List<ItemWRarity> list = null;

        switch (material) {
            case "rare":
                list = DungeonGenerator.invMap.get(p.getLocation().getWorld().getName()).get("rare");
                break;
			case "epic":
				list = DungeonGenerator.invMap.get(p.getLocation().getWorld().getName()).get("epic");
				break;
			case "legendary":
				list = DungeonGenerator.invMap.get(p.getLocation().getWorld().getName()).get("legendary");
				break;
            default:
                list = DungeonGenerator.invMap.get(p.getLocation().getWorld().getName()).get("DECORATED_POT");
                break;
        }
		
		list.add(new ItemWRarity(item.clone(),Integer.parseInt(args)));
		


		switch (material) {
			case "rare":
				yaml1.set("rare", list);
				DungeonGenerator.invMap.get(p.getLocation().getWorld().getName()).replace("rare", list);
				break;
			case "epic":
				yaml1.set("epic", list);
				DungeonGenerator.invMap.get(p.getLocation().getWorld().getName()).replace("epic", list);
				break;
			case "legendary":
				yaml1.set("legendary", list);
				DungeonGenerator.invMap.get(p.getLocation().getWorld().getName()).replace("legendary", list);
				break;

			default:
				yaml1.set("potItems", list);
				DungeonGenerator.invMap.get(p.getLocation().getWorld().getName()).replace("DECORATED_POT", list);
				break;
		}


		try {
			yaml1.save(itemsF);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		/*
			Container ch = (Container) p.getTargetBlockExact(5).getState();
			Inventory inv = ch.getInventory();
			
			FileConfiguration yaml2=YamlConfiguration.loadConfiguration(instance.invFile);
			yaml2.set("Items", inv.getContents());
			
			int size = ChestManager.getInvs(instance.maxInv, instance.invFile).size()+1;
			
			try {
				yaml2.save(new File(instance.invFile,size+".yml"));
			} catch (IOException e) {
				TODO Auto-generated catch block
				e.printStackTrace();
			}
		*/
	}

	public void removeItems(Player p, String section, String itemName) {

		File itemsF = new File(DungeonGenerator.getInstance().getDataFolder(),"items/"+p.getLocation().getWorld().getName()+".yml");

		FileConfiguration yaml1=YamlConfiguration.loadConfiguration(itemsF);

		if(section.equals("pot")) {
			for (ItemWRarity item : DungeonGenerator.invMap.get(p.getLocation().getWorld().getName()).get("DECORATED_POT")) {
				if(item.getItem().getType().name().equals(itemName)) {
					DungeonGenerator.invMap.get(p.getLocation().getWorld().getName()).get("DECORATED_POT").remove(item);
					break;
				}
			}
			yaml1.set("potItems", DungeonGenerator.invMap.get(p.getLocation().getWorld().getName()).get("DECORATED_POT"));
		}


		if(section.equals("rare")) {
			for (ItemWRarity item : DungeonGenerator.invMap.get(p.getLocation().getWorld().getName()).get("rare")) {
				if(item.getItem().getType().name().equals(itemName)) {
					DungeonGenerator.invMap.get(p.getLocation().getWorld().getName()).get("rare").remove(item);
					break;
				}
			}
			yaml1.set("rare", DungeonGenerator.invMap.get(p.getLocation().getWorld().getName()).get("rare"));
		}

		if(section.equals("epic")) {
			for (ItemWRarity item : DungeonGenerator.invMap.get(p.getLocation().getWorld().getName()).get("epic")) {
				if(item.getItem().getType().name().equals(itemName)) {
					DungeonGenerator.invMap.get(p.getLocation().getWorld().getName()).get("epic").remove(item);
					break;
				}
			}
			yaml1.set("epic", DungeonGenerator.invMap.get(p.getLocation().getWorld().getName()).get("epic"));
		}

		if(section.equals("legendary")) {
			for (ItemWRarity item : DungeonGenerator.invMap.get(p.getLocation().getWorld().getName()).get("legendary")) {
				if(item.getItem().getType().name().equals(itemName)) {
					DungeonGenerator.invMap.get(p.getLocation().getWorld().getName()).get("legendary").remove(item);
					break;
				}
			}
			yaml1.set("legendary", DungeonGenerator.invMap.get(p.getLocation().getWorld().getName()).get("legendary"));
		}
	}
}
