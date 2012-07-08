package org.zpokemon.server;

import java.util.ArrayList;

/**
 * 
 * @author Troy
 *
 */
public class Constants {
	public static ArrayList<PokemonThread> clients = new ArrayList<PokemonThread>();
	private static ArrayList<PlayerEntity> playerList = new ArrayList<PlayerEntity>();

	public static ArrayList<PokemonThread> getClients() {
		return clients;
	}

	public static void setClients(ArrayList<PokemonThread> clients) {
		Constants.clients = clients;
	}
	
	/**
	 * Remove a client and stop the thread running.
	 * 
	 * @param client
	 */
	public static void removeClient(PokemonThread client){
		while(!client.isInterrupted())
			client.interrupt();
		
		clients.remove(client);
	}

	/**
	 * Get a player by it's ID.
	 * 
	 * @param id
	 * @return PlayerEntity
	 */
	public static PlayerEntity getPlayer(short id){
		for(PlayerEntity p : playerList)
			if(p.getId() == id)
				return p;
		
		return null;
	}
	
	public static ArrayList<PlayerEntity> getPlayerList() {
		return playerList;
	}

	public static void setPlayerList(ArrayList<PlayerEntity> playerList) {
		Constants.playerList = playerList;
	}

}
