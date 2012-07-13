package org.zengine.uils;

import org.pokemon.PokemonEntity;

public class FormulaCalculator {
	
	private static final byte BATTLE_EXPERIENCE_CONSTANT = 7;
	
	/*
	 * These constants are placeholders. Remove these when we know what 
	 * id the balls have
	 */
	private static final byte POKE_BALL = 1;
	private static final byte GREAT_BALL = 2;
	private static final byte ULTRA_BALL = 3;
	private static final byte MASTER_BALL = 4;
	
	private static Random random = new Random();
	
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
	
	/**
	 * This method will calculate whether the pokemon is caught or not. It will also set a shake variable
	 * that will determine the number of shakes that will be animated. 
	 * 
	 * @param opponentPokemon
	 * @param pokeballID
	 * @param isAsleepOrFrozen
	 * @param isParBurnOrPois
	 * @return
	 */
	public static boolean isPokemonCaught(PokemonEntity opponentPokemon, short pokeballID, boolean isAsleepOrFrozen, 
			boolean isParBurnOrPois) {
		
		short r1 = 0, r2 = 0, r3 = 0, hpFactor, maxHpDivider = 0, currentHp = 0;
		byte pokemonStatus;
		
		// If a master ball is used, the pokemon is always caught
		if(pokeballID == MASTER_BALL)
			return true;
		
		// Generate a random number R1, with a range depending on the ball used
		try {
			switch(pokeballID) {
			case POKE_BALL:
				r1 = (short) random.random(0, 255);
				maxHpDivider = 12;
				break;
			case GREAT_BALL:
				r1 = (short) random.random(0,200);
				maxHpDivider = 8;
				break;
			default:
				r1 = (short) random.random(0,150);
				maxHpDivider = 12;
				break;
			}	
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}

		if(isAsleepOrFrozen)
			pokemonStatus = 25;
		else if(isParBurnOrPois)
			pokemonStatus = 12;
		else
			pokemonStatus = 0;
		
		r2 = (short) (r1 - pokemonStatus);

		// The pokemon is caught
		if(r2 < 0)
			return true;
		
		// This hp should be the Pokemon's max hp
		hpFactor = (short) (opponentPokemon.getHp() * 255);
		hpFactor /= maxHpDivider;
		
		// This should divide the pokemon's current hp with 4. Add a variable for this later(?)
		currentHp = (short) (opponentPokemon.getHp() / 4);
		
		if(currentHp > 0) {			
			hpFactor = (short) (hpFactor / currentHp);
			if(hpFactor > 255)
				hpFactor = 255;		
		}
		
		// If the base catch rate of the Pokémon is less than R*, the Pokémon automatically breaks free
		if(opponentPokemon.getCaptureRate() < r2)
			return false;
		
		try {
			r3 = (short) random.random(0, 255);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(r3 <= hpFactor)
			return true;
		
		// If we reach this, the capture failed. Calculate the number of shakes and string to animate
		return false;
	
	}
	
}