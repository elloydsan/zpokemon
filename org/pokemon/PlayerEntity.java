package org.pokemon;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.zengine.Constants;


/**
 * 
 * @author Fsig, NerdyGnome
 * 
 * The player class, all player details to be stored
 *
 */
public class PlayerEntity extends Entity {
	private short drawX;
	private short drawY;
	private short speed;
	private byte pixelJump;
	private byte pcount;
	private int coins;	
	private boolean onBike;
	private boolean moving;
	private BufferedImage[] images; 
	
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
	public PlayerEntity(double x, double y, short width, short height, byte delta, byte animation, short speed, int startCoins, BufferedImage[] images){
		super(x,y,width,height,delta,animation);
		this.drawX = (short) ((Constants.getWidth()/2) -30);
		this.drawY = (short) ((Constants.getHeight()/2) -50);
		this.speed = speed;
		this.pixelJump = 1;
		this.pcount = 0;
		this.coins = startCoins;
		this.images = images;
	}

	public short getSpeed() {
		return speed;
	}

	public void setSpeed(short speed) {
		this.speed = speed;
	}

	public byte getPcount() {
		return pcount;
	}

	public void setPcount(byte pcount) {
		this.pcount = pcount;
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

	public BufferedImage[] getImages() {
		return images;
	}
	
	public void setImages(BufferedImage[] images) {
		this.images = images;
	}
	
	/**
	 * Move the player if needed.
	 */
	public void move(boolean up, boolean down, boolean left, boolean right, long delta){
		/**
		 * Move our player
		 */
		if(up && super.getY() >= 0 &&
				GameConstants.isTileFree((int)(super.getX() / GameConstants.getTilemap().getTileWidth()), (int) (super.getY() - (pixelJump + delta / speed)) / GameConstants.getTilemap().getTileHeight())){
			
			super.setAnimation((byte) 6);
			super.setY(super.getY() - (pixelJump + delta / speed));
			GameConstants.getTilemap().setyOffSet(GameConstants.getTilemap().getyOffSet() + (pixelJump + delta / speed));
		}
			
		else if(down && super.getY() <= (GameConstants.getTilemap().getTileRows() * GameConstants.getTilemap().getTileHeight()) - GameConstants.getTilemap().getTileHeight() &&
				GameConstants.isTileFree((int)(super.getX() / GameConstants.getTilemap().getTileWidth()), (int) (super.getY() + (GameConstants.getTilemap().getTileHeight() + delta / speed)) / GameConstants.getTilemap().getTileHeight())){
			
			super.setAnimation((byte) 0);
			super.setY(super.getY() + (pixelJump + delta / speed));
			GameConstants.getTilemap().setyOffSet(GameConstants.getTilemap().getyOffSet() - (pixelJump + delta / speed));
		}
			
		else if(left && super.getX() >= 0 &&
				GameConstants.isTileFree((int)((int)super.getX() - (pixelJump + delta / speed)) / GameConstants.getTilemap().getTileWidth(), (int)super.getY() / GameConstants.getTilemap().getTileHeight())){
			
			super.setAnimation((byte) 3);
			super.setX(super.getX() - (pixelJump + delta / speed));
			GameConstants.getTilemap().setxOffSet(GameConstants.getTilemap().getxOffSet() + (pixelJump + delta / speed));
		}
			
		else if(right && super.getX() <= (GameConstants.getTilemap().getTileCols() * GameConstants.getTilemap().getTileWidth()) - GameConstants.getTilemap().getTileWidth() &&
				GameConstants.isTileFree((int)(super.getX() + (GameConstants.getTilemap().getTileWidth() + delta / speed)) / GameConstants.getTilemap().getTileWidth(), (int)super.getY() / GameConstants.getTilemap().getTileHeight())){

			super.setAnimation((byte) 9);
			super.setX(super.getX() + (pixelJump + delta / speed));
			GameConstants.getTilemap().setxOffSet(GameConstants.getTilemap().getxOffSet() - (pixelJump + delta / speed));
		}
		
		/**
		 * Continue moving if we are not on a tile.
		 */
		if(!up && !down && !left && !right){
			switch(super.getDirection()){
			case 0:
				//if(super.getY() != super.getDy()){
				if(super.getY()%GameConstants.getTilemap().getTileHeight()!=0){
					if(super.getY() >= 0 && 
							GameConstants.isTileFree((int)(super.getX() / GameConstants.getTilemap().getTileWidth()), (int) (super.getY() - (pixelJump + delta / speed)) / GameConstants.getTilemap().getTileHeight())){
						super.setAnimation((byte) 6);
						super.setY(super.getY() - (pixelJump + delta / speed));
						GameConstants.getTilemap().setyOffSet(GameConstants.getTilemap().getyOffSet() + (pixelJump + delta / speed));
					}else{
						super.setAnimation((byte) 6);
						super.setY(super.getY() + (pixelJump + delta / speed));
						GameConstants.getTilemap().setyOffSet(GameConstants.getTilemap().getyOffSet() - (pixelJump + delta / speed));
					}
				}else{
					this.moving = false;
				}
				break;
			case 1:
				//if(super.getY() != super.getDy()){
				if(super.getY()%GameConstants.getTilemap().getTileHeight()!=0){
					if(super.getY() <= (GameConstants.getTilemap().getTileRows() * GameConstants.getTilemap().getTileHeight()) - GameConstants.getTilemap().getTileHeight() &&
							GameConstants.isTileFree((int)(super.getX() / GameConstants.getTilemap().getTileWidth()), (int) (super.getY() + (pixelJump + delta / speed)) / GameConstants.getTilemap().getTileHeight())){
						super.setAnimation((byte) 0);
						super.setY(super.getY() + (pixelJump + delta / speed));
						GameConstants.getTilemap().setyOffSet(GameConstants.getTilemap().getyOffSet() - (pixelJump + delta / speed));
					}else{
						super.setAnimation((byte) 0);
						super.setY(super.getY() - (pixelJump + delta / speed));
						GameConstants.getTilemap().setyOffSet(GameConstants.getTilemap().getyOffSet() + (pixelJump + delta / speed));
					}
				}else{
					this.moving = false;
				}
				break;
			case 2:
				//if(super.getX() != super.getDx()){
				if(super.getX()%GameConstants.getTilemap().getTileWidth()!=0){
					if(super.getX() >= 0 &&
							GameConstants.isTileFree((int)((int)super.getX() - (pixelJump + delta / speed)) / GameConstants.getTilemap().getTileWidth(), (int)super.getY() / GameConstants.getTilemap().getTileHeight())){
						super.setAnimation((byte) 3);
						super.setX(super.getX() - (pixelJump + delta / speed));
						GameConstants.getTilemap().setxOffSet(GameConstants.getTilemap().getxOffSet() + (pixelJump + delta / speed));
					}else{
						super.setAnimation((byte) 3);
						super.setX(super.getX() + (pixelJump + delta / speed));
						GameConstants.getTilemap().setxOffSet(GameConstants.getTilemap().getxOffSet() - (pixelJump + delta / speed));
					}
				}else{
					this.moving = false;
				}
				break;
			case 3:
				//if(super.getX() != super.getDx()){
				if(super.getX()%GameConstants.getTilemap().getTileWidth()!=0){
					if(super.getX() <= (GameConstants.getTilemap().getTileCols() * GameConstants.getTilemap().getTileWidth()) - GameConstants.getTilemap().getTileWidth() &&
							GameConstants.isTileFree((int)(super.getX() + (pixelJump + delta / speed)) / GameConstants.getTilemap().getTileWidth(), (int)super.getY() / GameConstants.getTilemap().getTileHeight())){
						super.setAnimation((byte) 9);
						super.setX(super.getX() + (pixelJump + delta / speed));
						GameConstants.getTilemap().setxOffSet(GameConstants.getTilemap().getxOffSet() - (pixelJump + delta / speed));
					}else{
						super.setAnimation((byte) 9);
						super.setX(super.getX() - (pixelJump + delta / speed));
						GameConstants.getTilemap().setxOffSet(GameConstants.getTilemap().getxOffSet() + (pixelJump + delta / speed));
					}
				}else{
					this.moving = false;
				}
				break;
			}
		}
	}

	@Override
	public void draw(Graphics g) {
		//On resize update player position.
		if(this.drawX != ((Constants.getWidth()/2) -30)){
			this.drawX = (short) ((Constants.getWidth()/2) -30);
			this.drawY = (short) ((Constants.getHeight()/2) -50);
			
			//Pokemon.tileMap.setxOffSet((short) ((short)(dx / 20) - Pokemon.tileMap.getxOffSet()));
			//Pokemon.tileMap.setyOffSet((short) ((short)(dy / 20) - Pokemon.tileMap.getyOffSet()));
		}
		
		g.drawImage(images[super.getAnimation()], drawX, drawY, super.getWidth(), super.getHeight(), null);
	}

}
