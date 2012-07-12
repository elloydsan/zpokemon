package org.pokemon.utils;

/**
 * 
 * @author NerdyGnome
 *
 */


public class CheckEffectiveness {
	
	// 2D array containing the effectiveness of all types against all other types
	private static double[][] effectiveness = {
		{1,2,1,1,0.5,0.5,0.5,0.5,2,1,1,1,0.5,2,1,0.5,1},
		{1,0.5,1,1,0.5,1,1,2,1,1,1,1,1,2,1,0.5,1},
		{1,1,2,1,1,1,1,1,1,1,1,1,1,1,1,1,0.5,1},
		{1,1,0.5,0.5,1,1,2,1,0.5,0,1,1,1,1,1,1,2},
		{0.5,2,1,1,1,1,0.5,0,1,1,2,2,0.5,0.5,2,2,1},
		{2,1,0.5,1,1,0.5,1,1,2,1,2,1,1,1,0.5,2,0.5},
		{2,1,1,0.5,2,1,1,1,2,1,1,1,1,1,0.5,0.5,1},
		{1,0.5,1,1,1,1,1,2,1,1,1,0,1,2,1,0.5,1},
		{0.5,1,0.5,1,1,0.5,0.5,1,0.5,2,1,1,0.5,1,2,0.5,2},
		{0.5,1,1,2,1,2,0,1,0.5,1,1,1,2,1,2,2,1},
		{1,1,2,1,1,0.5,2,1,2,2,0.5,1,1,1,1,0.5,0.5},
		{1,1,1,1,1,1,1,0,1,1,1,1,1,1,0.5,0.5,1},
		{1,1,1,1,1,1,1,0.5,2,0.5,1,1,0.5,1,0.5,0,1},
		{1,0,1,1,2,1,1,1,1,1,1,1,2,0.5,1,0.5,1},
		{2,1,1,1,0.5,2,2,1,1,0.5,2,1,1,1,1,0.5,1},
		{1,1,1,0.5,1,0.5,1,1,1,1,2,1,1,1,2,0.5,0.5},
		{1,1,0.5,1,1,2,1,1,0.5,2,1,1,1,1,2,1,0.5}
	};
	
	/**
	 * Checks the effectiveness of type1 against type2.
	 * Example: getEffectiveness(idOfFire,idOfWater) 
	 * would result in 0.5 since fire is weak against
	 * water.
	 * 
	 * @param type1
	 * @param type2
	 * @return effectiveness
	 */
	public static double getEffectiveness(byte type1, byte type2) {
		return effectiveness[type1][type2];
	}
		
	

}
