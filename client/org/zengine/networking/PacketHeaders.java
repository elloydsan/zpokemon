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
	SERVER_TIME("1~"),
	MAP_CREATE("2~"),
	MAP_CHUNK("3~"),
	PLAYER_LIST("4~"),
	PLAYER_MOVE("5~"),
	PLAYER_DISCONNECT("6~"),
	PLAYER_SEND_MESSAGE("7~");
	
	private final String header;
	
	private PacketHeaders(String header){
		this.header = header;;
	}

	public String getHeader() {
		return header;
	}

}