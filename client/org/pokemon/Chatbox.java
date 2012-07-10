package org.pokemon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.zengine.Constants;
import org.zengine.graphics.*;

/**
 * This class implements a chatbox that will be used for chatting amongst players.
 * Functionality will be added to allowe developers to test certain commands. 
 * For example sending packets, initiating pokemon fights etc...
 * 
 * TODO
 * Clean up the chatlog so it contains only the last 8 messages
 * Make the chatbox more transparent
 * Figure out a place where to put the chatbox
 * Enable remove characters with backspace
 * Disable characters that we do not want. For example ESC, shift etc...
 * Take a closer look at the maxium character system
 * 
 * @author NerdyGnome
 *
 */
public class Chatbox implements Paintable{

	private static final byte CHAT_LOG_LINES = 8;
	private static final byte CHAT_MENU_TOP = 5;
	
	private byte chatSelectedIndex = 0;
	private byte MAXIUM_CHARACTERS = 45;
	private byte curNrOfCharacters = 0;
	
	private boolean isChatMenuOpen;
	private BufferedImage chatMenuImage;
	
	private String chatString = "";
	private String chatlog = ""; // The whole chatlog
	private String[] line;
	private String placeHolder = "";
	
	public Chatbox() {
		isChatMenuOpen = false;
	}
	
	/* Implement this later to make it easier for the user. 
	 * This way you will only have to write sendMessage("hello");
	 */
	public void sendMessage(String message) {		
	}
	
	public byte getMaximumCharacters() {
		return MAXIUM_CHARACTERS;
	}
	
	public byte getCurNrOfCharacters() {
		return curNrOfCharacters;
	}
	
	public void setCurNrOfCharacters(byte curNrOfCharacters) {
		this.curNrOfCharacters = curNrOfCharacters;
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
	}

	public String getChatlog() {
		return chatlog;
	}

	public void setChatlog(String chatlog) {
		this.chatlog = chatlog;
	}

	@Override
	public void draw(Graphics g) {
		
		InterfaceLoader.load(new byte[]{0});
		chatMenuImage = InterfaceLoader.getInterfaceImages().get(0);
		
		/*
		 * Paints the menu and text. Make sure to make it more flexible, so when
		 * the window size is changed, the position of the chatmenu is also changed.
		 */
		g.drawImage(chatMenuImage, (Constants.getWidth()/2) -203, 0, null);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 12));
		
		g.drawString(chatString,  (Constants.getWidth()/2)-185, 145);
		

		// Split the chatlog into messages
		line = chatlog.split("~");	
		
		int greaterThen = line.length -9 + chatSelectedIndex;
		int lessThen = line.length - chatSelectedIndex;
		int y = CHAT_MENU_TOP;
				
		// We only want to show the last 8 message
		for(int i = 0 ; i < line.length ; i++) 
			if(i > greaterThen && i < lessThen) 
				g.drawString(line[i], (Constants.getWidth()/2)-190, y += 15);
				
		// Add something here that deletes all but the last 8 messagex from chatlog
		//System.out.println(placeHolder);
		//chatlog = placeHolder;
		//placeHolder = "";
				
	}

}
