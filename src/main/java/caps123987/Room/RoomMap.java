package caps123987.Room;

import java.util.HashMap;
import java.util.Map;

import caps123987.Types.DunType;
import caps123987.Utils.newVector;

public class RoomMap {
	private DunType type;
	
	private Map<newVector, RoomMap> map = new HashMap<newVector, RoomMap>();
	
	
	
	public RoomMap(DunType type, RoomMap... roomM) {
		this.type = type;
		
		int count =0;
		for(newVector v:type.getEntrances()) {
			if(roomM.length>count) {
				map.put(v, roomM[count]);
			}else {
				break;
			}
		}
		
	}
	
	public RoomMap(DunType type) {
		this.type = type;
	}
}
