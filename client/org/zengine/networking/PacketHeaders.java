package org.zengine.networking;

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
	PLAYER_LIST("1~"),
	PLAYER_MOVE("2~"),
	PLAYER_DISCONNECT("3~"),
	MAP_CREATE("4~"),
	MAP_CHUNK("5~");
	
	private final String header;
	
	private PacketHeaders(String header){
		this.header = header;;
	}

	public String getHeader() {
		return header;
	}

}