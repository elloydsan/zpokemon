package org.zpokemon;


/**
 * 
 * @author NerdyGnome, Fsig
 * 
 * The Npc class, all Npc details to be stored
 *
 */
public class NpcEntity extends Entity {
	private short zoneSize;
	private boolean isChatting;

	/**
	 * Construct a new Npc Entity
	 * @param id
	 * @param x
	 * @param y
	 * @param direction
	 * @param zoneSize
	 */
	public NpcEntity(short id, double x, double y, short width, short height, byte direction, byte animation, short zoneSize) {	
		super(x,y,direction,animation);
		this.zoneSize = zoneSize;		
	}
	
	public short getZoneSize() {
		return zoneSize;
	}
	
	public void setZoneSize(short zoneSize) {
		this.zoneSize = zoneSize;
	}
	
	public boolean isChatting() {
		return isChatting;
	}
	
	public void setChatting(boolean isChatting) {
		this.isChatting = isChatting;
	}
	
}