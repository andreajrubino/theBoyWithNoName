package logic;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

public class World {
	public World(){
		tiledMap=new Tile[ROWS][COLS];
	}
	
	public void initializeStage(int level){
		try {
			CURRENT_BACKGROUND=ImageIO.read(getClass().getResource("../images/background"+String.valueOf(level)+".png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		InputStream is=this.getClass().getResourceAsStream("/levels/level"+String.valueOf(level)+".txt");
		BufferedReader reader=new BufferedReader(new InputStreamReader(is));
		String line=null;
		String[] tilesInLine=new String[ROWS];
		try {
			int i=0;
			while((line=reader.readLine())!=null){
				tilesInLine=line.split(" ");
				for(int j=0; j<COLS; j++){
					if(!tilesInLine[j].equalsIgnoreCase("empt")){
						tiledMap[i][j]=newTileInstance(tilesInLine[j],i,j);
					} else {
						tiledMap[i][j]=null;
					}
				}
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Tile newTileInstance(String name, int i, int j) {
		//APC Addition
		//if it's not empty return the corresponding block
		//invalid names will result in an io exception within block
		if(!name.equals("empt")){
			return new Block(name, i, j);
		}
		return null;
	}
	
	public static BufferedImage CURRENT_BACKGROUND;
	public static Tile[][] tiledMap;
	public static final int ROWS=9;
	public static final int COLS=20;
}
