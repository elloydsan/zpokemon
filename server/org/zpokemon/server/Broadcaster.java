package org.zpokemon.server;

import org.zpokemon.PlayerEntity;

/**
 * 
 * @author Troy
 * 
 * This class will send packets to all clients.
 *
 */
public class Broadcaster {
	
	/**
	 * Send a packet to all clients.
	 * 
	 * @param packet
	 */
	public static void broadcast(String packet){
		for(ClientThread pt : Constants.getClients())
			pt.sendPacket(packet);
	}
	
	/**
	 * Send a packet to all clients except
	 * the specified ID.
	 * 
	 * @param packet
	 * @param id
	 */
	public static void broadcastExcept(String packet, int id){
		for(ClientThread pt : Constants.getClients())
			if(pt.clientID != id)
				pt.sendPacket(packet);
	}
	
	/**
	 * Send the player list to all clients.
	 */
	public static void playerList(){
		for(PlayerEntity p : Constants.getPlayerList())
			broadcast(Packets.playerUpdate(p.getId()));
	}

}