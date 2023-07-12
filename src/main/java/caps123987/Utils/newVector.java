package caps123987.Utils;

import org.bukkit.util.Vector;

public class newVector {
	private int x;
	private int y;
	private int z;
	private int rot;
	public newVector(int x,int y,int z, int rot) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
		this.setRot(rot);
	}
	public newVector(Vector v,int rot) {
		this.setX(v.getBlockX());
		this.setY(v.getBlockY());
		this.setZ(v.getBlockZ());
		this.setRot(rot);
	}
	public newVector(Vector v) {
		this.setX(v.getBlockX());
		this.setY(v.getBlockY());
		this.setZ(v.getBlockZ());
		this.setRot(0);
	}
	public Vector getVector() {
		return new Vector(getX(),getY(),getZ());
	}
	public int getRot() {
		return rot;
	}
	public newVector setRot(int rot) {
		this.rot = rot;
		return this;
	}
	public int getZ() {
		return z;
	}
	public newVector setZ(int z) {
		this.z = z;
		return this;
	}
	public int getY() {
		return y;
	}
	public newVector setY(int y) {
		this.y = y;
		return this;
	}
	public int getX() {
		return x;
	}
	public newVector setX(int x) {
		this.x = x;
		return this;
	}
	@Override
	public String toString() {
		return "X: "+getX()+" Y: "+getY()+" Z: "+getZ()+" Rot: "+getRot();
	}
	
}
