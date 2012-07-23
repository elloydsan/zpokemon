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
	 * This will generate a server time packet.
	 */
	public static String serverTime(){
		return PacketHeaders.SERVER_TIME.getHeader() +
				Constants.getFulldayTimer().timeLimit + ":" + 
				Constants.getFulldayTimer().endTime + ":" +
				Constants.getFullnightTimer().timeLimit + ":" + 
				Constants.getFullnightTimer().endTime + ":" +
				Constants.getTransitionTimer().timeLimit + ":" + 
				Constants.getTransitionTimer().endTime + ":" +
				Constants.getChangeTransitionTimer().timeLimit + ":" + 
				Constants.getChangeTransitionTimer().endTime + ":" +
				Constants.getTransition() + ":" +
				Constants.isNight() + ":" +
				Constants.getSunmoonY();
	}
	
	/**
	 * This will generate a map create packet.
	 * 
	 * @return String
	 */
	public static String mapCreate(){
		return PacketHeaders.MAP_CREATE.getHeader() +
				Constants.getTileMap().getName() + ":" +
				Constants.getTileMap().getTileCols() + ":" +
				Constants.getTileMap().getTileRows() + ":" +
				Constants.getTileMap().getTileWidth() + ":" +
				Constants.getTileMap().getTileHeight() + ":" +
				Constants.getTileMap().getxOffSet() + ":" +
				Constants.getTileMap().getyOffSet() + ":" + 
				Constants.getTileMap().getStartX() + ":" + 
				Constants.getTileMap().getStartY();
	}
	
	/**
	 * This will generate a map chunk packet.
	 * 
	 * @return String
	 */
	public static String mapChunkLayer1(){
		String packet = PacketHeaders.MAP_CHUNK.getHeader() + "1~";
		
		for(int a = 0; a < Constants.getTileMap().getTileRows(); a++){
			for(int b = 0; b < Constants.getTileMap().getTileCols(); b++){
				if(Constants.getTileMap().getLayer1()[b][a].getImage() != 0){
					packet += b + "," + 
							a + "," + 
							Constants.getTileMap().getLayer1()[b][a].getState() + "," +
							Constants.getTileMap().getLayer1()[b][a].getInteractive() + "," + 
							Constants.getTileMap().getLayer1()[b][a].getImage() + ":";
				}
			}
		}
		
		return packet;		
	}
	
	/**
	 * This will generate a map chunk packet.
	 * 
	 * @return String
	 */
	public static String mapChunkLayer2(){
		String packet = PacketHeaders.MAP_CHUNK.getHeader() + "2~";
		
		for(int a = 0; a < Constants.getTileMap().getTileRows(); a++){
			for(int b = 0; b < Constants.getTileMap().getTileCols(); b++){
				if(Constants.getTileMap().getLayer2()[b][a].getImage() != 0){
					packet += b + "," + 
							a + "," + 
							Constants.getTileMap().getLayer2()[b][a].getState() + "," +
							Constants.getTileMap().getLayer2()[b][a].getInteractive() + "," + 
							Constants.getTileMap().getLayer2()[b][a].getImage() + ":";
				}
			}
		}
		
		return packet;		
	}
	
	/**
	 * This will generate a map chunk packet.
	 * 
	 * @return String
	 */
	public static String mapChunkLayer3(){
		String packet = PacketHeaders.MAP_CHUNK.getHeader() + "3~";
		
		for(int a = 0; a < Constants.getTileMap().getTileRows(); a++){
			for(int b = 0; b < Constants.getTileMap().getTileCols(); b++){
				if(Constants.getTileMap().getLayer3()[b][a].getImage() != 0){
					packet += b + "," + 
							a + "," + 
							Constants.getTileMap().getLayer3()[b][a].getState() + "," +
							Constants.getTileMap().getLayer3()[b][a].getInteractive() + "," + 
							Constants.getTileMap().getLayer3()[b][a].getImage() + ":";
				}
			}
		}
		
		return packet;		
	}
	
	/**
	 * This will generate a player update packet.
	 * 
	 * @param id
	 * @return String
	 */
	public static String playerUpdate(short id){
		return PacketHeaders.PLAYER_LIST.getHeader() + 
				Constants.getPlayer(id).getId() + ":" + 
        		Constants.getPlayer(id).getX() + ":" + 
        		Constants.getPlayer(id).getY() + ":" + 
        		Constants.getPlayer(id).getAnimation() + ":" +
        		Constants.getPlayer(id).isInGrass();
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