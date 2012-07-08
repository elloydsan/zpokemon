package org.pokemon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import org.zengine.Constants;
import org.zengine.Game;
import org.zengine.GameFrame;
import org.zengine.TileMap;
import org.zengine.networking.PacketManager;
import org.zengine.uils.Image;

/**
 * 
 * @author Troy
 *
 */
public class Pokemon extends Game{
	private final int UPDATE_RATE = 30;
	private final long UPDATE_PERIOD = 1000L / UPDATE_RATE;
	public static long beginTime, timeTaken, timeLeft, lastLoopTime, delta;
	
	public static boolean left,right,up,down;

	public static BufferedImage[] tileTextures;
	private Point xy = new Point(0,0);

	@Override
	public void draw(Graphics g) {	
		GameConstants.getTilemap().draw(g);
		GameConstants.getPlayer().draw(g);
		
		//Multiplayer
		if(GameConstants.getPlayerList() != null)
		for(OtherPlayerEntity p : GameConstants.getPlayerList()){
			p.draw(g);
		}
		
		g.setColor(Color.WHITE);
		g.drawString("FPS: " + Constants.getRender().getFps(), 10, 20);
		g.drawString("Mouse XY: " + xy.x + "," + xy.y, 10, 35);
	}

	@Override
	public void onStart() {
		System.out.println("Loading Pokemon.");
		tileTextures = Image.splitImage(Image.makeColorTransparent("resources/sprites/textures/pokemonTextures.gif", new Color(255,0,255)), 15, 15);
		GameConstants.setPlayerImages(Image.splitImage(Image.makeColorTransparent("resources/sprites/players/pokemonPlayer.gif", new Color(255,0,255)), 12, 4));
	}

	@Override
	public void start() {
		/**
		 * Init variables etc...
		 */
		new GameFrame("Pokemon",this);
		Constants.getGameFrame().setIconImage(Toolkit.getDefaultToolkit().getImage(Pokemon.class.getResource("/resources/icons/pokeball.png")));
		GameConstants.setTilemap(new TileMap((short)20,(short)15,(short)20,(short)20,tileTextures));
		GameConstants.setPlayer(new PlayerEntity((short)180, (short)60, (short)35, (short)35, (byte)0, (byte)0, (short)10, (short)0));
		
		/**
		 * Start the game loop
		 */
		System.out.println("Starting game.");
		if(GameConstants.isMultiplayer())
			GameConstants.setPacketManager(new PacketManager("127.0.0.1",5632));
		gameLoop();
	}
	
	public void gameLoop(){
		lastLoopTime = System.currentTimeMillis();
		
		while(true){
			try{
				beginTime = System.currentTimeMillis();
				delta = beginTime - lastLoopTime;
				lastLoopTime = beginTime;
					
				/**
				 * Perform and update / moves etc...
				 */
				update();
					
				timeTaken = System.currentTimeMillis() - beginTime;
				timeLeft = (UPDATE_PERIOD - timeTaken);
		
				if(timeLeft < 10) timeLeft = 10;
				
				try{
					Thread.sleep(timeLeft);
				}catch(InterruptedException ex){break;}
			}catch(Exception e){}
		}
	}
	
	/**
	 * Perform game updates and checks in here.
	 */
	public void update(){
		/**
		 * Update players movement.
		 */
		GameConstants.getPlayer().move(up, down, left, right, delta);
	}

	@Override
	public void onEnd() {
		System.out.println("Ending.");
	}

	/**
	 * Key pressed
	 * 
	 * The only way to stop glitch's when moving is 
	 * to only allow one key press at a time.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_W: //UP
			if(!GameConstants.getPlayer().isMoving() && !down && !left && !right)
			if(GameConstants.isTileFree((int)(GameConstants.getPlayer().getX() / GameConstants.getTilemap().getTileWidth()), 
					(int) (GameConstants.getPlayer().getY() - GameConstants.getTilemap().getTileHeight()) / GameConstants.getTilemap().getTileHeight())){
				up = true;
				GameConstants.getPlayer().setDirection((byte)0);
				GameConstants.getPlayer().setDy(GameConstants.getPlayer().getDy() - GameConstants.getTilemap().getTileHeight());
				GameConstants.getPlayer().setMoving(true);
			}
			break;
		case KeyEvent.VK_S: //DOWN
			if(!GameConstants.getPlayer().isMoving() && !up && !left && !right)
			if(GameConstants.isTileFree((int)(GameConstants.getPlayer().getX() / GameConstants.getTilemap().getTileWidth()), 
					(int) (GameConstants.getPlayer().getY() + GameConstants.getTilemap().getTileHeight()) / GameConstants.getTilemap().getTileHeight())){
				down = true;
				GameConstants.getPlayer().setDirection((byte)1);
				GameConstants.getPlayer().setDy(GameConstants.getPlayer().getDy() + GameConstants.getTilemap().getTileHeight());
				GameConstants.getPlayer().setMoving(true);
			}
			break;
		case KeyEvent.VK_A: //LEFT
			if(!GameConstants.getPlayer().isMoving() && !up && !down && !right)
			if(GameConstants.isTileFree((int)(GameConstants.getPlayer().getX() - GameConstants.getTilemap().getTileWidth()) / GameConstants.getTilemap().getTileWidth(), 
					(int)GameConstants.getPlayer().getY() / GameConstants.getTilemap().getTileHeight())){
				left = true;
				GameConstants.getPlayer().setDirection((byte)2);
				GameConstants.getPlayer().setDx(GameConstants.getPlayer().getDx() - GameConstants.getTilemap().getTileWidth());
				GameConstants.getPlayer().setMoving(true);
			}
			break;
		case KeyEvent.VK_D: //RIGHT
			if(!GameConstants.getPlayer().isMoving() && !up && !down && !left)
			if(GameConstants.isTileFree((int)(GameConstants.getPlayer().getX() + GameConstants.getTilemap().getTileWidth()) / GameConstants.getTilemap().getTileWidth(), 
					(int)GameConstants.getPlayer().getY() / GameConstants.getTilemap().getTileHeight())){
				right = true;
				GameConstants.getPlayer().setDirection((byte)3);
				GameConstants.getPlayer().setDx(GameConstants.getPlayer().getDx() + GameConstants.getTilemap().getTileWidth());
				GameConstants.getPlayer().setMoving(true);
			}
			break;
		}
	}

	/**
	 * Key released
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_W:
			up = false;
			break;
		case KeyEvent.VK_S:
			down = false;
			break;
		case KeyEvent.VK_A:
			left = false;
			break;
		case KeyEvent.VK_D:
			right = false;
			break;
		}
	}
	
	/**
	 * Key typed
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_ESCAPE: //Full screen
			if(Constants.isFullscreen()){
				Constants.setFullscreen(false);
				Constants.getGameFrame().setFullscreen(false);
			}else{
				Constants.setFullscreen(true);
				Constants.getGameFrame().setFullscreen(true);
			}
			break;
		}
	}

	/**
	 * These event's will most likely be forwarded
	 * onto another class to deal with them appropriately.
	 */
	
	@Override
	public void mouseMoved(MouseEvent e) {
		xy = new Point(e.getX(),e.getY());
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}

}