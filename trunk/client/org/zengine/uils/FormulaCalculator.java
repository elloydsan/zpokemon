package org.zengine.uils;

public class FormulaCalculator {
	
	private static final byte BATTLE_EXPERIENCE_CONSTANT = 7;
	
	/**
	 * This method calculates the experience gained from beating a pokemon
	 * in battle. PokemonLevel refers to your own pokemon's level, all others are
	 * about the opponent. NrOfPokemon is the number of your pokemon's that participated
	 * in the battle and have not fainted.
	 * 
	 * @param isPokemonWild
	 * @param isOwnerOT
	 * @param baseExp
	 * @param isHoldingLuckyEgg
	 * @param pokemonLevel
	 * @param nrOfPokemon
	 * @return Experience gained from beating a pokemon in battle
	 */
	public static double getBattleExperience(boolean isPokemonWild, boolean isOwnerOT, short baseExp, 
			boolean isHoldingLuckyEgg, short pokemonLevel, byte nrOfPokemon) {
		
		double a, t, e;
		
		// a is equal to 1 if the fainted Pokémon is wild, and 1.5 if the fainted Pokémon is owned by a Trainer.
		if(isPokemonWild)
			a = 1;
		else
			a = 1.5;
		
		/* t is equal to 1 if the winning Pokémon's OT is its current owner, 
		 * 1.5 if the Pokémon was gained in a domestic trade
		 */
		if(isOwnerOT)
			t = 1;
		else
			t = 1.5;
		
		// e is equal to 1.5 if the winning Pokémon is holding a Lucky Egg, and 1 otherwise.
		if(isHoldingLuckyEgg)
			e = 1.5;
		else
			e = 1;
				
		return (a * t * baseExp * e * pokemonLevel) / (BATTLE_EXPERIENCE_CONSTANT * nrOfPokemon);		
	}
	
	
	
	
	
	
	

}
