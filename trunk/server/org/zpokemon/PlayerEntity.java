package org.zpokemon;


/**
 * 
 * @author Fsig, NerdyGnome
 * 
 * The player class, all player details to be stored
 *
 */
public class PlayerEntity extends Entity {
	private short speed;
	private int coins;	
	private boolean onBike;
	private boolean moving;
	private boolean inGrass;
	
	/**
	 * Construct a new player.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param delta
	 * @param startcoins
	 */
	public PlayerEntity(short id, double x, double y, byte direction, byte animation){
		super(x,y,direction,animation);
		super.setId(id);
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

	public boolean isInGrass() {
		return inGrass;
	}

	public void setInGrass(boolean inGrass) {
		this.inGrass = inGrass;
	}

}
