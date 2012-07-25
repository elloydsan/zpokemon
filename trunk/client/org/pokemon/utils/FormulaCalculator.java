package org.pokemon.utils;

import org.pokemon.Move;
import org.pokemon.Pokemon;
import org.pokemon.entities.PokemonEntity;
import org.zengine.uils.Random;

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
	
	private static byte nrOfStepsInGrass = 0;
	
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
	public static double calculateBattleExperience(boolean isPokemonWild, boolean isOwnerOT, short baseExp, 
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
	 * 
	 * @param attackingPokemon
	 * @param move
	 * @param defendingPokemon
	 * @return damage made by move
	 * @throws InterruptedException
	 */
	public static short calculateAttackDamage(PokemonEntity attackingPokemon, Move move, PokemonEntity defendingPokemon) throws InterruptedException {
		
		short minDamage, maxDamage, attack, defense, base, minModifier, maxModifier;
		double effect = 0, stab = 0, other;
		byte critical;
		
		byte pokemonLevel = FormulaCalculator.calculatePokemonLevel(attackingPokemon);
		if(move.getMoveCategory() == 1) {
			attack = attackingPokemon.getSpAttack();
			defense = defendingPokemon.getSpDefense();
		}
		else {
			attack = attackingPokemon.getAttack();
			defense = defendingPokemon.getDefense();
		}	
		base = move.getPower();
		
		stab = FormulaCalculator.calculateStab(attackingPokemon, move);	
		for(int i = 0 ; i < defendingPokemon.getType().length ; i++) 
			effect *= FormulaCalculator.calculateEffectiveness(move.getType(), defendingPokemon.getType()[i]);
		//Add a method for deciding if an attack is critical or not.
		// critical = 2, otherwise 1
		critical = 1;
		other = 1; // other counts for things like held items. Add this in later versions
		
		minModifier = (short) (stab * effect * critical * other * 0.85);
		maxModifier = (short) (stab * effect * critical * other * 1);
		
		minDamage = (short) ((((2 * pokemonLevel + 10)/10) * (attack/defense) * (base + 2)) * minModifier);
		maxDamage = (short) ((((2 * pokemonLevel + 10)/10) * (attack/defense) * (base + 2)) * maxModifier);
	
		return (short) Math.floor(random.random(minDamage, maxDamage));		
	}
	
	//MAYBE MOVE THIS TO POKEMONENTITY? MAKES MORE SENSE
	//THIS FORMULA IS JUST FOR THE MEDIUM FAST GROUP.
	//EITHER IMPLEMENT ALL THE OTHERS OR JUST DO THIS ONE FOR ALL POKEMON
	/**
	 * Transform the pokemon's current xp to a level. It uses a simple bruteforce
	 * formula to find the current level, although this is not a problem since
	 * the max level is 100.
	 * 
	 * @param pokemon
	 * @return pokemon level
	 */
	public static byte calculatePokemonLevel(PokemonEntity pokemon) {		
		byte currentX = 0;
		
		while(pokemon.getExp() - (Math.pow(currentX, 3)) != 0)
			currentX++;		
		return currentX;	
	}
		
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
	public static double calculateEffectiveness(byte type1, byte type2) {
		return effectiveness[type1][type2];
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
	
	/**
	 * Calculates whether an attempt to escape from a pokemon battle is successful or not.
	 * 
	 * @param pokemon
	 * @param opponentPokemon
	 * @param escapeAttempts
	 * @return 
	 */
	public static boolean isEscapeSuccessful(PokemonEntity pokemon, PokemonEntity opponentPokemon, byte escapeAttempts) {
		
		short f = (short) ((pokemon.getSpeed() * 32)/((opponentPokemon.getSpeed()/4)%256) + (30 * ++escapeAttempts));
		if(f > 255)
			return true;
		else {		
			short randomNumber = 0;
			try {
				randomNumber = (short) random.random(0, 255);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(randomNumber < f)
				return true;	
		}	
		return false;	
	}
	
	/**
	 * Calculate the stab value of a move.
	 * 
	 * @param pokemon
	 * @param move
	 * @return stab value
	 */
	private static double calculateStab(PokemonEntity pokemon, Move move) {
		
		double stabValue = 1.0;
		
		for(int i = 0 ; i < pokemon.getType().length ; i++)
			if(pokemon.getType()[i] == move.getType())
				stabValue = 1.5;
		
		return stabValue;
	}
	
	public static byte getNrOfStepsInGrass() {
		return nrOfStepsInGrass;
	}

	public static void setNrOfStepsInGrass(byte nrOfStepsInGrass) {
		FormulaCalculator.nrOfStepsInGrass = nrOfStepsInGrass;
	}
	
}