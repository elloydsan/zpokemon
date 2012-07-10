package org.pokemon;

/**
 * 
 * @author Troy
 *
 */
public class Tile {
	private byte state;
	private byte interactive;
	private short image;
	
	/**
	 * Create a new tile.
	 * 
	 * @param x
	 * @param y
	 * @param state
	 * @param img
	 * @param interactive
	 */
	public Tile(byte state, byte interactive, short img){
		this.state = state;
		this.interactive = interactive;
		this.image = img;
	}

	public byte getState() {
		return state;
	}

	public byte getInteractive() {
		return interactive;
	}

	public int getImage() {
		return image;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public void setInteractive(byte interactive) {
		this.interactive = interactive;
	}

	public void setImage(short image) {
		this.image = image;
	}

}
