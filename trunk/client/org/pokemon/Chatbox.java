package org.pokemon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.zengine.Constants;
import org.zengine.graphics.Paintable;
import org.zengine.networking.PacketHeaders;
import org.zengine.uils.ImageUtils;
import org.zengine.uils.StringUtils;

/**
 * This class implements a chat box that will be used for chatting amongst players.
 * Functionality will be added to allow developers to test certain commands. 
 * For example sending packets, initiating Pokemon fights etc...
 * 
 * @author NerdyGnome, Troy
 * 
 * TODO
 * Add commands.
 *
 */
public class Chatbox implements Paintable{
	private static final ArrayList<Integer> disallowedCharacters = new ArrayList<Integer>();
	
	private static final byte CHAT_LOG_LINES = 8;
	private static final byte CHAT_MENU_TOP = 5;
	
	private byte chatSelectedIndex = 0;
	private byte MAXIUM_CHARACTERS = 120;
	private byte CURRENT_CHARACTERS = 0;
	
	private boolean isChatMenuOpen;
	private BufferedImage chatmenu_left;
	private BufferedImage chatmenu_middle;
	private BufferedImage chatmenu_right;
	
	private String chatString = "";
	private String chatlog = "";
	private String[] line;
	
	private int greaterThen;
	private int lessThen;
	private int y;
	
	public Chatbox() {
		isChatMenuOpen = false;
		chatmenu_left = ImageUtils.loadTranslucentImage("resources/sprites/interfaces/chatmenu_left.gif", 0.50f);
		chatmenu_middle = ImageUtils.loadTranslucentImage("resources/sprites/interfaces/chatmenu_middle.gif", 0.50f);
		chatmenu_right = ImageUtils.loadTranslucentImage("resources/sprites/interfaces/chatmenu_right.gif", 0.50f);
		
		disallowedCharacters.add(KeyEvent.VK_ENTER);
		disallowedCharacters.add(KeyEvent.VK_PAGE_DOWN);
		disallowedCharacters.add(KeyEvent.VK_SHIFT);
		disallowedCharacters.add(KeyEvent.VK_BACK_SPACE);
		disallowedCharacters.add(KeyEvent.VK_ESCAPE);
	}
	
	/**
	 * Send a message.
	 */
	public void sendMessage(KeyEvent e) {
		//Backspace characters.
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
			GameConstants.getChat().setChatString(StringUtils.backspace(GameConstants.getChat().getChatString()));
		}
		
		// We don't want to write stuff for these buttons
		if(e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && !disallowedCharacters.contains(e.getKeyCode()) && CURRENT_CHARACTERS < MAXIUM_CHARACTERS) {
			appendChatString(String.valueOf(e.getKeyChar()));
		}
		
		//This will send the whole string to the server.
		if(e.getKeyCode() == KeyEvent.VK_ENTER && chatString.length() > 0){
			setChatlog(chatlog + GameConstants.getPlayer().getId() + " - " + chatString + "~");
			
			//Check for multiplayer here
			if(GameConstants.isMultiplayer())
			GameConstants.getPacketManager().sendPacket(PacketHeaders.PLAYER_SEND_MESSAGE.getHeader() +
					GameConstants.getPlayer().getId() + ":" +
					chatString);
						
			//After we've sent our message, we want to clear the chat string.
			setChatString("");
		}
	}
	
	public byte getMaximumCharacters() {
		return MAXIUM_CHARACTERS;
	}
	
	public byte getCurrentCharacters() {
		return CURRENT_CHARACTERS;
	}
	
	public void setCurrentCharacters(byte currentCharacters) {
		this.CURRENT_CHARACTERS = currentCharacters;
	}
	
	public boolean isChatMenuOpen() {
		return isChatMenuOpen;
	}

	public void setChatMenuOpen(boolean isChatMenuOpen) {
		this.isChatMenuOpen = isChatMenuOpen;
	}

	public String getChatString() {
		return chatString;
	}
	
	public void setChatString(String chatString) {
		this.chatString = chatString;
		this.CURRENT_CHARACTERS = (byte) this.chatString.length();
	}

	public void appendChatString(String string) {
		this.chatString += string;
		this.CURRENT_CHARACTERS = (byte) this.chatString.length();
	}

	public String getChatlog() {
		return chatlog;
	}

	public void setChatlog(String chatlog) {
		this.chatlog = chatlog;
	}

	@Override
	public void render(Graphics2D g) {	
		/**
		 * The menu will now auto fill the whole screen width.
		 */
		g.drawImage(chatmenu_left, 0, 0, chatmenu_left.getWidth(), chatmenu_left.getHeight(), null);
		if(!Constants.isFullscreen()){
			g.drawImage(chatmenu_middle, chatmenu_left.getWidth(), 0, Constants.getWidth() - 37, chatmenu_middle.getHeight(), null);
			g.drawImage(chatmenu_right, Constants.getWidth() -17, 0, chatmenu_right.getWidth(), chatmenu_right.getHeight(), null);
		}else{
			g.drawImage(chatmenu_middle, chatmenu_left.getWidth(), 0, Constants.getWidth() - 21, chatmenu_middle.getHeight(), null);
			g.drawImage(chatmenu_right, Constants.getWidth() -1, 0, chatmenu_right.getWidth(), chatmenu_right.getHeight(), null);
		}
		
		//Draw the string
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 12));
		g.drawString(chatString, 15, 145);

		// Split the chat log into messages
		line = chatlog.split("~");	
		
		greaterThen = line.length -9 + chatSelectedIndex;
		lessThen = line.length - chatSelectedIndex;
		y = CHAT_MENU_TOP;
		
		/**
		 * This will clear out old unseen logs.
		 */
		if(line.length > CHAT_LOG_LINES){
			chatlog = "";
			
			for(int i = 0; i < line.length; i++)
				if(i > greaterThen && i < lessThen) 
					chatlog += line[i] + "~";
		}
			
		/**
		 * Only show last 8 lines.
		 */
		for(int i = 0 ; i < line.length ; i++)
			if(i > greaterThen && i < lessThen)
				g.drawString(line[i], 10, y += 15);
	}

}