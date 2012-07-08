package org.zpokemon.server;

/**
 * 
 * @author Troy
 * 
 * This should make it easier for people
 * to use the headers.
 *
 */
public enum PacketHeaders {
	LOGIN("0~"),
	GET_PLAYER_LIST("1~"),
	PLAYER_MOVE("2~"),
	PLAYER_DISCONNECT("3~");
	
	private final String header;
	
	private PacketHeaders(String header){
		this.header = header;;
	}

	public String getHeader() {
		return header;
	}

}