package org.pokemon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import org.pokemon.entities.OtherPlayerEntity;
import org.pokemon.entities.PlayerEntity;
import org.pokemon.interfaces.MinimapInterface;
import org.pokemon.utils.MapLoader;
import org.zengine.Constants;
import org.zengine.Game;
import org.zengine.GameFrame;
import org.zengine.networking.PacketManager;
import org.zengine.uils.ImageUtils;

/**
 * 
 * @author Troy
 * 
 * TODO 
 * Get the current date / time
 * Make night / day transition
 *
 */
public class Pokemon extends Game{
	private final int UPDATE_RATE = 30;
	private final long UPDATE_PERIOD = 1000L / UPDATE_RATE;
	public static long beginTime, timeTaken, timeLeft, lastLoopTime, delta;
	
	public static GameStates state = GameStates.LOADING;
	
	public static boolean left,right,up,down,showmem,night,menu;

	public static BufferedImage[] tileTextures;
	private Point xy = new Point(0,0);
	
	public static BufferedImage filter;
	public static BufferedImage filterEffect;

	@Override
	public void render(Graphics2D g) {
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
			/**
			 * Tilemap
			 */
			GameConstants.getTilemap().render(g);
			
			/**
			 * Player
			 */
			GameConstants.getPlayer().render(g);
			
			/**
			 * Multiplayer
			 */
			if(GameConstants.isMultiplayer()){
				if(GameConstants.getPlayerList() != null)
				for(OtherPlayerEntity p : GameConstants.getPlayerList())
					p.render(g);
			}
			
			/**
			 * This apply's night / day transitions plus the night.
			 * 
			 * All interfaces that do not apply the night effect
			 * should be rendered after this point.
			 */
			g.drawImage(filterEffect, 0, 0, Constants.getWidth(), Constants.getHeight(), null);
			
			/**
			 * Minimap
			 */
			if(menu && !GameConstants.getChat().isChatMenuOpen())
				GameConstants.getMinimap().render(g);
			
			/**
			 * Chatmenu
			 */
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
				g.drawString("Total Memory: " + Constants.formatBytes(Constants.getRunTime().totalMemory()), Constants.getWidth() - 300, 20);
				g.drawString("Free Memory: " + Constants.formatBytes(Constants.getRunTime().freeMemory()), Constants.getWidth() - 300, 35);
				g.drawString("Used Memory: " + Constants.formatBytes((Constants.getRunTime().totalMemory() - Constants.getRunTime().freeMemory())), 
						Constants.getWidth() - 300, 50);
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
		GameConstants.setTileImages(ImageUtils.splitImage(ImageUtils.makeColorTransparent("resources/sprites/textures/pokemonTextures.gif", new Color(255,0,255)), 15, 15));
		GameConstants.setPlayerImages(ImageUtils.splitImage(ImageUtils.makeColorTransparent("resources/sprites/players/pokemonPlayer.gif", new Color(255,0,255)), 12, 4));
		filter = ImageUtils.loadImage("resources/sprites/textures/filter.gif");
		
		new GameFrame("Pokemon",this);
		Constants.getGameFrame().consumeMouse(true);
		Constants.getGameFrame().setIconImage(Toolkit.getDefaultToolkit().getImage(Pokemon.class.getResource("/resources/icons/pokeball.png")));
		GameConstants.setPlayer(new PlayerEntity((short)280, (short)160, (short)35, (short)35, (byte)0, (byte)0, (short)10, (short)0));
		GameConstants.setChat(new Chatbox());
		GameConstants.setMinimap(new MinimapInterface((short) ((short)Constants.getWidth() - 155), (short)10, (short)130, (short)130));
		menu = true;
		
		/**
		 * Multiplayer check.
		 */
		System.out.println("Starting game.");
		if(GameConstants.isMultiplayer()){
			GameConstants.setPacketManager(new PacketManager("127.0.0.1",5632));
			//GameConstants.setPacketManager(new PacketManager("118.208.29.29",5632));
		}else{
			//GameConstants.setTilemap(new TileMap("test", (short)30,(short)20,(short)20,(short)20,100,100));
			GameConstants.setTilemap(MapLoader.loadmap("map"));
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
		if(!GameConstants.isMultiplayer())
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
		
		/**
		 * Night time settings.
		 */
		if(!GameConstants.isNight() && GameConstants.getFulldayTimer().isUp()){
			if(GameConstants.getTransitionTimer().isNotUp()){
				if(GameConstants.getChangeTransitionTimer().isUp() && GameConstants.getTransition() < 0.6f){
					GameConstants.setTransition(GameConstants.getTransition() + 0.0033f);
					filterEffect = ImageUtils.changeTranslucentImage(filter, GameConstants.getTransition());
					GameConstants.getMinimap().setSunmoonY(GameConstants.getMinimap().getSunmoonY() + (double)0.1485);
					GameConstants.getChangeTransitionTimer().reset();
				}
			}else{
				GameConstants.getFullnightTimer().reset();
				GameConstants.getTransitionTimer().reset();
				GameConstants.setNight(true);
			}
		}else if(!GameConstants.isNight() && GameConstants.getFulldayTimer().isNotUp()){
			GameConstants.getTransitionTimer().reset();
		}
		
		/**
		 * Change back to day.
		 */
		if(GameConstants.isNight() && GameConstants.getFulldayTimer().isUp()){
			if(GameConstants.getTransitionTimer().isNotUp()){
				if(GameConstants.getChangeTransitionTimer().isUp() && GameConstants.getTransition() > 0.0033f){
					GameConstants.setTransition(GameConstants.getTransition() - 0.0033f);
					filterEffect = ImageUtils.changeTranslucentImage(filter, GameConstants.getTransition());
					GameConstants.getMinimap().setSunmoonY(GameConstants.getMinimap().getSunmoonY() - (double)0.1485);
					GameConstants.getChangeTransitionTimer().reset();
				}
			}else{
				GameConstants.getFulldayTimer().reset();
				GameConstants.getTransitionTimer().reset();
				GameConstants.setNight(false);
			}
		}else if(GameConstants.isNight() && GameConstants.getFullnightTimer().isNotUp()){
			GameConstants.getTransitionTimer().reset();
		}
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
		case KeyEvent.VK_M:
			if(!GameConstants.getChat().isChatMenuOpen()){
				if(menu)
					menu = false;
				else
					menu = true;
			}
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