package org.zpokemon.server;

import java.awt.Color;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.zpokemon.TileMap;

/**
 * 
 * @author Troy
 * 
 * AES encryption implemented.
 * 
 * TODO
 * Make the server handle all actions like checking if a tile
 * is free etc... 
 * 
 * (I'm still not 100% on this, I kind of like the client 
 * handling it because it's less network resources and much 
 * quicker... but the down side is it can be hacked.)
 * 
 * TODO
 * Make the server handle chunks of data for clients for example
 * only pass the chunk of the map to the client as needed.
 * 
 * Currently send's the whole map but only non default tiles.
 * 
 * TODO
 * Add in a mysql database connector.
 * Use this to do database pooling and save / load information
 * store in the DB, like users for example.
 *
 */
public class Server extends JFrame{
	private static final long serialVersionUID = 1L;
	private static JTextPane log = new JTextPane();
	private JScrollPane logContainer;
	
	private static StyledDocument doc = log.getStyledDocument();
	private static Style style = log.addStyle("Server", null);
	
	static Date date;
	static SimpleDateFormat sdf=new SimpleDateFormat("dd/MMM/yyyy - HH:mm:ss");
	
	private int clientNumber;
	
	/**
	 * Construct a new server.
	 */
	public Server(){
		this.setSize(600,300);
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Server Console | Pokemon");
		
		log.setSize(600, 300);
		log.setBackground(Color.BLACK);
		log.setEditable(false);
		
		logContainer = new JScrollPane(log,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		logContainer.setSize(600, 300);
		logContainer.setBackground(Color.BLACK);
		log.setForeground(Color.WHITE);
		
		this.add(logContainer);
		this.setVisible(true);
		
		//Create a new tile map.
		Constants.setTileMap(new TileMap("test", (short)30,(short)20,(short)20,(short)20, 100, 100,(short)280, (short)160));
		
		try{
			ServerLoop();
		}catch(Exception e){
			log("Failed to start the server: " + e.getMessage());
		};
	}
	
	/**
	 * The server loop
	 */
	private void ServerLoop() throws IOException{
		ServerSocket serverSocket = null;
		boolean listening = true;
		int portNumber = 5632;
		log("The server has started on port: " + portNumber);
	     
		try {
			serverSocket = new ServerSocket(portNumber);
	    }catch (IOException e) {
	        log("Could not listen on port: " + portNumber);
	        System.exit(-1);
	    }
		
		/**
		 * Start the time manager.
		 */
		new TimeManager();

	    /**
	     * While listening for connections
	     * accept new clients.
	     */
	    while(listening){
	    	Constants.getClients().add(new ClientThread(serverSocket.accept(), clientNumber++));
	    	 
			/**
			 * Sleep
			 */
	    	try {
	    		Thread.sleep(100);
	    	}catch (InterruptedException e) {
	    		e.printStackTrace();
	    	}
	    }
	     
	     /**
	      * Time to close the server.
	      */
	     serverSocket.close();
	     log("Server shut down.");
	}
	
	/**
	 * Log a message.
	 * 
	 * @param text
	 */
	public static void log(String msg){
		try{
			if(log.getText().split("\n").length > 200){
				log.setText("Amount of lines exceeded 200, cleared and starting fresh." + "\n");
			}
			
			doc.insertString(doc.getLength(), getDate() + ": " + msg + "\n", style);
		}catch (BadLocationException e){
			e.printStackTrace();
		}
		
		//Auto scroll
		log.setCaretPosition(doc.getLength());
	}
	
	/**
	 * Log a message with different coloured text.
	 * 
	 * @param msg
	 * @param lineColour
	 */
	public static void log(String msg, Color lineColour){
		try{
			if(log.getText().split("\n").length > 200){
				log.setText("Amount of lines exceeded 200, cleared and starting fresh." + "\n");
			}
			
			doc.insertString(doc.getLength(), getDate() + ": ", style);
			StyleConstants.setForeground(style, lineColour);
			doc.insertString(doc.getLength(), msg + "\n", style);
			StyleConstants.setForeground(style, Color.WHITE);
		}catch (BadLocationException e){
			e.printStackTrace();
		}
		
		//Auto scroll
		log.setCaretPosition(doc.getLength());
	}
	
	/**
	 * Get the date.
	 * 
	 * @return String
	 */
	public static String getDate(){
		date = new Date();
		return sdf.format(date);
	}
	
	/**
	 * Start the server
	 */
	public static void main(String[] args){
		new Server();
	}
	
}