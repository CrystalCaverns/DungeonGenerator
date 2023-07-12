package caps123987.DungeonGenerator;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import caps123987.Commands.CommListener;

public class DungeonGenerator extends JavaPlugin{
	
	
	public Logger logger;
	public static DungeonGenerator instance;
	
	@Override
	public void onEnable() {
		
		/**To eddit Room you need to edit it in DunType enum
		*firts you need to add your own "section"
		*first argument is id, CAN'T repeat
		*second is material for dubuging
		*third is entrance usualy 0 0 -4
		*
		*then you put newVector class witch is youst like Vector but fourth parameter is degree
		* - set only only to 90 180 270, for examle: 4,0,0,90 means 4 block from center to right and the dungeon neeto to rotate 90 degees
		**/
		logger = super.getLogger();
		instance = this;
		getCommand("DungeonGenerator").setExecutor(new CommListener());
	}
	
	public static DungeonGenerator getInstance() {
		return instance;
	}
}
