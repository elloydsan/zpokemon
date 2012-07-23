package org.zengine.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.crypto.spec.SecretKeySpec;

import org.pokemon.GameConstants;
import org.pokemon.GameStates;
import org.pokemon.Pokemon;
import org.pokemon.TileMap;
import org.pokemon.entities.OtherPlayerEntity;
import org.zengine.uils.ImageUtils;

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
 * AES encryption implemented.
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
    private boolean loggedin;
    private AESEncoder encoder;
    private SecretKeySpec key;
	
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
		this.loggedin = false;
		
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
	        sendRawPacket(PacketHeaders.LOGIN.getHeader() + 
	        		GameConstants.getPlayer().getX() + ":" + 
	        		GameConstants.getPlayer().getY());

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
				/**
				 * If we are logged in the packet's will be encrypted.
				 * This will decrypt the packet before we process it.
				 */
				if(loggedin)
					fromServer = encoder.decrypt(fromServer);
				else
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
			        	/**
			        	 * This will set the user's ID, key 
			        	 * and also set the login flag to true.
			        	 * 
			        	 * Without this key the client can't interact
			        	 * with the server. The server will assume the
			        	 * client is sending junk / trying to cheat and 
			        	 * disconnect the socket.
			        	 */
				        GameConstants.getPlayer().setId(Short.parseShort(replyPacket[0]));
				        this.key = new SecretKeySpec(GameConstants.stringToBytes(replyPacket[1]),"AES");
				        this.encoder = new AESEncoder(key);
				        this.loggedin = true;
				        
				        //Download map.
				        sendPacket(PacketHeaders.MAP_CREATE.getHeader() + ":");
				        
				        //Fill map.
				        sendPacket(PacketHeaders.MAP_CHUNK.getHeader() + ":");
				        
				        //Get server time.
				        sendPacket(PacketHeaders.SERVER_TIME.getHeader() + ":");
				        
				        //Get player list
				        sendPacket(PacketHeaders.PLAYER_LIST.getHeader() + ":");
				        
				        //Start the game.
				        Pokemon.state = GameStates.PLAYING;
			        	break;
			        case 1: //Server Time
			        	System.out.println("Server sunmoon: " + replyPacket[10]);
			        	
			        	GameConstants.getFulldayTimer().timeLimit = Long.parseLong(replyPacket[0]);
			        	GameConstants.getFulldayTimer().endTime = Long.parseLong(replyPacket[1]);
			        	GameConstants.getFullnightTimer().timeLimit = Long.parseLong(replyPacket[2]);
			        	GameConstants.getFullnightTimer().endTime = Long.parseLong(replyPacket[3]);
			        	GameConstants.getTransitionTimer().timeLimit = Long.parseLong(replyPacket[4]);
			        	GameConstants.getTransitionTimer().endTime = Long.parseLong(replyPacket[5]);
			        	GameConstants.getChangeTransitionTimer().timeLimit = Long.parseLong(replyPacket[6]);
			        	GameConstants.getChangeTransitionTimer().endTime = Long.parseLong(replyPacket[7]);
			        	GameConstants.setTransition(Float.parseFloat(replyPacket[8]));
			        	Pokemon.filterEffect = ImageUtils.changeTranslucentImage(Pokemon.filter, GameConstants.getTransition());
			        	GameConstants.setNight(Boolean.parseBoolean(replyPacket[9]));
			        	GameConstants.getMinimap().setSunmoonY(GameConstants.getMinimap().getSunmoonY() + Double.parseDouble(replyPacket[10]));
			        	break;
			        case 2: //Create map
			        	GameConstants.setTilemap(new TileMap(replyPacket[0],
			        			Short.parseShort(replyPacket[1]),
			        			Short.parseShort(replyPacket[2]),
			        			Short.parseShort(replyPacket[3]),
			        			Short.parseShort(replyPacket[4]),
			        			Double.parseDouble(replyPacket[5]),
			        			Double.parseDouble(replyPacket[6])));
			        	
			        	GameConstants.getPlayer().setX(Double.parseDouble(replyPacket[7]));
			        	GameConstants.getPlayer().setY(Double.parseDouble(replyPacket[8]));
			        	break;
			        case 3: //Download map chunks / fill map data.
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
			        case 4: //Get player list
			        	if(Short.parseShort(replyPacket[0]) != GameConstants.getPlayer().getId()){
			        		if(GameConstants.getPlayer(Short.parseShort(replyPacket[0])) != null){ //If we found a client update them.
			        			GameConstants.getPlayer(Short.parseShort(replyPacket[0])).setX(Double.parseDouble(replyPacket[1]));
			        			GameConstants.getPlayer(Short.parseShort(replyPacket[0])).setY(Double.parseDouble(replyPacket[2]));
			        			GameConstants.getPlayer(Short.parseShort(replyPacket[0])).setAnimation(Byte.parseByte(replyPacket[3]));
			        			GameConstants.getPlayer(Short.parseShort(replyPacket[0])).setInGrass(Boolean.valueOf(replyPacket[4]));
			        			//System.out.println("Updating player: " + replyPacket[0] + " XY: " + replyPacket[1] + "," + replyPacket[2] + "," + replyPacket[3]);
			        		}else{ //Else add a new user in.
			        			GameConstants.getPlayerList().add(new OtherPlayerEntity(Short.parseShort(replyPacket[0]),
				        				Double.parseDouble(replyPacket[1]), 
					        			Double.parseDouble(replyPacket[2]), 
					        			(short)35, 
					        			(short)35, 
					        			(byte)0, 
					        			Byte.parseByte(replyPacket[3]), 
					        			(short)0));
			        			
			        			GameConstants.getPlayer(Short.parseShort(replyPacket[0])).setInGrass(Boolean.valueOf(replyPacket[4]));
					        	//System.out.println("Trying to add players. ID: " + replyPacket[0] + " XY: " + replyPacket[1] + "," + replyPacket[2]);
			        		}
			        	}
			        	break;
			        case 5: //Move player
			        	break;
			        case 6: //Disconnect player
			        	GameConstants.getPlayerList().remove(GameConstants.getPlayer(Short.parseShort(header[1])));
			        	break;
			      
			        case 7: //Receive message's from all clients.
			        	if((short)Integer.parseInt(replyPacket[0]) != GameConstants.getPlayer().getId() && replyPacket.length > 1)
			        		GameConstants.getChat().setChatlog(GameConstants.getChat().getChatlog() + 
			        				replyPacket[0] + " - " + 
			        				replyPacket[1] + "~");
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
	public void sendRawPacket(String packet){
		if(packet != null){
			out.println(Encoder.encode(packet));
			out.flush();
		}
	}
	
	/**
	 * Send a packet to the server.
	 * 
	 * @param packet
	 */
	public void sendPacket(String packet){
		if(packet != null){
			out.println(encoder.encrypt(packet));
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
				GameConstants.getPlayer().getAnimation() + ":" + 
				GameConstants.getPlayer().isInGrass());
	}
	
}