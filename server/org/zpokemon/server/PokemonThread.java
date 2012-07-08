package org.zpokemon.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 
 * @author Troy
 *
 */
public class PokemonThread extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    
    public int clientID;
    private String[] header;
    private String[] packet;

    /**
     * Start a new thread
     * 
     * @param socket
     * @param clientNumber
     */
    public PokemonThread(Socket socket, int clientID) {
        this.socket = socket;
        this.clientID = clientID;
        log("New client# " + clientID + " at " + socket);
        this.start();
    }

    /**
     * Manages the thread while its running.
     */
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            //Get messages from the client, line by line
            while(true){
                header = Encoder.decode(in.readLine()).split("~");
                if(header[1].contains(":"))
                	packet = header[1].split(":");
                else
                	packet = null;
                
                /**
                 * Check for a disconnection.
                 */
                if(header == null || header.equals(".")){
                    break;
                }
                
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
                	Constants.getPlayerList().add(new PlayerEntity((short)clientID, Double.parseDouble(packet[0]), Double.parseDouble(packet[1]),(byte)0,(byte)0));
                	sendPacket(PacketHeaders.LOGIN.getHeader() + clientID);
                	
                    /**
                     * Notify all client's and new user has joined.
                     */
                    Broadcaster.broadcast(Packets.playerUpdate((short)clientID));
                	break;
                case 1: //Get Player list
                	for(PlayerEntity p : Constants.getPlayerList())
            			sendPacket(Packets.playerUpdate(p.getId()));
                	break;
                case 2: //Player moves
                	Constants.getPlayer(Short.parseShort(packet[0])).setX(Double.parseDouble(packet[1]));
                	Constants.getPlayer(Short.parseShort(packet[0])).setY(Double.parseDouble(packet[2]));
                	Constants.getPlayer(Short.parseShort(packet[0])).setAnimation(Byte.parseByte(packet[3]));
                	
                	sendPacket(PacketHeaders.PLAYER_MOVE.getHeader() + "0"); //Return TOK.
                	
                    /**
                     * Notify all client's this player has moved.
                     */
                    Broadcaster.broadcast(Packets.playerUpdate((short)clientID));
                	break;
                }
                
                //Log a message to the server
                //log("Client packet-header: " + input[0]);
            }
        }catch(IOException e){
            log("DC client #: " + clientID + " | The user most likley closed the window.");
        }finally{
            try {
                socket.close();
            } catch (IOException e){
                log("Couldn't close a socket, what's going on?");
            }

            /**
             * Tell client's to remove player.
             */
            Broadcaster.broadcast(Packets.playerDisconnect(clientID));
            
            /**
             * Remove player.
             */
            Constants.getPlayerList().remove(Constants.getPlayer((short)clientID));
            log("Connection with client # " + clientID + " closed.");
            
            /**
             * Finally, kill of this thread and remove it.
             */
            Constants.removeClient(this);
        }
    }
    
    /**
     * Easier way to encode.
     */
    public void sendPacket(String packet){
    	if(packet != null)
	    	out.println(Encoder.encode(packet));
    }

    /**
     * Logs a simple message.  In this case we just write the
     * message to the server applications standard output.
     */
    private void log(String message) {
        Server.log(message);
    }
    
}