package org.pokemon.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.pokemon.TileMap;

/**
 * 
 * @author Troy
 * 
 * Pretty bloody ugly but it makes editing map's easier.
 *
 */
public class MapLoader {
	private static MapLoader instance = new MapLoader();
	private static String line;
	private static String value;
	private static String[] split;
	private static int count = 0;
	
	/**
	 * This will load in a Tile Map from the given file.
	 * 
	 * @param mapname
	 * @return TileMap
	 */
	public static TileMap loadmap(String mapname){
		try{
			BufferedReader input = new BufferedReader(new InputStreamReader(instance.getClass().getClassLoader().getResourceAsStream("resources/data/maps/" + mapname + ".dat"))); 
			String name = "Default";
			short cols = 0;
			short rows = 0;
			short width = 0;
			short height = 0;
			double startX = 0;
			double startY = 0;
			String layer1 = "";
			String layer2 = "";
			String layer3 = "";
			boolean on1 = false;
			boolean on2 = false;
			boolean on3 = false;
	        
	        while((line = input.readLine()) != null){
	        	if(!line.startsWith("/") && line.length() > 3){
	        		if(line.contains(":")){
	        			split = line.split(":");
		        		count ++;
		        		System.out.println(count);
		        		
		        		if(split.length > 1){
			        		if(split[1].contains("/")){
			        			split = split[1].split("/");
			        			value = split[0].trim();
			        		}else{
			        			value = split[1].trim();
			        		}
		        		}else{
		        			value = line;
		        		}
	        		}else{
	        			value = line;
	        		}
	        		
	        		if(line.startsWith("name"))
	        			name = value;
	        		else if(line.startsWith("cols"))
	        			cols = Short.parseShort(value);
	        		else if(line.startsWith("rows"))
	        			rows = Short.parseShort(value);
	        		else if(line.startsWith("width"))
	        			width = Short.parseShort(value);
	        		else if(line.startsWith("height"))
	        			height = Short.parseShort(value);
	        		else if(line.startsWith("start x"))
	        			startX = Short.parseShort(value);
	        		else if(line.startsWith("start y"))
	        			startY = Short.parseShort(value);
	        		else if(line.startsWith("layer 1"))
	        			on1 = true;
	        		else if(line.startsWith("layer 2"))
	        			on2 = true;
	        		else if(line.startsWith("layer 3"))
	        			on3 = true;
	        		
	        		if(value.contains(";")){
	        			if(on1)
	        				on1 = false;
	        			else if(on2)
	        				on2 = false;
	        			else if(on3)
	        				on3 = false;
	        		}
	  
	        		if(on1)
	        			layer1 += line.replace("\r\n", "");
	        		else if(on2)
	        			layer2 += line.replace("\r\n", "");
	        		else if(on3)
	        			layer3 += line.replace("\r\n", "");
	        	}
	        }
	        
	        System.out.println(name + " : " + cols + " : " + rows);
	        TileMap tilemap = new TileMap(name,cols,rows,width,height,startX,startY);
	        
	        /**
	         * Layer 1
	         */
	        split = layer1.split(":");
        	
        	for(int i = 0; i < split.length; i++){
        		if(split[i].contains(",")){
		        	String[] split2 = split[i].split(",");
		        		
		        	int x = Integer.parseInt(split2[0].trim());
		        	int y = Integer.parseInt(split2[1].trim());
		        		
		        	tilemap.getLayer1()[x][y].setState(Byte.parseByte(split2[2].trim()));
		        	tilemap.getLayer1()[x][y].setInteractive(Byte.parseByte(split2[3].trim()));
		        	tilemap.getLayer1()[x][y].setImage(Short.parseShort(split2[4].trim()));
	        	}
        	}
        	
        	/**
        	 * Layer 2
        	 */
	        split = layer2.split(":");
        	
        	for(int i = 0; i < split.length; i++){
        		if(split[i].contains(",")){
		        	String[] split2 = split[i].split(",");
		        		
		        	int x = Integer.parseInt(split2[0].trim());
		        	int y = Integer.parseInt(split2[1].trim());
		        		
		        	tilemap.getLayer2()[x][y].setState(Byte.parseByte(split2[2].trim()));
		        	tilemap.getLayer2()[x][y].setInteractive(Byte.parseByte(split2[3].trim()));
		        	tilemap.getLayer2()[x][y].setImage(Short.parseShort(split2[4].trim()));
	        	}
        	}
        	
        	/**
        	 * Layer 3
        	 */
	        split = layer3.split(":");
        	
        	for(int i = 0; i < split.length; i++){
        		if(split[i].contains(",")){
		        	String[] split2 = split[i].split(",");
		        		
		        	int x = Integer.parseInt(split2[0].trim());
		        	int y = Integer.parseInt(split2[1].trim());
		        		
		        	tilemap.getLayer3()[x][y].setState(Byte.parseByte(split2[2].trim()));
		        	tilemap.getLayer3()[x][y].setInteractive(Byte.parseByte(split2[3].trim()));
		        	tilemap.getLayer3()[x][y].setImage(Short.parseShort(split2[4].trim()));
	        	}
        	}
        	
        	/**
        	 * Clean up.
        	 */
        	line = null;
        	value = null;
        	split = null;
			name = null;
			layer1 = null;
			layer2 = null;
			layer3 = null;
			
        	return tilemap;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

}