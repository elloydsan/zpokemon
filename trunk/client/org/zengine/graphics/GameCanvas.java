package org.zengine.graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferStrategy;

import org.zengine.Constants;

/**
 * 
 * @author Troy
 *
 */
public class GameCanvas extends Canvas{
	private static final long serialVersionUID = 7086780417047334597L;
	
	private GraphicsDevice graphicsDevice;
	private GraphicsEnvironment graphicsEnviroment;
	private BufferStrategy buffer;
	private Graphics2D g;
	private Rectangle clip = new Rectangle(-100, -100, Constants.getWidth() +100, Constants.getHeight() +100);
	
	public GameCanvas(){
		super();
		this.setBackground(Color.BLACK);
		this.setIgnoreRepaint(true);
		
		this.addMouseListener(Constants.getGame());
		this.addMouseMotionListener(Constants.getGame());
		this.addKeyListener(Constants.getGame());
	}
	
	/**
	 * Initialise variables.
	 */
	public void init(){
		graphicsEnviroment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		graphicsDevice = graphicsEnviroment.getDefaultScreenDevice();
		
		setSize(Constants.getWidth(), Constants.getHeight());
		createBufferStrategy(2);
		buffer = getBufferStrategy();
	}
	
	/**
	 * The render method, this will be called by Render to display the graphics.
	 * 
	 * @param g
	 * @param backBuffer
	 */
	public void draw(){	
		g = (Graphics2D) buffer.getDrawGraphics();  
		/**
		 * Turn antialias on to smooth out ruff edges.
		 * 
		 * Actually, that looks shit...
		 */
		//g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.clearRect(0, 0, getWidth(), getHeight());
		
		/**
		 * Set a clip area that actually covers the 
		 * whole screen and then some.
		 * 
		 * This probably isn't needed but not being to
		 * familiar with the buffer strategy, it seems
		 * like this may save resource 
		 */
		g.setClip(clip);
		 
		//Draw the game's graphics.
		Constants.getGame().render(g);
		 
		//Dispose of the graphics.
		g.dispose();
		 
		// flip/draw the backbuffer to the canvas component.
		if(!buffer.contentsLost())
			buffer.show();
		 
		//Synchronise with the display refresh rate.
		Toolkit.getDefaultToolkit().sync();
	}
	 
	/**
	 * Set the window to full screen.
	 * 
	 * @param window
	 */
	public void setFullscreen(Window window, boolean fullscreen){
		if(fullscreen){
			this.graphicsDevice.setFullScreenWindow(window);
		}else{
			Window w = this.graphicsDevice.getFullScreenWindow();
			if(w != null)
				w.dispose();
			this.graphicsDevice.setFullScreenWindow(null);
		}
	}

	public Rectangle getClip() {
		return clip;
	}

	public void setClip(Rectangle clip) {
		this.clip = clip;
	}

}