package org.zpokemon.server;

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
	 */
	public static void broadcast(String packet){
		for(PokemonThread pt : Constants.getClients()){
			pt.sendPacket(packet);
		}
	}
	
	/**
	 * Send the player list to all clients.
	 */
	public static void playerList(){
		for(PlayerEntity p : Constants.getPlayerList())
			broadcast("1~" + p.getId() + ":" + p.getX() + ":" + p.getY() + ":" + p.getAnimation());
	}

}
