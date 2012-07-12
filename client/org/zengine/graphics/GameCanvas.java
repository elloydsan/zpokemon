package org.zengine.graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
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
		this.graphicsEnviroment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		this.graphicsDevice = graphicsEnviroment.getDefaultScreenDevice();
		
		this.setSize(Constants.getWidth(), Constants.getHeight());
		this.createBufferStrategy(2);
		this.buffer = getBufferStrategy();
	}
	
	/**
	 * The render method, this will be called by Render to display the graphics.
	 * 
	 * @param g
	 * @param backBuffer
	 */
	public void draw(){	
		Graphics g = buffer.getDrawGraphics();  
		g.clearRect(0, 0, getWidth(), getHeight());
		
		/**
		 * Set a clip area that actually covers the 
		 * whole screen and then some.
		 * 
		 * This probably isn't needed but not being to
		 * familiar with the buffer strategy, it seems
		 * like this may save resource 
		 */
		g.setClip(-100, 
				-100, 
				Constants.getWidth() +100, 
				Constants.getHeight() +100);
		 
		//Draw the game's graphics.
		Constants.getGame().draw(g);
		 
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

}