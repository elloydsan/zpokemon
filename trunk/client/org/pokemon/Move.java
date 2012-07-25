package org.pokemon;

/**
 * Class for pokemon moves
 * 
 * @author NerdyGnome
 *
 */
public class Move {
	private String name;
	private byte type;
	private byte moveCategory; //0 = physical, 1 = special, 2 = status
	private byte pp; 
	private short power;
	private short accuracy; 
	private short hpCost; 
	private short effect; 
	boolean isDamageDealing; //Boolean to check whether this move does damage or not
	
	/**
	 * Construct a new Move. 
	 * 
	 * @param name
	 * @param type
	 * @param moveClass
	 * @param pp
	 * @param power
	 * @param accuracy
	 * @param hpCost
	 * @param effect
	 * @param isOffensive
	 */
	public Move(String name, byte type, byte moveCategory, byte pp, short power,
			short accuracy, short hpCost, short effect, boolean isDamageDealing) {
		this.name = name;
		this.type = type;
		this.moveCategory = moveCategory;
		this.pp = pp;
		this.power = power;
		this.accuracy = accuracy;
		this.hpCost = hpCost;
		this.effect = effect;
		this.isDamageDealing = isDamageDealing;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getMoveCategory() {
		return moveCategory;
	}

	public void setMoveCategory(byte moveClass) {
		this.moveCategory = moveClass;
	}

	public byte getPp() {
		return pp;
	}

	public void setPp(byte pp) {
		this.pp = pp;
	}

	public short getPower() {
		return power;
	}

	public void setPower(short power) {
		this.power = power;
	}

	public short getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(short accuracy) {
		this.accuracy = accuracy;
	}

	public short getHpCost() {
		return hpCost;
	}

	public void setHpCost(short hpCost) {
		this.hpCost = hpCost;
	}

	public short getEffect() {
		return effect;
	}

	public void setEffect(short effect) {
		this.effect = effect;
	}

	public boolean isDamageDealing() {
		return isDamageDealing;
	}

	public void setOffensive(boolean isDamageDealing) {
		this.isDamageDealing = isDamageDealing;
	}
}
