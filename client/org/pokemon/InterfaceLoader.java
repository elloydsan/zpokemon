package org.pokemon;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * 
 * @author NerdyGnome
 * 
 * Reads in the given interface images into memory
 *
 */
public class InterfaceLoader {
	
	private static final String interfacePath = "resources/sprites/interfaces/";
	private static final InterfaceLoader instance = new InterfaceLoader();
	private static ArrayList<BufferedImage> interfaceImages = new ArrayList<BufferedImage>(1);
	private static ArrayList<String> fileNames = new ArrayList<String>(1);
	private static BufferedReader input;
	private static BufferedImage image;
	private static byte lineCount = 0;

	
	public static void load(byte[] interfaceID) {
		
		input = new BufferedReader(new InputStreamReader(instance.getClass().getClassLoader().getResourceAsStream(interfacePath + "interfaces.dat")));
		String line;
		
		/*
		 * Reads in what interface images should be loaded into memory
		 */
		try {
			while((line = input.readLine()) != null) {		
				if(contains(lineCount,interfaceID))
					fileNames.add(line);
					
				lineCount++;		
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		 * Loads all interface images that is in the fileName arraylist
		 */
		
		for(String s : fileNames) {
			try {
				image = ImageIO.read(instance.getClass().getClassLoader().getResource(interfacePath + s));
				interfaceImages.add(image);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}	
	}
	
	private static boolean contains(byte interfaceID, byte[] interfaces) {
		for(int i = 0 ; i < interfaces.length ; i++) 		
			if(interfaces[i] == interfaceID)
				return true;
		
		return false;			
	}
	
	public static ArrayList<BufferedImage> getInterfaceImages() {
		return interfaceImages;
	}
}
