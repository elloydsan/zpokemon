package org.pokemon.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.pokemon.Move;

/**
 * Class for loading in pokemon moves into memory.
 * 
 * @author NerdyGnome
 *
 */
public class MoveLoader {
	private static final MoveLoader instance = new MoveLoader();
	private static BufferedReader input;
	private static ArrayList<Move> moves = new ArrayList<Move>(4);
	
	private static String name;
	private static byte type;
	private static byte moveCategory;
	private static byte pp; 
	private static short power; 
	private static short accuracy; 
	private static short hpCost; 
	private static short effect; 
	private static boolean isDamageDealing;
	
	/**
	 * Loads all given move files into memory. The available files range from 0.move to 163.move
	 * 
	 * @param moves
	 */
	public static void load(byte[] moves) {
		
		try {
			byte lineCount = 0;
			short currentMove;
			String line = null;
		
			for(int i = 0 ; i < moves.length ; i++) {
				
				currentMove = moves[i];	
				input = new BufferedReader(new InputStreamReader(instance.getClass().getClassLoader().getResourceAsStream("resources/data/moves/" + currentMove + ".move")));
			
				try {
					while ((line = input.readLine()) != null) {
						switch(lineCount) {
						case 0: 
							MoveLoader.name = line;
							break;
						case 1:
							MoveLoader.type = Byte.parseByte(line);
							break;
						case 2:
							MoveLoader.moveCategory = Byte.parseByte(line);
							break;
						case 3:
							MoveLoader.pp = Byte.parseByte(line);
							break;
						case 4:
							MoveLoader.power = Short.parseShort(line);
							break;
						case 5:
							MoveLoader.accuracy = Short.parseShort(line);
							break;
						case 6:
							MoveLoader.hpCost = Short.parseShort(line);
							break;
						case 7:
							MoveLoader.effect = Short.parseShort(line);
							break;
						case 8:
							MoveLoader.isDamageDealing = Boolean.parseBoolean(line);
							break;
						}
						lineCount++;				
					}
				}finally {
					input.close();
				}					
				MoveLoader.moves.add(new Move(name,type,moveCategory,pp,power,accuracy,hpCost,effect,isDamageDealing));		
			}
		}catch (IOException e){
			 e.printStackTrace();
		}
	}
	
	/**
	 * Returns an arraylist of all moves that are loaded into memory
	 * @return
	 */
	public static ArrayList<Move> getAllMoves() {
		return MoveLoader.moves;
	}

	/**
	 * Returns the move given by the parameter move.
	 * If it does not exist, null is returned.
	 * 
	 * @param move
	 * @return 
	 */
	public static Move getMove(String move) {	
		
		for(Move m : MoveLoader.moves)
			if(m.getName().equals(move))
				return m;
		return null;			
	}	
}
