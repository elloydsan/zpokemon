package org.pokemon;

/**
 * 
 * 
 * @author NerdyGnome
 *
 */
public class Type {
	private byte id;
	private String name;
	
	/**
	 * 
	 * @param id
	 * @param name
	 */
	public Type(byte id, String name) {
		this.id = id;
		this.name = name;		
	}

	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
