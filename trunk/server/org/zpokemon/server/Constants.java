package org.zpokemon.server;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.zpokemon.PlayerEntity;
import org.zpokemon.TileMap;

/**
 * 
 * @author Troy
 *
 */
public class Constants {
	public static ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	private static ArrayList<PlayerEntity> playerList = new ArrayList<PlayerEntity>();
	
	public static TileMap tileMap;
	
    /**
     * Convert the string back to bytes.
     * 
     * @param s
     * @return byte[]
     */
	public byte[] stringToBytes(String s) {
	    byte[] b2 = new BigInteger(s, 36).toByteArray();
	    return Arrays.copyOfRange(b2, 1, b2.length);
	}
	
	/**
	 * In the future we may let the server generate a secret key which
	 * will then be passed to the client as well to encrypt and decrypt
	 * packets to keep peering eyes confused.
	 * 
	 * @return SecretKeySpec
	 */
	public SecretKeySpec generateSecretKey(){
		try{
	   	 	KeyGenerator kgen = KeyGenerator.getInstance("AES");
	        kgen.init(128);
	        SecretKey skey = kgen.generateKey();
	        byte[] raw = skey.getEncoded();
	
	        return new SecretKeySpec(raw, "AES");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	public static ArrayList<ClientThread> getClients() {
		return clients;
	}

	public static void setClients(ArrayList<ClientThread> clients) {
		Constants.clients = clients;
	}
	
	/**
	 * Remove a client and stop the thread running.
	 * 
	 * @param client
	 */
	public static void removeClient(ClientThread client){
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

	public static TileMap getTileMap() {
		return tileMap;
	}

	public static void setTileMap(TileMap tileMap) {
		Constants.tileMap = tileMap;
	}

}
