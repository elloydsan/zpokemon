package org.zengine;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.pokemon.GameConstants;
import org.zengine.graphics.GameCanvas;
import org.zengine.graphics.Render;

/**
 * 
 * @author Troy
 *
 */
public class GameFrame extends JFrame implements ComponentListener{
	private static final long serialVersionUID = -2508508456984008132L;
	
	/**
	 * Construct a new game frame.
	 * 
	 * @param title
	 * @param game
	 */
	public GameFrame(String title, Game game){
		Constants.setGame(game);
		this.setTitle(title);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(GameFrame.class.getResource("/resources/icons/defaulticon.png")));
		this.setSize(Constants.getWidth(), Constants.getHeight());
		this.setPreferredSize(new Dimension(Constants.getPreferedWidth(), Constants.getPreferedHeight()));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		Constants.setScreenWidth((int)dim.getWidth());
		Constants.setScreenHeight((int)dim.getHeight());
		
		GameCanvas canvas = new GameCanvas();
		
		Constants.setGameFrame(this);
		Constants.setGameCanvas(canvas);
		Constants.setRender(new Render());
		
		this.addComponentListener(this);
		this.add(canvas);
		this.setVisible(true);
		this.requestFocus();
		
		canvas.init();
	}
	
	/**
	 * Consume the mouse to allow for a custom drawn
	 * mouse on screen.
	 * 
	 * @param consume
	 */
	public void consumeMouse(boolean consume){
		if(consume){
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension dim = toolkit.getBestCursorSize(1, 1);
			BufferedImage cursorImg = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = cursorImg.createGraphics();
			g2d.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
			g2d.clearRect(0, 0, dim.width, dim.height);
			g2d.dispose();
			Cursor hiddenCursor = toolkit.createCustomCursor(cursorImg, new Point(0,0), "hiddenCursor");
			this.setCursor(hiddenCursor);
		}else{
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	/**
	 * Set the gameframe to fullscreen or normal.
	 * 
	 * @param fullscreen
	 */
	public void setFullscreen(boolean fullscreen){
		Constants.setFullscreen(fullscreen);
		this.dispose();
		
		/**
		 * Just my luck, I write all this bloody code 
		 * then find out hey... there is a built in method 
		 * to do that..
		 * 
		 * It appears that my code seems to work better...
		 * This can't be right... but with multiple monitors
		 * the in built method seems to fail hard core...
		 */
		if(fullscreen){
			Constants.setWidth(Constants.getScreenWidth());
			Constants.setHeight(Constants.getScreenHeight());
			Constants.getGameCanvas().setClip(new Rectangle(-100, -100, Constants.getWidth() +100, Constants.getHeight() +100));
			
			if(GameConstants.getTilemap() != null){
				GameConstants.getTilemap().setxOffSet(GameConstants.getTilemap().getxOffSet() + 
						(((double)Constants.getWidth() - (double)Constants.getOldWidth()) * (double)0.5));
				
				GameConstants.getTilemap().setyOffSet((double)GameConstants.getTilemap().getyOffSet() + 
						(((double)Constants.getHeight() - (double)Constants.getOldHeight()) * (double)0.5));
			}
			
			if(GameConstants.getMinimap() != null){
				GameConstants.getMinimap().setxOffSet(GameConstants.getMinimap().getxOffSet() + 
						((((double)Constants.getWidth() - (double)Constants.getOldWidth()) * (double)0.5) /GameConstants.getMinimap().getScale()));
				GameConstants.getMinimap().setyOffSet(GameConstants.getMinimap().getyOffSet() + 
						((((double)Constants.getHeight() - (double)Constants.getOldHeight()) * (double)0.5) /GameConstants.getMinimap().getScale()));
			}
					
			this.setSize(Constants.getScreenWidth(), Constants.getScreenHeight());
			this.setLocation(0, 0);
		}else{
			Constants.setWidth(Constants.getPreferedWidth());
			Constants.setHeight(Constants.getPreferedHeight());
			Constants.getGameCanvas().setClip(new Rectangle(-100, -100, Constants.getWidth() +100, Constants.getHeight() +100));
			
			if(GameConstants.getTilemap() != null){
				GameConstants.getTilemap().setxOffSet(GameConstants.getTilemap().getxOffSet() + 
						(((double)Constants.getWidth() - (double)Constants.getOldWidth()) * (double)0.5));
				
				GameConstants.getTilemap().setyOffSet((double)GameConstants.getTilemap().getyOffSet() + 
						(((double)Constants.getHeight() - (double)Constants.getOldHeight()) * (double)0.5));
			}
			
			if(GameConstants.getMinimap() != null){
				GameConstants.getMinimap().setxOffSet(GameConstants.getMinimap().getxOffSet() + 
						((((double)Constants.getWidth() - (double)Constants.getOldWidth()) * (double)0.5) /GameConstants.getMinimap().getScale()));
				GameConstants.getMinimap().setyOffSet(GameConstants.getMinimap().getyOffSet() + 
						((((double)Constants.getHeight() - (double)Constants.getOldHeight()) * (double)0.5) /GameConstants.getMinimap().getScale()));
			}
			
			this.setSize(Constants.getPreferedWidth(), Constants.getPreferedHeight());
			this.setLocationRelativeTo(null);
		}
			
		this.setUndecorated(fullscreen);
		/**
		 * Funny enough, my code seems to work better then
		 * the built in method. How bizzar...
		 */
		//Constants.getGameCanvas().setFullscreen(this, fullscreen);
		this.setVisible(true);
	}

	@Override
	public void componentResized(ComponentEvent e) {
		if(getWidth() < Constants.getMinimumWidth() && getHeight() < Constants.getMinimumHeight()){
			this.setSize(Constants.getMinimumWidth(), Constants.getMinimumHeight());
		}
		
		if(!Constants.isFullscreen()){
			while(getWidth()%Constants.getTileWidth()!=0)
				this.setSize(getWidth() -1, getHeight());
				
			while(getHeight()%Constants.getTileHeight()!=0)
				this.setSize(getWidth(), getHeight() -1);
		}
			
		Constants.setWidth(getWidth());
		Constants.setHeight(getHeight());
		Constants.getGameCanvas().setClip(new Rectangle(-100, -100, Constants.getWidth() +100, Constants.getHeight() +100));
			
		if(GameConstants.getTilemap() != null){
			GameConstants.getTilemap().setxOffSet(GameConstants.getTilemap().getxOffSet() + 
					(((double)Constants.getWidth() - (double)Constants.getOldWidth()) * (double)0.5));
			
			GameConstants.getTilemap().setyOffSet((double)GameConstants.getTilemap().getyOffSet() + 
					(((double)Constants.getHeight() - (double)Constants.getOldHeight()) * (double)0.5));
		}
		
		if(GameConstants.getMinimap() != null){
			GameConstants.getMinimap().setxOffSet(GameConstants.getMinimap().getxOffSet() + 
					((((double)Constants.getWidth() - (double)Constants.getOldWidth()) * (double)0.5) /GameConstants.getMinimap().getScale()));
			GameConstants.getMinimap().setyOffSet(GameConstants.getMinimap().getyOffSet() + 
					((((double)Constants.getHeight() - (double)Constants.getOldHeight()) * (double)0.5) /GameConstants.getMinimap().getScale()));
		}
			
		Constants.getGameCanvas().setSize(getWidth(), getHeight());
	}

	public void componentHidden(ComponentEvent arg0) {}
	public void componentMoved(ComponentEvent arg0) {}
	public void componentShown(ComponentEvent arg0) {}

}