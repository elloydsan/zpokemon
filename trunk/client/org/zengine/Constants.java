package org.zengine;

import org.zengine.graphics.GameCanvas;
import org.zengine.graphics.Render;


/**
 * 
 * @author Troy
 *
 */
public class Constants {
	private static Game game;
	
	private static int width = 800;
	private static int height = 600;
	private static int oldWidth = 800;
	private static int oldHeight = 600;
	private static int preferedWidth = 800;
	private static int preferedHeight = 600;
	private static int screenWidth = 800;
	private static int screenHeight = 600;
	
	private static int tileWidth = 20;
	private static int tileHeight = 20;
	
	private static GameFrame gameFrame;
	private static GameCanvas gameCanvas;
	private static Render render;
	
	private static boolean fullscreen = false;
	
	
	public static Game getGame() {
		return game;
	}

	public static void setGame(Game game) {
		Constants.game = game;
	}

	public static int getWidth() {
		return width;
	}
	
	public static void setWidth(int width) {
		if(Constants.width != Constants.oldWidth)
			Constants.oldWidth = Constants.width;
		
		Constants.width = width;
	}
	
	public static int getHeight() {
		return height;
	}
	
	public static void setHeight(int height) {
		if(Constants.height != Constants.oldHeight)
			Constants.oldHeight = Constants.height;
		
		Constants.height = height;
	}

	public static int getOldWidth() {
		return oldWidth;
	}

	public static void setOldWidth(int oldWidth) {
		Constants.oldWidth = oldWidth;
	}

	public static int getOldHeight() {
		return oldHeight;
	}

	public static void setOldHeight(int oldHeight) {
		Constants.oldHeight = oldHeight;
	}

	public static int getPreferedWidth() {
		return preferedWidth;
	}

	public static int getPreferedHeight() {
		return preferedHeight;
	}

	public static void setPreferedWidth(int preferedWidth) {
		Constants.preferedWidth = preferedWidth;
	}

	public static void setPreferedHeight(int preferedHeight) {
		Constants.preferedHeight = preferedHeight;
	}

	public static int getScreenWidth() {
		return screenWidth;
	}

	public static int getScreenHeight() {
		return screenHeight;
	}

	public static void setScreenWidth(int screenWidth) {
		Constants.screenWidth = screenWidth;
	}

	public static void setScreenHeight(int screenHeight) {
		Constants.screenHeight = screenHeight;
	}

	public static GameFrame getGameFrame() {
		return gameFrame;
	}

	public static int getTileWidth() {
		return tileWidth;
	}

	public static int getTileHeight() {
		return tileHeight;
	}

	public static void setTileWidth(int tileWidth) {
		Constants.tileWidth = tileWidth;
	}

	public static void setTileHeight(int tileHeight) {
		Constants.tileHeight = tileHeight;
	}

	public static void setGameFrame(GameFrame gameFrame) {
		Constants.gameFrame = gameFrame;
	}

	public static GameCanvas getGameCanvas() {
		return gameCanvas;
	}

	public static void setGameCanvas(GameCanvas gameCanvas) {
		Constants.gameCanvas = gameCanvas;
	}

	public static Render getRender() {
		return render;
	}

	public static void setRender(Render render) {
		Constants.render = render;
	}

	public static boolean isFullscreen() {
		return fullscreen;
	}

	public static void setFullscreen(boolean fullscreen) {
		Constants.fullscreen = fullscreen;
	}

}