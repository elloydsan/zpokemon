package org.pokemon.entities;

import java.awt.Color;
import java.awt.Graphics2D;

import org.pokemon.GameConstants;


/**
 * 
 * @author Fsig, NerdyGnome
 * 
 * The player class, all player details to be stored
 *
 */
public class OtherPlayerEntity extends Entity {
	private short speed;
	private boolean onBike;
	private boolean moving;
	private boolean inGrass;
	
	/**
	 * Construct a new other player.
	 * 
	 * @param id
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param delta
	 * @param animation
	 * @param speed
	 */
	public OtherPlayerEntity(short id, double x, double y, short width, short height, byte delta, byte animation, short speed){
		super(x,y,width,height,delta,animation);
		super.setId(id);
		this.speed = speed;
	}

	public short getSpeed() {
		return speed;
	}

	public void setSpeed(short speed) {
		this.speed = speed;
	}
	
	public boolean isOnBike() {
		return onBike;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setOnBike(boolean onBike) {
		this.onBike = onBike;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	public boolean isInGrass() {
		return inGrass;
	}

	public void setInGrass(boolean inGrass) {
		this.inGrass = inGrass;
	}

	/**
	 * Move the player if needed.
	 */
	public void move(boolean up, boolean down, boolean left, boolean right, long delta){
		//This should only handle the animation.
	}

	@Override
	public void render(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.drawString("Player: " + super.getId(), 
				(int)super.getX() + (int)GameConstants.getTilemap().getxOffSet() -10, 
				(int)super.getY() + (int)GameConstants.getTilemap().getyOffSet() -10);
		
		if(!inGrass)
		g.drawImage(GameConstants.getPlayerImages()[super.getAnimation()], 
				(int)super.getX() + (int)GameConstants.getTilemap().getxOffSet() -10, 
				(int)super.getY() + (int)GameConstants.getTilemap().getyOffSet() -10, 
				super.getWidth(), 
				super.getHeight(), 
				null);
	}

}
