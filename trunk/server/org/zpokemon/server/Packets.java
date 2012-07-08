package org.zpokemon.server;

/**
 * 
 * @author Troy
 * 
 * This class will generate packet's to send.
 *
 */
public class Packets {
	
	/**
	 * This will generate a player update packet.
	 * 
	 * @param id
	 * @return String
	 */
	public static String playerUpdate(short id){
		return PacketHeaders.GET_PLAYER_LIST.getHeader() + 
				Constants.getPlayer(id).getId() + ":" + 
        		Constants.getPlayer(id).getX() + ":" + 
        		Constants.getPlayer(id).getY() + ":" + 
        		Constants.getPlayer(id).getAnimation(); 
	}
	
	/**
	 * This will return a player disconnect packer.
	 * 
	 * @param id
	 * @return String
	 */
	public static String playerDisconnect(int id){
		return PacketHeaders.PLAYER_DISCONNECT.getHeader() + id;
	}

}