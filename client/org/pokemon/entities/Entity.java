package org.pokemon.entities;

import org.zengine.graphics.Paintable;

/**
 * 
 * @author NerdyGnome, Troy
 * 
 * The character class, which is a superclass for
 * the Player- and NPC-class. 
 *
 */
public abstract class Entity implements Paintable{
	private short id;
	private double x;
	private double y;
	private double dx;
	private double dy;
	private short width;
	private short height;
	private byte direction;
	private byte animation;
	
	/**
	 * Construct a new Entity.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param direction
	 */
	public Entity(double x, double y, short width, short height, byte direction, byte animation) {	
		this.x = x;
		this.y = y;
		this.dx = x;
		this.dy = y;
		this.width = width;
		this.height = height;
		this.direction = direction;	
		this.animation = animation;
	}

	public short getId() {
		return id;
	}

	public void setId(short id) {
		this.id = id;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getDx() {
		return dx;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public double getDy() {
		return dy;
	}

	public void setDy(double dy) {
		this.dy = dy;
	}

	public short getWidth() {
		return width;
	}

	public void setWidth(short width) {
		this.width = width;
	}

	public short getHeight() {
		return height;
	}

	public void setHeight(short height) {
		this.height = height;
	}

	public byte getDirection() {
		return direction;
	}

	public void setDirection(byte direction) {
		this.direction = direction;
	}

	public byte getAnimation() {
		return animation;
	}

	public void setAnimation(byte animation) {
		this.animation = animation;
	}

}