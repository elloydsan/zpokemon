package org.zpokemon.server;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import javax.crypto.spec.SecretKeySpec;

import org.zpokemon.PlayerEntity;

/**
 * 
 * @author Troy
 *
 */
public class ClientThread extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private AESEncoder encoder;
    private SecretKeySpec key;
    
    public int clientID;
    private String fromClient;
    private String[] header;
    private String[] packet;
    
    public boolean loggedin;

    /**
     * Start a new thread
     * 
     * @param socket
     * @param clientNumber
     */
    public ClientThread(Socket socket, int clientID) {
        this.socket = socket;
        try {
        	this.socket.setTcpNoDelay(true);
		} catch (SocketException e) {
			e.printStackTrace();
		}
        this.clientID = clientID;
        this.key = Constants.generateSecretKey();
        this.encoder = new AESEncoder(key);
        log("New client " + clientID + ": " + socket, Color.GREEN);
        this.loggedin = false;
        this.start();
    }

    /**
     * Manages the thread while its running.
     */
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            while((fromClient = in.readLine()) != null){           	
            	if(loggedin)
            		header = encoder.decrypt(fromClient).split("~");
            	else
            		header = Encoder.decode(fromClient).split("~");

                if(header.length > 1){
                	if(header[1].contains(":"))
                		packet = header[1].split(":");
                	else
                		packet = null;
	                
	                /**
	                 * Switch through packet headers.
	                 * 
	                 * Client->Server
	                 * 
	                 * We must response with the same header
	                 * packet so the client know's how to process
	                 * the response.
	                 */
	                switch(Integer.parseInt(header[0])){
	                case 0: //Login
	                	/**
	                	 * In the login packet we need to include the user's start X and start Y
	                	 * location for this map.
	                	 */
	                	Constants.getPlayerList().add(new PlayerEntity((short)clientID, Double.parseDouble(packet[0]), Double.parseDouble(packet[1]),(byte)0,(byte)0));
	                	sendRawPacket(PacketHeaders.LOGIN.getHeader() + clientID + ":" + Constants.bytesToString(this.key.getEncoded()));
	                	this.loggedin = true;
	                	
	                    /**
	                     * Notify all client's and new user has joined.
	                     */
	                    Broadcaster.broadcastExcept(Packets.playerUpdate((short)clientID),clientID);
	                	break;
	                case 1: //Server time
	                	/**
	                	 * We pass the current time settings to the
	                	 * client and then the client manages the
	                	 * time from then after.
	                	 */
	                	sendPacket(Packets.serverTime());
	                	break;
	                case 2: //Create map.
	                	sendPacket(Packets.mapCreate());
	                	break;
	                case 3: //Download map chunks.
	                	sendPacket(Packets.mapChunkLayer1());
	                	sendPacket(Packets.mapChunkLayer2());
	                	sendPacket(Packets.mapChunkLayer3());
	                	break;
	                case 4: //Get Player list
	                	for(PlayerEntity p : Constants.getPlayerList())
	            			sendPacket(Packets.playerUpdate(p.getId()));
	                	break;
	                case 5: //Player moves
	                	/**
	                	 * Return TOK if the tile they are trying to move to is 
	                	 * free otherwise return 1 which is false.
	                	 * 
	                	 * The server will handle tile checking in the future. 
	                	 * (Maybe, or the client don't know yet.)
	                	 */
	                	//sendPacket(PacketHeaders.PLAYER_MOVE.getHeader() + "0");
	                	
	                	Constants.getPlayer(Short.parseShort(packet[0])).setX(Double.parseDouble(packet[1]));
	                	Constants.getPlayer(Short.parseShort(packet[0])).setY(Double.parseDouble(packet[2]));
	                	Constants.getPlayer(Short.parseShort(packet[0])).setAnimation(Byte.parseByte(packet[3]));
	                	Constants.getPlayer(Short.parseShort(packet[0])).setInGrass(Boolean.parseBoolean(packet[4]));
	                	
	                    /**
	                     * Notify all client's this player has moved.
	                     */
	                    Broadcaster.broadcastExcept(Packets.playerUpdate((short)clientID),clientID);
	                	break;
	                case 6: //Player disconnect.
	                	// TODO safe disconnect a user.
	                	break;
	                case 7: //Chat
	                	if(packet.length > 1)
	                	Broadcaster.broadcastExcept(PacketHeaders.PLAYER_SEND_MESSAGE.getHeader() + 
	                			packet[0] + ":" + 
	                			packet[1],
	                			clientID);
	                	break;
	                default: //Default should NEVER be reached.
	                	//Best to assume this player is cheating and DC them I guess?
	                	break;
	                }
                }
            }
        }catch(IOException e){
            //log("DC client #: " + clientID + " | The user most likley closed the window.");
        }finally{
            try {
                socket.close();
            } catch (IOException e){
                log("Couldn't close a socket, what's going on?");
            }

            /**
             * Tell client's to remove player.
             */
            Broadcaster.broadcastExcept(Packets.playerDisconnect(clientID),clientID);
            
            /**
             * Remove player.
             */
            Constants.getPlayerList().remove(Constants.getPlayer((short)clientID));
            log("Connection with client # " + clientID + " closed.", Color.RED);
            
            /**
             * Finally, kill of this thread and remove it.
             */
            Constants.removeClient(this);
        }
    }
    
    /**
     * Send a raw packet to the client.
     */
    public void sendRawPacket(String packet){
    	if(packet != null){
	    	out.println(Encoder.encode(packet));
	    	out.flush();
    	}
    }
    
    /**
     * Easier way to encode.
     */
    public void sendPacket(String packet){
    	if(packet != null){
	    	out.println(this.encoder.encrypt(packet));
	    	out.flush();
    	}
    }

    /**
     * Logs a simple message.  In this case we just write the
     * message to the server applications standard output.
     */
    private void log(String message) {
        Server.log(message);
    }
    
    /**
     * Log a message to the server with a coloured line.
     * 
     * @param message
     * @param color
     */
    private void log(String message, Color color) {
        Server.log(message, color);
    }
    
}