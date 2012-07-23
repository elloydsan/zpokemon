package org.zpokemon;


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
 * The server will be in charge of sending chunks
 * of the tile map to the client.
 *
 */
public class TileMap{
	private String name;
	private short startX;
	private short startY;
	private double xOffSet;
	private double yOffSet;
	private short tileCols;
	private short tileRows;
	private short tileWidth;
	private short tileCount;
	private short tileHeight;
	private Tile[][] layer1;
	private Tile[][] layer2;
	private Tile[][] layer3;

	/**
	 * Construct a new TileMap.
	 * 
	 * @param tileCols
	 * @param tileRows
	 * @param tileWidth
	 * @param tileHeight
	 * @param startX
	 * @param startY
	 */
	public TileMap(String name, short tileCols, short tileRows, short tileWidth, short tileHeight, double xoff, double yoff, short startX, short startY){
		this.name = name;
		this.tileCols = tileCols;
		this.tileRows = tileRows;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.startX = startX;
		this.startY = startY;
		this.xOffSet = xoff;
		this.yOffSet = yoff;
		
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
		
		/**
		 * Some filler shite to make sure the map is working.
		 */
		layer1[0][0].setImage((short) 134);
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
		layer2[15][tileRows-3].setImage((short) 15);
		
		/**
		 * Set states
		 */
		/*layer1[10][tileRows-2].setState((byte) 2);
		layer1[11][tileRows-2].setState((byte) 2);
		layer1[12][tileRows-2].setState((byte) 2);
		layer1[13][tileRows-2].setState((byte) 2);
		layer1[14][tileRows-2].setState((byte) 2);
		layer1[15][tileRows-2].setState((byte) 2);
		
		layer1[10][tileRows-3].setState((byte) 2);
		layer1[11][tileRows-3].setState((byte) 2);
		layer1[12][tileRows-3].setState((byte) 2);
		layer1[13][tileRows-3].setState((byte) 2);
		layer1[14][tileRows-3].setState((byte) 2);
		layer1[15][tileRows-3].setState((byte) 2);*/
		
		
		
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
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public short getStartX() {
		return startX;
	}

	public short getStartY() {
		return startY;
	}

	public void setStartX(short startX) {
		this.startX = startX;
	}

	public void setStartY(short startY) {
		this.startY = startY;
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

}