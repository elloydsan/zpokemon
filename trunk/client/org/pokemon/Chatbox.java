package org.pokemon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.zengine.graphics.Paintable;
import org.zengine.networking.PacketHeaders;
import org.zengine.uils.ImageUtils;
import org.zengine.uils.StringUtils;

/**
 * This class implements a chat box that will be used for chatting amongst players.
 * Functionality will be added to allow developers to test certain commands. 
 * For example sending packets, initiating Pokemon fights etc...
 * 
 * TODO
 * Clean up the chat log so it contains only the last 8 messages 	- Troy: Done
 * Make the chat box more transparent								- Troy: Done
 * Figure out a place where to put the chat box 					- Troy: Done
 * Enable remove characters with backspace							- Troy: Done
 * Disable characters that we do not want.						 	- Troy: Done
 * Take a closer look at the maximum character system				- Troy: Done
 * 
 * @author NerdyGnome
 * 
 * Edited by Troy.
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
	private BufferedImage chatMenuImage;
	
	private String chatString = "";
	private String chatlog = "";
	private String[] line;
	
	public Chatbox() {
		isChatMenuOpen = false;
		chatMenuImage = ImageUtils.loadTranslucentImage("resources/sprites/interfaces/devScreen.gif", 0.50f);
		
		disallowedCharacters.add(KeyEvent.VK_ENTER);
		disallowedCharacters.add(KeyEvent.VK_PAGE_DOWN);
		disallowedCharacters.add(KeyEvent.VK_SHIFT);
		disallowedCharacters.add(KeyEvent.VK_BACK_SPACE);
	}
	
	/**
	 * Implement this later to make it easier for the user. 
	 * This way you will only have to write sendMessage("hello");
	 * 
	 * Troy: Done.
	 */
	public void sendMessage(KeyEvent e) {
		//Backspace characters.
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
			GameConstants.getChat().setChatString(StringUtils.backspace(GameConstants.getChat().getChatString()));
		}
		
		// We don't want to write stuff for these buttons
		if(!disallowedCharacters.contains(e.getKeyCode()) && CURRENT_CHARACTERS < MAXIUM_CHARACTERS) {
			appendChatString(String.valueOf(e.getKeyChar()));
		}
		
		//This will send the whole string to the server.
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
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

	public BufferedImage getChatMenuImage() {
		return chatMenuImage;
	}

	public void setChatMenuImage(BufferedImage chatMenuImage) {
		this.chatMenuImage = chatMenuImage;
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
	public void draw(Graphics g) {
		/**
		 * This is silly because it will reload every time a draw is called
		 * which happens to be about 30 times per second.
		 */
		//InterfaceLoader.load(new byte[]{0});
		//chatMenuImage = InterfaceLoader.getInterfaceImages().get(0);
		
		/**
		 * Paints the menu and text. Make sure to make it more flexible, so when
		 * the window size is changed, the position of the chatmenu is also changed.
		 */
		g.drawImage(chatMenuImage, 0, 0, null);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 12));
		g.drawString(chatString, 15, 145);

		// Split the chat log into messages
		line = chatlog.split("~");	
		
		int greaterThen = line.length -9 + chatSelectedIndex;
		int lessThen = line.length - chatSelectedIndex;
		int y = CHAT_MENU_TOP;
		
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