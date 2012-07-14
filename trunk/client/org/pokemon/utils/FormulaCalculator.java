package org.pokemon.utils;

import org.pokemon.PokemonEntity;
import org.zengine.uils.Random;

public class FormulaCalculator {
	
	private static final byte BATTLE_EXPERIENCE_CONSTANT = 7;
	
	/*
	 * PLACEHOLDERS, REMOVE THESE WHEN WE KNOW THE REAL ID'S
	 * OF THE BALL.
	 */
	private static final byte POKE_BALL = 1;
	private static final byte GREAT_BALL = 2;
	private static final byte ULTRA_BALL = 3;
	private static final byte MASTER_BALL = 4;
	
	private static Random random = new Random();
	private static byte nrOfStepsInGrass = 0;
	
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
	
	public static boolean isPokemonCaught(PokemonEntity opponentPokemon, short pokeballID, boolean isAsleepOrFrozen, boolean isParBurnOrPois) {
		
		short r1 = 0, r2, r3 = 0, hpFactor, currentHp;
		byte maxHpDivider = 0, pokemonStatus = 0;
		
		// A pokemon is always caught when a master ball is used
		if(pokeballID == MASTER_BALL)
			return true;
		
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
				maxHpDivider = 8;
				break;	
			}	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(isAsleepOrFrozen)
			pokemonStatus = 25;
		else if(isParBurnOrPois)
			pokemonStatus = 12;
		else
			pokemonStatus = 0;
		
		r2 = (short) (r1-pokemonStatus);
		
		//The pokemon is caught
		if(r2 < 0)
			return true;
		
		// This should be the pokemon's max hp
		hpFactor = (short) (opponentPokemon.getHp() * 255);
		hpFactor /= maxHpDivider;
		
		// This should be the pokemon's current hp
		currentHp = (short) (opponentPokemon.getHp() / 4);
		if(currentHp > 0) {
			hpFactor /= currentHp;			
			if(hpFactor > 255)
				hpFactor = 255;			
		}
		
		//Pokemon breaks free
		if(opponentPokemon.getCaptureRate() < r2)
			return false;
		
		try {
			r3 = (short) random.random(0, 255);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Pokemon is caught
		if(r3 <= hpFactor)
			return true;
		
		/*
		 * SET A FEW VARIABLES LIKE NOROFSHAKES AND WHAT STRING TO ANIMATE WHEN THE CAPTURING FAILS
		 */
			
		//If everything else failed, the pokemon escapes.
		return false;
	}
	
	//REPLACE ENCOUNTERINGRATE WITH ENCOUNTERING RATE OF THE POKEMON.
	//ADD ER TO THE POKEMON DATA FILES WHEN YOU GOT TIME
	
	/**
	 * Calculates whether we should generate a pokemon for the player to encounter.
	 * Returns a boolean depending on the outcome, it does not generate a pokemon.
	 * 
	 * @return
	 */
	public static boolean isPokemonEncountered() {
		
		byte encounteringRate, randomNumber = 0;
		short totalEncounteringChance;
		
		// 10 is the ER for very common pokemon's. Remove this line after you've added ER to data files
		encounteringRate = 10;
		
		//Formula for calculating the chance of encountering a pokemon
		totalEncounteringChance = (short) (180/encounteringRate - getNrOfStepsInGrass());
				
		try {
			randomNumber = (byte)random.random(0, totalEncounteringChance);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// The formula states that it is 1 in totalEncounteringChance chance that we encounter a pokemon.
		if(randomNumber == 1) {
			setNrOfStepsInGrass((byte)0);
			return true;
		}
						
		return false;
	}
	
	public static byte getNrOfStepsInGrass() {
		return nrOfStepsInGrass;
	}

	public static void setNrOfStepsInGrass(byte nrOfStepsInGrass) {
		FormulaCalculator.nrOfStepsInGrass = nrOfStepsInGrass;
	}
	
}