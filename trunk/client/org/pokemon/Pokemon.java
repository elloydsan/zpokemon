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
import org.zengine.networking.PacketManager;
import org.zengine.uils.ImageUtils;

/**
 * 
 * @author Troy
 *
 */
public class Pokemon extends Game{
	private final int UPDATE_RATE = 30;
	private final long UPDATE_PERIOD = 1000L / UPDATE_RATE;
	public static long beginTime, timeTaken, timeLeft, lastLoopTime, delta;
	
	public GameStates state = GameStates.LOADING;
	
	public static boolean left,right,up,down;

	public static BufferedImage[] tileTextures;
	private Point xy = new Point(0,0);
	
	private boolean showmem = false;

	@Override
	public void render(Graphics g) {
		switch(state){
		case LOADING:
			g.setColor(Color.WHITE);
			g.drawString("Loading...", (Constants.getGameFrame().getWidth() / 2) - 30, (Constants.getGameFrame().getHeight() /2) - 30);
			break;
		case INITIALISED:
			break;
		case MULTIPLAYER:
			break;
		case CONNECT:
			break;
		case PLAYING:
			GameConstants.getTilemap().render(g);
			GameConstants.getPlayer().render(g);
			
			//Multiplayer
			if(GameConstants.getPlayerList() != null)
			for(OtherPlayerEntity p : GameConstants.getPlayerList())
				p.render(g);
			
			//Chatmenu
			if(GameConstants.getChat().isChatMenuOpen()) 
				GameConstants.getChat().render(g);
			break;
		case PAUSED:
			break;
		case BATTLE:	
			break;
		case DESTROYED:
			break;
		}
		
		if(state != GameStates.LOADING){
			if(!GameConstants.getChat().isChatMenuOpen()){
				g.setColor(Color.WHITE);
				g.drawString("FPS: " + Constants.getRender().getFps(), 10, 20);
				g.drawString("Mouse XY: " + xy.x + "," + xy.y, 10, 35);
			}
			
			if(showmem){
				g.setColor(Color.YELLOW);
				g.drawString("Total Memory: " + Constants.formatBytes(Constants.getRunTime().totalMemory()), Constants.getWidth() - 200, 20);
				g.drawString("Free Memory: " + Constants.formatBytes(Constants.getRunTime().freeMemory()), Constants.getWidth() - 200, 35);
				g.drawString("Used Memory: " + Constants.formatBytes((Constants.getRunTime().totalMemory() - Constants.getRunTime().freeMemory())), 
						Constants.getWidth() - 200, 50);
			}
			
			/**
			 * Draw custom mouse.
			 */
			g.setColor(Color.RED);
			g.drawLine(xy.x -4, xy.y, xy.x +4, xy.y);
			g.drawLine(xy.x, xy.y -4, xy.x, xy.y +4);
		}
	}

	@Override
	public void onStart() {
		/**
		 * Init variables etc...
		 */
		System.out.println("Loading Pokemon.");
		tileTextures = ImageUtils.splitImage(ImageUtils.makeColorTransparent("resources/sprites/textures/pokemonTextures.gif", new Color(255,0,255)), 15, 15);
		GameConstants.setPlayerImages(ImageUtils.splitImage(ImageUtils.makeColorTransparent("resources/sprites/players/pokemonPlayer.gif", new Color(255,0,255)), 12, 4));
		
		new GameFrame("Pokemon",this);
		Constants.getGameFrame().consumeMouse(true);
		Constants.getGameFrame().setIconImage(Toolkit.getDefaultToolkit().getImage(Pokemon.class.getResource("/resources/icons/pokeball.png")));
		GameConstants.setPlayer(new PlayerEntity((short)280, (short)160, (short)35, (short)35, (byte)0, (byte)0, (short)10, (short)0));
		GameConstants.setChat(new Chatbox());
		
		/**
		 * Multiplayer check.
		 */
		System.out.println("Starting game.");
		if(GameConstants.isMultiplayer()){
			GameConstants.setPacketManager(new PacketManager("127.0.0.1",5632));
			//GameConstants.setPacketManager(new PacketManager("118.208.29.29",5632));
		}else{
			GameConstants.setTilemap(new TileMap((short)30,(short)20,(short)20,(short)20,tileTextures));
		}
	}

	@Override
	public void start() {
		/**
		 * Anything else that need's to happen on start.
		 */
		while(GameConstants.getTilemap() == null){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Start the game loop.
		 * 
		 * This state will be set from a menu
		 * screen later on.
		 */
		state = GameStates.PLAYING;
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
	public void keyPressed(KeyEvent e){
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
		case KeyEvent.VK_W: //UP
			if(!GameConstants.getPlayer().isMoving() && !GameConstants.getChat().isChatMenuOpen())
			if(GameConstants.isTileFree((int)(GameConstants.getPlayer().getX() / GameConstants.getTilemap().getTileWidth()), 
					(int) (GameConstants.getPlayer().getY() - GameConstants.getTilemap().getTileHeight()) / GameConstants.getTilemap().getTileHeight())){
				up = true;
				GameConstants.getPlayer().setAnimation((byte) 6);
				GameConstants.getPlayer().setDirection((byte) 0);
				GameConstants.getPlayer().setDy(GameConstants.getPlayer().getDy() - GameConstants.getTilemap().getTileHeight());
				GameConstants.getPlayer().setMoving(true);
			}
			break;
		case KeyEvent.VK_S: //DOWN
			if(!GameConstants.getPlayer().isMoving() && !GameConstants.getChat().isChatMenuOpen())
			if(GameConstants.isTileFree((int)(GameConstants.getPlayer().getX() / GameConstants.getTilemap().getTileWidth()), 
					(int) (GameConstants.getPlayer().getY() + GameConstants.getTilemap().getTileHeight()) / GameConstants.getTilemap().getTileHeight())){
				down = true;
				GameConstants.getPlayer().setAnimation((byte) 0);
				GameConstants.getPlayer().setDirection((byte) 1);
				GameConstants.getPlayer().setDy(GameConstants.getPlayer().getDy() + GameConstants.getTilemap().getTileHeight());
				GameConstants.getPlayer().setMoving(true);
			}
			break;
		case KeyEvent.VK_A: //LEFT
			if(!GameConstants.getPlayer().isMoving() && !GameConstants.getChat().isChatMenuOpen())
			if(GameConstants.isTileFree((int)(GameConstants.getPlayer().getX() - GameConstants.getTilemap().getTileWidth()) / GameConstants.getTilemap().getTileWidth(), 
					(int)GameConstants.getPlayer().getY() / GameConstants.getTilemap().getTileHeight())){
				left = true;
				GameConstants.getPlayer().setAnimation((byte) 3);
				GameConstants.getPlayer().setDirection((byte) 2);
				GameConstants.getPlayer().setDx(GameConstants.getPlayer().getDx() - GameConstants.getTilemap().getTileWidth());
				GameConstants.getPlayer().setMoving(true);
			}
			break;
		case KeyEvent.VK_D: //RIGHT
			if(!GameConstants.getPlayer().isMoving() && !GameConstants.getChat().isChatMenuOpen())
			if(GameConstants.isTileFree((int)(GameConstants.getPlayer().getX() + GameConstants.getTilemap().getTileWidth()) / GameConstants.getTilemap().getTileWidth(), 
					(int)GameConstants.getPlayer().getY() / GameConstants.getTilemap().getTileHeight())){
				right = true;
				GameConstants.getPlayer().setAnimation((byte) 9);
				GameConstants.getPlayer().setDirection((byte) 3);
				GameConstants.getPlayer().setDx(GameConstants.getPlayer().getDx() + GameConstants.getTilemap().getTileWidth());
				GameConstants.getPlayer().setMoving(true);
			}
			break;
		case KeyEvent.VK_PAGE_DOWN: // CHATMENU
			if(GameConstants.getChat().isChatMenuOpen()) { 
				GameConstants.getChat().setChatMenuOpen(false);			
				GameConstants.getChat().setChatString("");
			}else{
				GameConstants.getChat().setChatMenuOpen(true);				
			}
			break;
		case KeyEvent.VK_PAGE_UP: //Show mem stats
			if(showmem)
				showmem = false;
			else
				showmem = true;
			break;
		}
		
		/**
		 * Checks input for chat menu.
		 */
		if(GameConstants.getChat().isChatMenuOpen()){
			GameConstants.getChat().sendMessage(e);
		}
		
		forwardEvents(null,e);
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
		
		forwardEvents(null,e);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		forwardEvents(null,e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		xy = new Point(e.getX(),e.getY());
		forwardEvents(e,null);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		forwardEvents(e,null);
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		forwardEvents(e,null);
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		forwardEvents(e,null);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		forwardEvents(e,null);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		forwardEvents(e,null);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		forwardEvents(e,null);
	}

	/**
	 * This will forward on event's to the appropriate
	 * interfaces and such.
	 * 
	 * @param me
	 * @param ke
	 */
	public void forwardEvents(MouseEvent me, KeyEvent ke){
		switch(state){
		case LOADING:
			break;
		case INITIALISED:
			break;
		case MULTIPLAYER:
			break;
		case CONNECT:
			break;
		case PLAYING:
			break;
		case PAUSED:
			break;
		case BATTLE:	
			break;
		case DESTROYED:
			break;
		}
	}
	
}