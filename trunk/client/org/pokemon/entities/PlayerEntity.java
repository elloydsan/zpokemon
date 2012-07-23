package org.pokemon.entities;

import java.awt.Color;
import java.awt.Graphics2D;

import org.pokemon.GameConstants;
import org.zengine.Constants;


/**
 * 
 * @author Fsig, NerdyGnome
 * 
 * The player class, all player details to be stored
 * 
 * I can't really think of a better way to animate our
 * player while he moves.
 * 
 * So if you have any ideas please let me know.
 *
 */
public class PlayerEntity extends Entity {
	private short drawX;
	private short drawY;
	private short speed;
	private byte pixelJump;
	private byte tickCount;
	private boolean changeAnimation;
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
	public PlayerEntity(double x, double y, short width, short height, byte delta, byte animation, short speed, int startCoins){
		super(x,y,width,height,delta,animation);
		this.drawX = (short) ((Constants.getWidth()/2) -30);
		this.drawY = (short) ((Constants.getHeight()/2) -50);
		this.speed = speed;
		this.pixelJump = 4;
		this.tickCount = 1;
		this.coins = startCoins;
		this.onBike = false;
		this.moving = false;
		this.inGrass = false;
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

	/**
	 * Move the player if needed.
	 */
	public void move(boolean up, boolean down, boolean left, boolean right, long delta){
		if(tickCount > 30)
			tickCount = 1;
		
		if(tickCount%5==0)
			changeAnimation = true;
		
		/**
		 * Move our player
		 */
		if(up && super.getY() >= 0 &&
				GameConstants.isTileFree((int)(super.getX() / GameConstants.getTilemap().getTileWidth()), (int) (super.getY() - pixelJump) / GameConstants.getTilemap().getTileHeight())){
			
			super.setY(super.getY() - pixelJump);
			GameConstants.getTilemap().setyOffSet(GameConstants.getTilemap().getyOffSet() + pixelJump);
			
			if(changeAnimation){
				if(super.getAnimation() == 6)
					super.setAnimation((byte) 7);
				else if(super.getAnimation() == 7)
					super.setAnimation((byte) 8);
				else if(super.getAnimation() == 8)
					super.setAnimation((byte) 7);
				
				changeAnimation = false;
			}
			
			if(GameConstants.isMultiplayer())
				GameConstants.getPacketManager().movePlayer();
		}
			
		else if(down && super.getY() <= (GameConstants.getTilemap().getTileRows() * GameConstants.getTilemap().getTileHeight()) - GameConstants.getTilemap().getTileHeight() &&
				GameConstants.isTileFree((int)(super.getX() / GameConstants.getTilemap().getTileWidth()), (int) (super.getY() + GameConstants.getTilemap().getTileHeight()) / GameConstants.getTilemap().getTileHeight())){

			super.setY(super.getY() + pixelJump);
			GameConstants.getTilemap().setyOffSet(GameConstants.getTilemap().getyOffSet() - pixelJump);
			
			if(changeAnimation){
				if(super.getAnimation() == 0)
					super.setAnimation((byte) 1);
				else if(super.getAnimation() == 1)
					super.setAnimation((byte) 2);
				else if(super.getAnimation() == 2)
					super.setAnimation((byte) 0);
				
				changeAnimation = false;
			}
			
			if(GameConstants.isMultiplayer())
				GameConstants.getPacketManager().movePlayer();
		}
			
		else if(left && super.getX() >= 0 &&
				GameConstants.isTileFree((int)((int)super.getX() - pixelJump) / GameConstants.getTilemap().getTileWidth(), (int)super.getY() / GameConstants.getTilemap().getTileHeight())){

			super.setX(super.getX() - pixelJump);
			GameConstants.getTilemap().setxOffSet(GameConstants.getTilemap().getxOffSet() + pixelJump);
			
			if(changeAnimation){
				if(super.getAnimation() == 3)
					super.setAnimation((byte) 4);
				else if(super.getAnimation() == 4)
					super.setAnimation((byte) 3);
				
				changeAnimation = false;
			}
			
			if(GameConstants.isMultiplayer())
				GameConstants.getPacketManager().movePlayer();
		}
			
		else if(right && super.getX() <= (GameConstants.getTilemap().getTileCols() * GameConstants.getTilemap().getTileWidth()) - GameConstants.getTilemap().getTileWidth() &&
				GameConstants.isTileFree((int)(super.getX() + GameConstants.getTilemap().getTileWidth()) / GameConstants.getTilemap().getTileWidth(), (int)super.getY() / GameConstants.getTilemap().getTileHeight())){

			super.setX(super.getX() + pixelJump);
			GameConstants.getTilemap().setxOffSet(GameConstants.getTilemap().getxOffSet() - pixelJump);
			
			if(changeAnimation){
				if(super.getAnimation() == 9)
					super.setAnimation((byte) 10);
				else if(super.getAnimation() == 10)
					super.setAnimation((byte) 9);
				
				changeAnimation = false;
			}
			
			if(GameConstants.isMultiplayer())
				GameConstants.getPacketManager().movePlayer();
		}
		
		/**
		 * Continue moving if we are not on a tile.
		 */
		if(!up && !down && !left && !right){
			switch(super.getDirection()){
			case 0:
				if(super.getY()%GameConstants.getTilemap().getTileHeight()!=0){
					if(super.getY() >= 0 && 
							GameConstants.isTileFree((int)(super.getX() / GameConstants.getTilemap().getTileWidth()), (int) (super.getY() - pixelJump) / GameConstants.getTilemap().getTileHeight())){
						super.setY(super.getY() - pixelJump);
						GameConstants.getTilemap().setyOffSet(GameConstants.getTilemap().getyOffSet() + pixelJump);
						
						if(changeAnimation){
							if(super.getAnimation() == 6)
								super.setAnimation((byte) 7);
							else if(super.getAnimation() == 7)
								super.setAnimation((byte) 8);
							else if(super.getAnimation() == 8)
								super.setAnimation((byte) 7);
							
							changeAnimation = false;
						}
					}else{
						super.setY(super.getY() + pixelJump);
						GameConstants.getTilemap().setyOffSet(GameConstants.getTilemap().getyOffSet() - pixelJump);
						
						if(changeAnimation){
							if(super.getAnimation() == 6)
								super.setAnimation((byte) 7);
							else if(super.getAnimation() == 7)
								super.setAnimation((byte) 8);
							else if(super.getAnimation() == 8)
								super.setAnimation((byte) 7);
							
							changeAnimation = false;
						}
					}
					
					if(GameConstants.isMultiplayer())
						GameConstants.getPacketManager().movePlayer();
				}else if(moving){
					super.setAnimation((byte) 6);
					this.moving = false;
					
					if(GameConstants.isMultiplayer())
						GameConstants.getPacketManager().movePlayer();
				}
				break;
			case 1:
				if(super.getY()%GameConstants.getTilemap().getTileHeight()!=0){
					if(super.getY() <= (GameConstants.getTilemap().getTileRows() * GameConstants.getTilemap().getTileHeight()) - GameConstants.getTilemap().getTileHeight() &&
							GameConstants.isTileFree((int)(super.getX() / GameConstants.getTilemap().getTileWidth()), (int) (super.getY() + pixelJump) / GameConstants.getTilemap().getTileHeight())){
						super.setY(super.getY() + pixelJump);
						GameConstants.getTilemap().setyOffSet(GameConstants.getTilemap().getyOffSet() - pixelJump);
						
						if(changeAnimation){
							if(super.getAnimation() == 0)
								super.setAnimation((byte) 1);
							else if(super.getAnimation() == 1)
								super.setAnimation((byte) 2);
							else if(super.getAnimation() == 2)
								super.setAnimation((byte) 0);
							
							changeAnimation = false;
						}
					}else{
						super.setY(super.getY() - pixelJump);
						GameConstants.getTilemap().setyOffSet(GameConstants.getTilemap().getyOffSet() + pixelJump);
						
						if(changeAnimation){
							if(super.getAnimation() == 0)
								super.setAnimation((byte) 1);
							else if(super.getAnimation() == 1)
								super.setAnimation((byte) 2);
							else if(super.getAnimation() == 2)
								super.setAnimation((byte) 0);
							
							changeAnimation = false;
						}
					}
					
					if(GameConstants.isMultiplayer())
						GameConstants.getPacketManager().movePlayer();
				}else if(moving){
					super.setAnimation((byte) 0);
					this.moving = false;
					
					if(GameConstants.isMultiplayer())
						GameConstants.getPacketManager().movePlayer();
				}
				break;
			case 2:
				if(super.getX()%GameConstants.getTilemap().getTileWidth()!=0){
					if(super.getX() >= 0 &&
							GameConstants.isTileFree((int)((int)super.getX() - pixelJump) / GameConstants.getTilemap().getTileWidth(), (int)super.getY() / GameConstants.getTilemap().getTileHeight())){
						super.setX(super.getX() - pixelJump);
						GameConstants.getTilemap().setxOffSet(GameConstants.getTilemap().getxOffSet() + pixelJump);
						
						if(changeAnimation){
							if(super.getAnimation() == 3)
								super.setAnimation((byte) 4);
							else if(super.getAnimation() == 4)
								super.setAnimation((byte) 3);
							
							changeAnimation = false;
						}
					}else{
						super.setX(super.getX() + pixelJump);
						GameConstants.getTilemap().setxOffSet(GameConstants.getTilemap().getxOffSet() - pixelJump);
						
						if(changeAnimation){
							if(super.getAnimation() == 3)
								super.setAnimation((byte) 4);
							else if(super.getAnimation() == 4)
								super.setAnimation((byte) 3);
							
							changeAnimation = false;
						}
					}
					
					if(GameConstants.isMultiplayer())
						GameConstants.getPacketManager().movePlayer();
				}else if(moving){
					super.setAnimation((byte) 3);
					this.moving = false;
					
					if(GameConstants.isMultiplayer())
						GameConstants.getPacketManager().movePlayer();
				}
				break;
			case 3:
				if(super.getX()%GameConstants.getTilemap().getTileWidth()!=0){
					if(super.getX() <= (GameConstants.getTilemap().getTileCols() * GameConstants.getTilemap().getTileWidth()) - GameConstants.getTilemap().getTileWidth() &&
							GameConstants.isTileFree((int)(super.getX() + pixelJump) / GameConstants.getTilemap().getTileWidth(), (int)super.getY() / GameConstants.getTilemap().getTileHeight())){
						super.setX(super.getX() + pixelJump);
						GameConstants.getTilemap().setxOffSet(GameConstants.getTilemap().getxOffSet() - pixelJump);
						
						if(changeAnimation){
							if(super.getAnimation() == 9)
								super.setAnimation((byte) 10);
							else if(super.getAnimation() == 10)
								super.setAnimation((byte) 9);
							
							changeAnimation = false;
						}
					}else{
						super.setX(super.getX() - pixelJump);
						GameConstants.getTilemap().setxOffSet(GameConstants.getTilemap().getxOffSet() + pixelJump);
						
						if(changeAnimation){
							if(super.getAnimation() == 9)
								super.setAnimation((byte) 10);
							else if(super.getAnimation() == 10)
								super.setAnimation((byte) 9);
							
							changeAnimation = false;
						}
					}
					
					if(GameConstants.isMultiplayer())
						GameConstants.getPacketManager().movePlayer();
				}else if(moving){
					super.setAnimation((byte) 9);
					this.moving = false;
					
					if(GameConstants.isMultiplayer())
						GameConstants.getPacketManager().movePlayer();
				}
				break;
			}
		}
		
		tickCount++;
	}

	@Override
	public void render(Graphics2D g) {
		//On resize update player position.
		if(this.drawX != ((Constants.getWidth()/2) -30)){
			this.drawX = (short) ((Constants.getWidth()/2) -30);
			this.drawY = (short) ((Constants.getHeight()/2) -50);
		}
		
		if(GameConstants.isMultiplayer()){
			g.setColor(Color.RED);
			g.drawString("You: " + super.getId(), drawX, drawY);
		}
		
		if(!inGrass)
		g.drawImage(GameConstants.getPlayerImages()[super.getAnimation()], drawX, drawY, super.getWidth(), super.getHeight(), null);
	}

	public short getDrawX() {
		return drawX;
	}

	public void setDrawX(short drawX) {
		this.drawX = drawX;
	}

	public short getDrawY() {
		return drawY;
	}

	public void setDrawY(short drawY) {
		this.drawY = drawY;
	}

}
