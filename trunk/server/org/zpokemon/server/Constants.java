package org.zpokemon.server;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.zpokemon.PlayerEntity;
import org.zpokemon.TileMap;
import org.zpokemon.server.utils.Timer;

/**
 * 
 * @author Troy
 *
 */
public class Constants {
	public static ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	private static ArrayList<PlayerEntity> playerList = new ArrayList<PlayerEntity>();
	
	public static TileMap tileMap;
	
	//Time variables
	private static Timer fulldayTimer = new Timer(2400000); //Currently set at 		40 minutes.
	private static Timer fullnightTimer = new Timer(300000); //Currently set at 	10 minutes.
	private static Timer transitionTimer = new Timer(300000); //Currently set at 	5 minutes.
	private static Timer changeTransitionTimer = new Timer(100); //Currently at 	5 seconds.
	
	private static float transition;
	private static double sunmoonY;
	private static boolean night;
	
	/**
	 * This will convert the bytes to a string.
	 * 
	 * @param b
	 * @return String
	 */
	public static String bytesToString(byte[] b) {
	    byte[] b2 = new byte[b.length + 1];
	    b2[0] = 1;
	    System.arraycopy(b, 0, b2, 1, b.length);
	    return new BigInteger(b2).toString(36);
	}
	
    /**
     * Convert the string back to bytes.
     * 
     * @param s
     * @return byte[]
     */
	public static byte[] stringToBytes(String s) {
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
	public static SecretKeySpec generateSecretKey(){
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

	public static Timer getFulldayTimer() {
		return fulldayTimer;
	}

	public static Timer getFullnightTimer() {
		return fullnightTimer;
	}

	public static Timer getTransitionTimer() {
		return transitionTimer;
	}

	public static Timer getChangeTransitionTimer() {
		return changeTransitionTimer;
	}

	public static float getTransition() {
		return transition;
	}

	public static boolean isNight() {
		return night;
	}

	public static void setFulldayTimer(Timer fulldayTimer) {
		Constants.fulldayTimer = fulldayTimer;
	}

	public static void setFullnightTimer(Timer fullnightTimer) {
		Constants.fullnightTimer = fullnightTimer;
	}

	public static void setTransitionTimer(Timer transitionTimer) {
		Constants.transitionTimer = transitionTimer;
	}

	public static void setChangeTransitionTimer(Timer changeTransitionTimer) {
		Constants.changeTransitionTimer = changeTransitionTimer;
	}

	public static void setTransition(float transition) {
		Constants.transition = transition;
	}

	public static void setNight(boolean night) {
		Constants.night = night;
	}

	public static double getSunmoonY() {
		return sunmoonY;
	}

	public static void setSunmoonY(double sunmoonY) {
		Constants.sunmoonY = sunmoonY;
	}

}
