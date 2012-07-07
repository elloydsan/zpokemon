package org.pokemon;

import org.zengine.TileMap;

/**
 * 
 * @author Troy
 *
 */
public class GameConstants {
	private static TileMap tilemap;
	private static PlayerEntity player;

	public static TileMap getTilemap() {
		return tilemap;
	}

	public static void setTilemap(TileMap tilemap) {
		GameConstants.tilemap = tilemap;
	}
	
	public static PlayerEntity getPlayer() {
		return player;
	}

	public static void setPlayer(PlayerEntity player) {
		GameConstants.player = player;
	}

	/**
	 * Check if a tile is walkable.
	 * 
	 * @param x
	 * @param y
	 * @return boolean
	 */
	public static boolean isTileFree(int x, int y){
		if(x < 0 || x >= tilemap.getTileCols()) return false;
		if(y < 0 || y >= tilemap.getTileRows()) return false;
		
		return (tilemap.getLayer1()[x][y].getState() == 0);
	}

}
