package org.pokemon.interfaces;

import org.zengine.graphics.Paintable;

/**
 * 
 * @author Troy
 * 
 * This is the base of all interface's to be used by
 * Pokemon. You must make your interface extend Interface.
 *
 */
public abstract class Interface implements Paintable {
	private short x;
	private short y;
	private short width;
	private short height;
	private boolean active;
	
	/**
	 * Construct a new Interface.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Interface(short x, short y, short width, short height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.active = false;
	}

	public short getX() {
		return x;
	}

	public void setX(short x) {
		this.x = x;
	}

	public short getY() {
		return y;
	}

	public void setY(short y) {
		this.y = y;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}