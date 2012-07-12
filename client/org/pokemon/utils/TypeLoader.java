package org.pokemon.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.pokemon.Type;

/**
 * 
 * @author NerdyGnome
 *
 */
public class TypeLoader {
	private static final TypeLoader instance = new TypeLoader();

	private static final byte NUMBER_OF_TYPES = 17;
	private static byte lineCount = 0;
	private static ArrayList<Type> loadedTypes = new ArrayList<Type>(1);
	private static BufferedReader input;
	
	/**
	 * Loads all given types into memory.
	 * 
	 * @param types
	 */
	public static void load(byte[] types) {
		
		input = new BufferedReader(new InputStreamReader(instance.getClass().getClassLoader().getResourceAsStream("resources/data/types/types.dat")));
		String line = null;																						
		
		try {
			for(line = input.readLine(); lineCount <NUMBER_OF_TYPES ; lineCount++, line = input.readLine()) {		
				if(!contains(lineCount,types))
					continue;
				
				loadedTypes.add(new Type(lineCount,line));	
				System.out.println(line);
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private static boolean contains(byte type, byte[] types) {
		for(int i = 0 ; i < types.length ; i++) 		
			if(types[i] == type)
				return true;
		
		return false;			
	}
	
	/**
	 * Returns all types that are loaded into memory.
	 * 
	 * @return
	 */
	public static ArrayList<Type> getLoadedTypes() {
		return loadedTypes;
	}

	/**
	 * Returns the type that matches typeId, if it exists
	 * in the memory.
	 * 
	 * @param typeId
	 * @return
	 */
	public static Type getLoadedTypes(byte typeId) {
		for(int i = 0 ; i < loadedTypes.size() ; i++) {
			
			if(loadedTypes.get(i).getId() == typeId)
				return loadedTypes.get(i);
		}
		
		System.out.println("ERROR: Type with id: (" + typeId + ") is not loaded into memory");
		return null;
	}
}
