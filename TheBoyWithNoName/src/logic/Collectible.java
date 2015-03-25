package logic;

import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Collectible extends Tile {
	public Collectible(String name, int row, int col){
		super(name,row,col);
		loadInformations();
	}
	
	@Override
	protected void initializeStuff() {
		currentX=col*TILE_SIZE+TILE_SIZE/2-width/2;
		currentY=row*TILE_SIZE;
		boundingBox=new Rectangle(currentX,currentY,width,height);
	}
	
	@Override
	protected void loadInformations() {
		try {
			image=ImageIO.read(getClass().getResource("../images/"+name+".png"));
			width=image.getWidth();
			height=image.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int width;
	private int height;
}
