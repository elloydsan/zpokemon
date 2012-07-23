package org.pokemon.interfaces;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import org.pokemon.GameConstants;
import org.pokemon.Pokemon;
import org.pokemon.entities.OtherPlayerEntity;
import org.zengine.Constants;
import org.zengine.uils.ImageUtils;

/**
 * 
 * @author Troy
 * 
 * Setting the clipping zone takes up a fair bit of
 * memory it must create a new object every time this
 * happens which is about 30 times a second...
 * 
 * To reduce some memory usage in the future we could
 * possible render to a image and then draw the image.
 * 
 * This way we only update the image when needed.
 * 
 * The tricky part with that will be making sure we
 * don't have a update occur in the rendering phase or
 * else we make see black and flickering in the image...
 *
 */
public class MinimapInterface extends Interface{
	private BufferedImage mapborder;
	private BufferedImage sunmoon;
	private Ellipse2D circleClip;
	private Ellipse2D circleRender;
	private Ellipse2D sunmoonClip;
	private short scale;
	private double xOffSet;
	private double yOffSet;
	private double sunmoonX;
	private double sunmoonY;

	public MinimapInterface(short x, short y, short width, short height) {
		super(x, y, width, height);
		this.mapborder = ImageUtils.loadImage("resources/sprites/textures/minimap.png");
		this.sunmoon = ImageUtils.loadImage("resources/sprites/textures/sunmoon.png");

		this.scale = 5;
		this.circleClip = new Ellipse2D.Double();
		this.circleClip.setFrame(super.getX(), super.getY(), super.getWidth() -2, super.getHeight() -2);
		this.circleRender = new Ellipse2D.Double();
		this.circleRender.setFrame(super.getX() -10, super.getY() -10, super.getWidth() +10, super.getHeight() +10);
		
		this.sunmoonX = super.getX() +98;
		this.sunmoonY = super.getY() -23;
		this.sunmoonClip = new Ellipse2D.Double();
		this.sunmoonClip.setFrame(this.sunmoonX, this.sunmoonY +27, sunmoon.getWidth(), sunmoon.getWidth());
	}

	public short getScale() {
		return scale;
	}

	public void setScale(short scale) {
		this.scale = scale;
	}

	public double getxOffSet() {
		return xOffSet;
	}

	public double getyOffSet() {
		return yOffSet;
	}

	public void setxOffSet(double xOffSet) {
		this.xOffSet = xOffSet;
	}

	public void setyOffSet(double yOffSet) {
		this.yOffSet = yOffSet;
	}

	public double getSunmoonX() {
		return sunmoonX;
	}

	public double getSunmoonY() {
		return sunmoonY;
	}

	public void setSunmoonX(double sunmoonX) {
		this.sunmoonX = sunmoonX;
	}

	public void setSunmoonY(double sunmoonY) {
		this.sunmoonY = sunmoonY;
	}

	@Override
	public void render(Graphics2D g) {
		if(super.getX() != (Constants.getWidth() - 155)){
			super.setX((short) (Constants.getWidth() - 155));
			this.circleClip.setFrame(super.getX(), super.getY(), super.getWidth() -2, super.getHeight() -2);
			this.circleRender.setFrame(super.getX() -10, super.getY() -10, super.getWidth() +10, super.getHeight() +10);
			this.sunmoonX = super.getX() +98;
			this.sunmoonClip.setFrame(sunmoonX, (super.getY() -23) + 27, sunmoon.getWidth(), sunmoon.getWidth());
		}
		
		/**
		 * Set a clip area.
		 */
		g.setColor(Color.BLACK);
		g.fill(circleClip);
		g.setClip(circleClip);
		
		/**
		 * Draw the minimap
		 */
		for(int a = 0; a < GameConstants.getTilemap().getTileRows(); a++){
			for(int b = 0; b < GameConstants.getTilemap().getTileCols(); b++){
				if(circleRender.contains(new Point(((super.getX() - 13) - (int)xOffSet) + (b * (GameConstants.getTilemap().getTileWidth() / scale)) + (int)(GameConstants.getTilemap().getxOffSet() / scale), 
						((super.getY() + 11) - (int)yOffSet) + (a * (GameConstants.getTilemap().getTileHeight() / scale)) + (int)(GameConstants.getTilemap().getyOffSet() / scale)))){
					
					g.drawImage(GameConstants.getTileImages()[GameConstants.getTilemap().getLayer1()[b][a].getImage()], 
							((super.getX() - 13) - (int)xOffSet) + (b * (GameConstants.getTilemap().getTileWidth() / scale)) + (int)(GameConstants.getTilemap().getxOffSet() / scale), 
							((super.getY() + 11) - (int)yOffSet) + (a * (GameConstants.getTilemap().getTileHeight() / scale)) + (int)(GameConstants.getTilemap().getyOffSet() / scale),  
							(GameConstants.getTilemap().getTileWidth() / scale), 
							(GameConstants.getTilemap().getTileHeight() / scale), 
							null);
					
					if(GameConstants.getTilemap().getLayer2()[b][a].getImage() > 0){
						g.drawImage(GameConstants.getTileImages()[GameConstants.getTilemap().getLayer2()[b][a].getImage()], 
								((super.getX() - 13) - (int)xOffSet) + (b * (GameConstants.getTilemap().getTileWidth() / scale)) + (int)(GameConstants.getTilemap().getxOffSet() / scale), 
								((super.getY() + 11) - (int)yOffSet) + (a * (GameConstants.getTilemap().getTileHeight() / scale)) + (int)(GameConstants.getTilemap().getyOffSet() / scale),  
								(GameConstants.getTilemap().getTileWidth() / scale), 
								(GameConstants.getTilemap().getTileHeight() / scale), 
								null);
					}
					
					if(GameConstants.getTilemap().getLayer3()[b][a].getImage() > 0){
						g.drawImage(GameConstants.getTileImages()[GameConstants.getTilemap().getLayer3()[b][a].getImage()], 
								((super.getX() - 13) - (int)xOffSet) + (b * (GameConstants.getTilemap().getTileWidth() / scale)) + (int)(GameConstants.getTilemap().getxOffSet() / scale), 
								((super.getY() + 11) - (int)yOffSet) + (a * (GameConstants.getTilemap().getTileHeight() / scale)) + (int)(GameConstants.getTilemap().getyOffSet() / scale),  
								(GameConstants.getTilemap().getTileWidth() / scale), 
								(GameConstants.getTilemap().getTileHeight() / scale), 
								null);
					}
				}
			}
		}
		
		/**
		 * Apply the night effect.
		 */
		g.drawImage(Pokemon.filterEffect, 0, 0, Constants.getWidth(), Constants.getHeight(), null);
		
		/**
		 * If multiplayer, draw other players.
		 */
		if(GameConstants.isMultiplayer()){
			if(GameConstants.getPlayerList() != null)
			for(OtherPlayerEntity p : GameConstants.getPlayerList()){
				g.setColor(Color.WHITE);
				g.fillOval(((super.getX() -12) - (int)xOffSet) + (int)(p.getX() / scale) + (int)((GameConstants.getTilemap().getxOffSet() -10) / scale),
						((super.getY() + 12)  - (int)yOffSet) + (int)(p.getY() / scale) + (int)((GameConstants.getTilemap().getyOffSet() -10) / scale), 
						scale,
						scale);
			}
		}
		
		/**
		 * Draw the sun/moon
		 */
		g.setClip(sunmoonClip);
		g.drawImage(sunmoon,
				(int)sunmoonX,
				(int)sunmoonY, 
				sunmoon.getWidth(), 
				sunmoon.getHeight(), 
				null);
		
		/**
		 * Restore the clip
		 */
		g.setClip(Constants.getGameCanvas().getClip());
		
		/**
		 * Draw the border
		 */
		g.drawImage(mapborder, 
				super.getX(), 
				super.getY(), 
				super.getWidth(), 
				super.getHeight(), 
				null);
	}

}
