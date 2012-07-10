package org.pokemon;

import java.awt.Color;
import java.awt.Graphics;


/**
 * 
 * @author Fsig, NerdyGnome
 * 
 * The player class, all player details to be stored
 *
 */
public class OtherPlayerEntity extends Entity {
	private short speed;
	private int coins;	
	private boolean onBike;
	private boolean moving;
	
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
	 * @param startCoins
	 */
	public OtherPlayerEntity(short id, double x, double y, short width, short height, byte delta, byte animation, short speed, int startCoins){
		super(x,y,width,height,delta,animation);
		super.setId(id);
		this.speed = speed;
		this.coins = startCoins;
	}

	public short getSpeed() {
		return speed;
	}

	public void setSpeed(short speed) {
		this.speed = speed;
	}

	public int getCoins() {
		return coins;
	}
	
	public void setCoins(int coins) {
		this.coins = coins;
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
	
	/**
	 * Move the player if needed.
	 */
	public void move(boolean up, boolean down, boolean left, boolean right, long delta){
		//This should only handle the animation.
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawString("Player: " + super.getId(), 
				(int)super.getX() + (int)GameConstants.getTilemap().getxOffSet() -10, 
				(int)super.getY() + (int)GameConstants.getTilemap().getyOffSet() -10);
		
		
		g.drawImage(GameConstants.getPlayerImages()[super.getAnimation()], 
				(int)super.getX() + (int)GameConstants.getTilemap().getxOffSet() -10, 
				(int)super.getY() + (int)GameConstants.getTilemap().getyOffSet() -10, 
				super.getWidth(), 
				super.getHeight(), 
				null);
	}

}