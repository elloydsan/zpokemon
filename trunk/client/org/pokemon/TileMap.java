package org.pokemon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import org.zengine.Constants;
import org.zengine.graphics.Paintable;

/**
 * 
 * @author Troy
 * 
 * The TileMap class currently supports 3 layers.
 * These layers are generally as follows:
 * 1: Background / base tiles.
 * 2: Overlay tiles to blend tiles together.
 * 3: Objects.
 * 
 * I am still trying to work out a efficient way
 * to render the massive map without having to
 * irritate over all the tiles.
 *
 */
public class TileMap implements Paintable{
	private String name;
	private double xOffSet;
	private double yOffSet;
	private short tileCols;
	private short tileRows;
	private short tileWidth;
	private short tileCount;
	private short tileHeight;
	private Rectangle viewport;
	private Tile[][] layer1;
	private Tile[][] layer2;
	private Tile[][] layer3;
	
	/**
	 * Create a new TileMap.
	 * 
	 * The TileMap class currently supports 3 layers.
	 * These layers are generally as follows:
	 * 1: Background / base tiles.
	 * 2: Overlay tiles to blend tiles together.
	 * 3: Objects.
	 * 
	 * @param name
	 * @param tileCols
	 * @param tileRows
	 * @param tileWidth
	 * @param tileHeight
	 * @param startX
	 * @param startY
	 */
	public TileMap(String name, short tileCols, short tileRows, short tileWidth, short tileHeight, double startX, double startY){
		this.name = name;
		this.tileCols = tileCols;
		this.tileRows = tileRows;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.xOffSet = startX;
		this.yOffSet = startY;
		
		tileCount = (short) (tileCols * tileRows);
		layer1 = new Tile[tileCols][tileRows];
		layer2 = new Tile[tileCols][tileRows];
		layer3 = new Tile[tileCols][tileRows];
		
		for(short a = 0; a < tileRows; a++){
			for(short b = 0; b < tileCols; b++){
				layer1[b][a] = new Tile((byte)0, (byte)0, (short)0);	
				layer2[b][a] = new Tile((byte)0, (byte)0, (short)0);
				layer3[b][a] = new Tile((byte)0, (byte)0, (short)0);
			}
		}
		
		//The view port will render 3 extra tiles that are off the screen.
		viewport = new Rectangle(-(this.tileWidth * 3), 
				-(this.tileHeight * 3), 
				Constants.getWidth() +(this.tileWidth * 3), 
				Constants.getHeight() +(this.tileHeight * 3));
		
		System.out.println("Viewport: " + viewport.getX() + "," + viewport.getY() + "," + viewport.getWidth() + "," + viewport.getHeight());
		
		//Set some water in the top left hand corner.
		/*layer1[0][0].setImage((short) 134);
		layer1[1][0].setImage((short) 134);
		layer1[2][0].setImage((short) 134);
		layer1[3][0].setImage((short) 134);
		layer1[4][0].setImage((short) 134);
		layer1[5][0].setImage((short) 134);
		
		layer1[0][0].setState((byte) 1);
		layer1[1][0].setState((byte) 1);
		layer1[2][0].setState((byte) 1);
		layer1[3][0].setState((byte) 1);
		layer1[4][0].setState((byte) 1);
		layer1[5][0].setState((byte) 1);
		
		layer2[0][1].setImage((short) 2);
		layer3[1][1].setImage((short) 4);
		
		layer2[0][1].setState((byte) 1);
		layer3[1][1].setState((byte) 1);
		
		//And add in some fences at the corners of the map.
		layer1[tileCols-1][0].setImage((short) 3);
		layer1[0][tileRows-1].setImage((short) 4);
		layer1[tileCols-1][tileRows-1].setImage((short) 5);
		
		layer1[tileCols-1][0].setState((byte) 1);
		layer1[0][tileRows-1].setState((byte) 1);
		layer1[tileCols-1][tileRows-1].setState((byte) 1);
		
		
		
		layer2[10][tileRows-1].setImage((short) 16);
		layer2[11][tileRows-1].setImage((short) 16);
		layer2[12][tileRows-1].setImage((short) 16);
		layer2[13][tileRows-1].setImage((short) 16);
		layer2[14][tileRows-1].setImage((short) 16);
		layer2[15][tileRows-1].setImage((short) 16);
		
		layer2[10][tileRows-2].setImage((short) 15);
		layer2[11][tileRows-2].setImage((short) 15);
		layer2[12][tileRows-2].setImage((short) 15);
		layer2[13][tileRows-2].setImage((short) 15);
		layer2[14][tileRows-2].setImage((short) 15);
		layer2[15][tileRows-2].setImage((short) 15);
		
		layer2[10][tileRows-3].setImage((short) 15);
		layer2[11][tileRows-3].setImage((short) 15);
		layer2[12][tileRows-3].setImage((short) 15);
		layer2[13][tileRows-3].setImage((short) 15);
		layer2[14][tileRows-3].setImage((short) 15);
		layer2[15][tileRows-3].setImage((short) 15);*/
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getxOffSet() {
		return xOffSet;
	}

	public void setxOffSet(double d) {
		this.xOffSet = d;
	}

	public double getyOffSet() {
		return yOffSet;
	}

	public void setyOffSet(double yOffSet) {
		this.yOffSet = yOffSet;
	}

	public short getTileCols() {
		return tileCols;
	}

	public void setTileCols(short tileCols) {
		this.tileCols = tileCols;
	}

	public short getTileRows() {
		return tileRows;
	}

	public void setTileRows(short tileRows) {
		this.tileRows = tileRows;
	}

	public short getTileWidth() {
		return tileWidth;
	}

	public void setTileWidth(short tileWidth) {
		this.tileWidth = tileWidth;
	}

	public short getTileCount() {
		return tileCount;
	}

	public void setTileCount(short tileCount) {
		this.tileCount = tileCount;
	}

	public short getTileHeight() {
		return tileHeight;
	}

	public void setTileHeight(short tileHeight) {
		this.tileHeight = tileHeight;
	}

	public Rectangle getViewport() {
		return viewport;
	}

	public void setViewport(Rectangle viewport) {
		this.viewport = viewport;
	}
	
	public Tile[][] getLayer1() {
		return layer1;
	}

	public void setLayer1(Tile[][] layer1) {
		this.layer1 = layer1;
	}

	public Tile[][] getLayer2() {
		return layer2;
	}

	public void setLayer2(Tile[][] layer2) {
		this.layer2 = layer2;
	}

	public Tile[][] getLayer3() {
		return layer3;
	}

	public void setLayer3(Tile[][] layer3) {
		this.layer3 = layer3;
	}

	@Override
	public void render(Graphics2D g) {
		if(Constants.getWidth() > viewport.getWidth()){
			viewport = new Rectangle(-(this.tileWidth * 3), 
					-(this.tileHeight * 3), 
					Constants.getWidth() + (this.tileWidth * 3), 
					Constants.getHeight() + (this.tileHeight * 3));
			
			//System.out.println("Updating viewport: " + viewport.getX() + "," + viewport.getY() + "," + viewport.getWidth() + "," + viewport.getHeight());
		}
		
		g.setColor(Color.WHITE);
		
		/**
		 * Draw the map.
		 */
		try{
			
		for(short a = 0; a < tileRows; a++){
			for(short b = 0; b < tileCols; b++){
				if(viewport.contains(new Point((b * tileWidth) + (int)xOffSet, 
												(a * tileHeight) + (int)yOffSet))){
					g.drawImage(GameConstants.getTileImages()[layer1[b][a].getImage()], 
							(b * tileWidth) + (int)xOffSet, 
							(a * tileHeight) + (int)yOffSet, 
							tileWidth, 
							tileHeight, 
							null);
					
					if(layer2[b][a].getImage() > 0){
						g.drawImage(GameConstants.getTileImages()[layer2[b][a].getImage()], 
								(b * tileWidth) + (int)xOffSet, 
								(a * tileHeight) + (int)yOffSet, 
								tileWidth, 
								tileHeight, 
								null);
					}
					
					if(layer3[b][a].getImage() > 0){
						g.drawImage(GameConstants.getTileImages()[layer3[b][a].getImage()], 
								(b * tileWidth) + (int)xOffSet, 
								(a * tileHeight) + (int)yOffSet, 
								tileWidth, 
								tileHeight, 
								null);
					}
				}
			}
		}
		
		}catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
	}

}