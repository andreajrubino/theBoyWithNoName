package logic;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class Tile {
	public Tile(String name, int i, int j){
		this.name=name;
		this.row=i;
		this.col=j;
		loadInformations();
		initializeStuff();
	}
	
	protected abstract void initializeStuff();
	
	protected abstract void loadInformations();
	
	public BufferedImage getImage(){
		return image;
	}
	
	public Rectangle getBoundingBox() {
		return boundingBox;
	}
	
	public int getCurrentX() {
		return currentX;
	}
	
	public int getCurrentY() {
		return currentY;
	}
	
	public String getName(){
		return name;
	}
	
	protected String name;
	protected int currentX;
	protected int currentY;
	protected int row;
	protected int col;
	protected BufferedImage image;
	protected Rectangle boundingBox;
	public static final int TILE_SIZE=64;
}
