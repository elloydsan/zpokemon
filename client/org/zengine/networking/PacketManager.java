package org.zengine.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.pokemon.GameConstants;
import org.pokemon.OtherPlayerEntity;
import org.pokemon.Pokemon;
import org.pokemon.TileMap;

/**
 * 
 * @author Troy
 * 
 * This Packet Manager uses the Socket to
 * send and receive packets over TCP.
 * 
 * This method is slower but seem's more logical
 * because we do not need to be as accurate as a FPS
 * game.
 * 
 * Packet structure example:
 * header:userid:information that goes with header
 * 
 * If you have a packet that is sent over and over
 * it may be best to create a method in here to send
 * the packet for you.
 *
 */
public class PacketManager {
	private Thread listener;
	
	private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private String fromServer;
    private String[] header;
    private String[] replyPacket;
	
	private int port;
	private InetAddress ipAddress;
	
	/**
	 * This will create a new PacketManager
	 * which will listen to all incoming packets
	 * on the specified IP address and port.
	 * 
	 * @param ipAddress
	 */
	public PacketManager(String ipAddress, int port){
		this.port = port;
		
		try {
			this.ipAddress = InetAddress.getByName(ipAddress);
			socket = new Socket(this.ipAddress, this.port);
			
			 try{
				 this.socket.setTcpNoDelay(true);
			 }catch (SocketException e) {
				 e.printStackTrace();
			 }
	        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        out = new PrintWriter(socket.getOutputStream(), true);
	        
	        //Login packet.
	        sendPacket(PacketHeaders.LOGIN.getHeader() + 
	        		GameConstants.getPlayer().getX() + ":" + 
	        		GameConstants.getPlayer().getY());
	        
	        //Download map.
	        sendPacket(PacketHeaders.MAP_CREATE.getHeader() + ":");
	        
	        //Fill map.
	        sendPacket(PacketHeaders.MAP_CHUNK.getHeader() + ":");
	        
	        //Get player list
	        sendPacket(PacketHeaders.PLAYER_LIST.getHeader() + ":");
	        
	        //Start a new listener.
	        listener = new Thread(){
	        	@Override
				public void run(){
					listen();
				}
	        };
	        listener.start();
		}catch(UnknownHostException e1) {
			e1.printStackTrace();
			return;
		}catch(IOException e) {
			System.out.println("Failed to connect to the server.");
			System.exit(-1);
			return;
		}

	}
	
	/**
	 * Listen for all incoming packets.
	 * 
	 * This method will keep on listening for all packet's and
	 * will not move onto the next packet until the current
	 * packet is dealt with.
	 * 
	 * With this we should be able to accomplish broadcasting from
	 * the server to all client's player updates, movement updates
	 * and so forth.
	 * 
	 * These methods are still in test stages.
	 * 
	 */
	private void listen(){
		try{
			while((fromServer = in.readLine()) != null){
				fromServer = Encoder.decode(fromServer);
				
				if(fromServer != null && fromServer.contains("~")){
					header = fromServer.split("~");
					if(header[1].contains(":"))
						replyPacket = header[1].split(":");
					
					 /**
			         * Switch through packet headers.
			         * 
			         * Server->Client
			         */
			        switch(Integer.parseInt(header[0])){
			        case 0: //Login
				        GameConstants.getPlayer().setId(Short.parseShort(header[1]));
			        	break;
			        case 1: //Get player list
			        	if(Short.parseShort(replyPacket[0]) != GameConstants.getPlayer().getId()){
			        		//Try and find the user.
			        		OtherPlayerEntity found = GameConstants.getPlayer(Short.parseShort(replyPacket[0]));
			        			
			        		if(found != null){ //If we found a client update them.
			        			GameConstants.getPlayer(Short.parseShort(replyPacket[0])).setX(Double.parseDouble(replyPacket[1]));
			        			GameConstants.getPlayer(Short.parseShort(replyPacket[0])).setY(Double.parseDouble(replyPacket[2]));
			        			GameConstants.getPlayer(Short.parseShort(replyPacket[0])).setAnimation(Byte.parseByte(replyPacket[3]));
			        			//System.out.println("Updating player: " + replyPacket[0] + " XY: " + replyPacket[1] + "," + replyPacket[2] + "," + replyPacket[3]);
			        		}else{ //Else add a new user in.
			        			GameConstants.getPlayerList().add(new OtherPlayerEntity(Short.parseShort(replyPacket[0]),
				        				Double.parseDouble(replyPacket[1]), 
					        			Double.parseDouble(replyPacket[2]), 
					        			(short)35, 
					        			(short)35, 
					        			(byte)0, 
					        			Byte.parseByte(replyPacket[3]), 
					        			(short)0, 
					        			(short)0));
					        	//System.out.println("Trying to add players. ID: " + replyPacket[0] + " XY: " + replyPacket[1] + "," + replyPacket[2]);
			        		}
			        	}
			        	break;
			        case 2: //Move player
			        	break;
			        case 3: //Disconnect player
			        	GameConstants.getPlayerList().remove(GameConstants.getPlayer(Short.parseShort(header[1])));
			        	break;
			        case 4: //Create map
			        	GameConstants.setTilemap(new TileMap(Short.parseShort(replyPacket[0]),
			        			Short.parseShort(replyPacket[1]),
			        			Short.parseShort(replyPacket[2]),
			        			Short.parseShort(replyPacket[3]),
			        			Pokemon.tileTextures));
			        	
			        	GameConstants.getTilemap().setxOffSet(Double.parseDouble(replyPacket[4]));
			        	GameConstants.getTilemap().setyOffSet(Double.parseDouble(replyPacket[5]));
			        	GameConstants.getPlayer().setX(Double.parseDouble(replyPacket[6]));
			        	GameConstants.getPlayer().setY(Double.parseDouble(replyPacket[7]));
			        	break;
			        case 5: //Download map chunks / fill map data.
			        	if(header.length > 2){
			        		replyPacket = header[2].split(":");
			        	
				        	for(int i = 0; i < replyPacket.length; i++){
				        		switch(Integer.parseInt(header[1])){
				        		case 1:
				        			if(replyPacket[i].contains(",")){
						        		String[] split = replyPacket[i].split(",");
						        		
						        		int x = Integer.parseInt(split[0]);
						        		int y = Integer.parseInt(split[1]);
						        		
						        		GameConstants.getTilemap().getLayer1()[x][y].setState(Byte.parseByte(split[2]));
						        		GameConstants.getTilemap().getLayer1()[x][y].setInteractive(Byte.parseByte(split[3]));
						        		GameConstants.getTilemap().getLayer1()[x][y].setImage(Short.parseShort(split[4]));
					        		}
				        			break;
				        		case 2:
				        			if(replyPacket[i].contains(",")){
						        		String[] split = replyPacket[i].split(",");
						        		
						        		int x = Integer.parseInt(split[0]);
						        		int y = Integer.parseInt(split[1]);
						        		
						        		GameConstants.getTilemap().getLayer2()[x][y].setState(Byte.parseByte(split[2]));
						        		GameConstants.getTilemap().getLayer2()[x][y].setInteractive(Byte.parseByte(split[3]));
						        		GameConstants.getTilemap().getLayer2()[x][y].setImage(Short.parseShort(split[4]));
					        		}
				        			break;
				        		case 3:
				        			if(replyPacket[i].contains(",")){
						        		String[] split = replyPacket[i].split(",");
						        		
						        		int x = Integer.parseInt(split[0]);
						        		int y = Integer.parseInt(split[1]);
						        		
						        		GameConstants.getTilemap().getLayer3()[x][y].setState(Byte.parseByte(split[2]));
						        		GameConstants.getTilemap().getLayer3()[x][y].setInteractive(Byte.parseByte(split[3]));
						        		GameConstants.getTilemap().getLayer3()[x][y].setImage(Short.parseShort(split[4]));
					        		}
				        			break;
				        		}
				        	}
			        	}
			        	break;
			        default:
			        	break;
			        }
				}
				
		        /**
		         * Have a small sleep to let the CPU recover.
		         */
				Thread.sleep(10);
			}
		}catch(Exception e){
			e.printStackTrace();
			
			while(!listener.isInterrupted())
				listener.interrupt();
			
			disconnect();
		}
	}
	
	/**
	 * Close the connection.
	 */
	public void disconnect(){
		try{
			out.close();
			in.close();
			socket.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Send a packet to the server.
	 * 
	 * @param packet
	 */
	public void sendPacket(String packet){
		//System.out.println("Packet Size: " + packet.length());
		if(packet != null){
			out.println(Encoder.encode(packet));
			out.flush();
		}
	}
	
	/**
	 * Update our player's position.
	 */
	public void movePlayer(){
		sendPacket(PacketHeaders.PLAYER_MOVE.getHeader() + 
				GameConstants.getPlayer().getId() + ":" + 
				GameConstants.getPlayer().getX() + ":" + 
				GameConstants.getPlayer().getY() + ":" +
				GameConstants.getPlayer().getAnimation());
	}
	
}