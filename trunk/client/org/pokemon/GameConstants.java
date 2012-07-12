package org.pokemon;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.zengine.networking.PacketManager;

/**
 * 
 * @author Troy
 *
 */
public class GameConstants {
	private static TileMap tilemap;
	private static PlayerEntity player;
	private static BufferedImage[] playerImages;
	
	// Chat
	private static Chatbox chat;
	
	//Networking
	private static boolean multiplayer = true;
	private static PacketManager packetManager;
	private static ArrayList<OtherPlayerEntity> playerList = new ArrayList<OtherPlayerEntity>();

	public static TileMap getTilemap() {
		return tilemap;
	}

	public static void setTilemap(TileMap tilemap) {
		GameConstants.tilemap = tilemap;
	}
	
	public static PlayerEntity getPlayer() {
		return player;
	}

	public static void setPlayer(PlayerEntity player) {
		GameConstants.player = player;
	}

	public static BufferedImage[] getPlayerImages() {
		return playerImages;
	}

	public static void setPlayerImages(BufferedImage[] playerImages) {
		GameConstants.playerImages = playerImages;
	}
	
	public static Chatbox getChat() {
		return chat;
	}
	
	public static void setChat(Chatbox chat) {
		GameConstants.chat = chat;
	}

	/**
	 * Check if a tile is walkable.
	 * 
	 * @param x
	 * @param y
	 * @return boolean
	 */
	public static boolean isTileFree(int x, int y){
		if(x < 0 || x >= tilemap.getTileCols()) return false;
		if(y < 0 || y >= tilemap.getTileRows()) return false;
		
		if(tilemap.getLayer2()[x][y].getImage() == 15)
			player.setInGrass(true);
		else
			player.setInGrass(false);
		
		
		return (tilemap.getLayer1()[x][y].getState() == 0) && 
				(tilemap.getLayer2()[x][y].getState() == 0) && 
				(tilemap.getLayer3()[x][y].getState() == 0);
	}

	public static boolean isMultiplayer() {
		return multiplayer;
	}

	public static void setMultiplayer(boolean multiplayer) {
		GameConstants.multiplayer = multiplayer;
	}

	public static PacketManager getPacketManager() {
		return packetManager;
	}

	public static void setPacketManager(PacketManager packetManager) {
		GameConstants.packetManager = packetManager;
	}
	
	/**
	 * Get a player by it's ID.
	 * 
	 * @param id
	 * @return PlayerEntity
	 */
	public static OtherPlayerEntity getPlayer(short id){
		for(OtherPlayerEntity p : playerList)
			if(p.getId() == id)
				return p;
		
		return null;
	}

	public static ArrayList<OtherPlayerEntity> getPlayerList() {
		return playerList;
	}

	public static void setPlayerList(ArrayList<OtherPlayerEntity> playerList) {
		GameConstants.playerList = playerList;
	}

}
