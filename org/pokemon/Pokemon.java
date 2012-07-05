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
	
	private boolean left,right,up,down;
	public static TileMap tileMap;
	private PlayerEntity player;
	public static BufferedImage[] tileTextures;
	public static BufferedImage[] playerImages;
	private Point xy = new Point(0,0);

	@Override
	public void draw(Graphics g) {	
		tileMap.draw(g);
		player.draw(g);
		
		g.setColor(Color.WHITE);
		g.drawString("FPS: " + Constants.getRender().getFps(), 10, 20);
		g.drawString("Mouse XY: " + xy.x + "," + xy.y, 10, 35);
	}

	@Override
	public void onStart() {
		System.out.println("Loading Pokemon.");
		tileTextures = Image.splitImage(Image.makeColorTransparent("resources/sprites/textures/pokemonTextures.gif", new Color(255,0,255)), 15, 15);
		playerImages = Image.splitImage(Image.makeColorTransparent("resources/sprites/players/pokemonPlayer.gif", new Color(255,0,255)), 12, 4);
	}

	@Override
	public void start() {
		/**
		 * Init variables etc...
		 */
		new GameFrame("Pokemon",this);
		Constants.getGameFrame().setIconImage(Toolkit.getDefaultToolkit().getImage(Pokemon.class.getResource("/resources/icons/pokeball.png")));
		tileMap = new TileMap((short)20,(short)10,(short)20,(short)20,tileTextures);
		player = new PlayerEntity((short)9, (short)3, (short)35, (short)35, (byte)0, (byte)0, (short)100, (short)0, playerImages);
		
		/**
		 * Start the game loop
		 */
		System.out.println("Starting game.");
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
		
		/**
		 * Pixel based movement... sucks...
		 */
		/*if(player.isMoving() == true) {
			player.setPcount((byte) (player.getPcount() + player.getSpeed()));
			
			if(up){
				tileMap.setyOffSet((tileMap.getyOffSet() + player.getSpeed()));
		    }
			
			else if(down) {
				tileMap.setyOffSet((tileMap.getyOffSet() - player.getSpeed()));
		    }
			
			else if(left) {
				tileMap.setxOffSet((tileMap.getxOffSet() + player.getSpeed()));
		    }
			
			else if(right) {
				tileMap.setxOffSet((tileMap.getxOffSet() - player.getSpeed()));
		    }
	    }
		
	    if(player.getPcount() >= 20) {
	    	player.setPcount((byte)0);
			player.setMoving(false);
			up = false;
			down = false;
			left = false;
			right = false;
	    }*/
	    
		
		/**
		 * Time based movement, also kind of sucks...
		 */
		if(up && player.getY() >= 0){
			player.setMoving(true);
			player.setDirection((byte)0);
			player.setAnimation((byte) 6);
			//player.setDy(player.getY() - 1);
			player.setY(player.getY() - ((delta * player.getSpeed()) /1000D) / tileMap.getTileHeight());
			tileMap.setyOffSet((tileMap.getyOffSet() + (delta * player.getSpeed()) /1000D));
		}
			
		else if(down && player.getY() <= (tileMap.getTileRows() -1)){
			player.setMoving(true);
			player.setDirection((byte)1);
			player.setAnimation((byte) 0);
			//player.setDy(player.getY() + 1);
			player.setY(player.getY() + ((delta * player.getSpeed()) /1000D) / tileMap.getTileHeight());
			tileMap.setyOffSet((tileMap.getyOffSet() - (delta * player.getSpeed()) /1000D));
		}
			
		else if(left && player.getX() >= 0){
			player.setMoving(true);
			player.setDirection((byte)2);
			player.setAnimation((byte) 3);
			//player.setDx(player.getY() - 1);
			player.setX(player.getX() - ((delta * player.getSpeed()) /1000D) / tileMap.getTileWidth());
			tileMap.setxOffSet((tileMap.getxOffSet() + (delta * player.getSpeed()) /1000D));
		}
			
		else if(right && player.getX() <= (tileMap.getTileCols() -1)){
			player.setMoving(true);
			player.setDirection((byte)3);
			player.setAnimation((byte) 9);
			//player.setDx(player.getY() + 1);
			player.setX(player.getX() + ((delta * player.getSpeed()) /1000D) / tileMap.getTileWidth());
			tileMap.setxOffSet((tileMap.getxOffSet() - (delta * player.getSpeed()) /1000D));
		}
		
		//Continue moving until destination is reached.
		/*if(!up && !down && !left && !right && player.isMoving()){
			switch(player.getDirection()){
			case 0:
				if(((int)player.getY() - (0.5)) != (int)player.getDy()){
					player.setY(player.getY() - ((delta * player.getSpeed()) /1000D) / tileMap.getTileHeight());
					tileMap.setyOffSet((tileMap.getyOffSet() + (delta * player.getSpeed()) /1000D));
				}else{
					player.setMoving(false);
				}
				break;
			case 1:
				if((int)player.getY() != (int)player.getDy()){
					player.setY(player.getY() + ((delta * player.getSpeed()) /1000D) / tileMap.getTileHeight());
					tileMap.setyOffSet((tileMap.getyOffSet() - (delta * player.getSpeed()) /1000D));
				}else{
					player.setMoving(false);
				}
				break;
			case 2:
				if((int)player.getX() != (int)player.getDx()){
					player.setX(player.getX() - ((delta * player.getSpeed()) /1000D) / tileMap.getTileWidth());
					tileMap.setxOffSet((tileMap.getxOffSet() + (delta * player.getSpeed()) /1000D));
				}else{
					player.setMoving(false);
				}
				break;
			case 3:
				if((int)player.getX() != (int)player.getDx()){
					player.setX(player.getX() + ((delta * player.getSpeed()) /1000D) / tileMap.getTileWidth());
					tileMap.setxOffSet((tileMap.getxOffSet() - (delta * player.getSpeed()) /1000D));
				}else{
					player.setMoving(false);
				}
				break;
			}
		}*/
	}

	@Override
	public void onEnd() {
		System.out.println("Ending.");
	}
	
	/**
	 * Check if a tile is walkable.
	 * 
	 * @param x
	 * @param y
	 * @return boolean
	 */
	public boolean isTileFree(int x, int y){
		System.out.println(x + "," + y);
		return (tileMap.getLayer1()[x][y].getState() == 0);
	}

	/**
	 * Key pressed
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_W:
			up = true;
			player.setMoving(true);
			break;
		case KeyEvent.VK_S:
			down = true;
			player.setMoving(true);
			break;
		case KeyEvent.VK_A:
			left = true;
			player.setMoving(true);
			break;
		case KeyEvent.VK_D:
			right = true;
			player.setMoving(true);
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