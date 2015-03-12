package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import logic.Boy;
import logic.Tile;
import logic.World;

//PlayPanel - Is the panel where you see the actual game in motion,
//all the big part under the stats panel 
public class PlayPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	public PlayPanel(){
		
		//set the size of the play panel
		this.setSize(GameFrame.WIDTH, PLAY_PANEL_HEIGHT);
		
		//set a random background color to distinguish the play panel from the rest
		this.setBackground(Color.DARK_GRAY);
		
		//set no layouts
		this.setLayout(null);
		
		//double buffering should supposedly improve animations
		//read more about how double buffer works at http://www.anandtech.com/show/2794/2
		this.setDoubleBuffered(true);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2=(Graphics2D)g;
		
		//use antialiasing to draw smoother images and lines
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		g2.drawImage(World.CURRENT_BACKGROUND,0,-Tile.TILE_SIZE,GameFrame.WIDTH,PLAY_PANEL_HEIGHT, null);
		
		for(int i=0; i<World.ROWS; i++){
			for(int j=0; j<World.COLS; j++){
				if(World.tiledMap[i][j]!=null){
					g2.drawImage(World.tiledMap[i][j].getImage(), j*Tile.TILE_SIZE, i*Tile.TILE_SIZE, null);
				}
			}
		}
		
		//draw the protagonist of the game
		if(!boy.getRestoring()){
			g2.drawImage(boy.getCurrentFrame(),boy.getCurrentX(),boy.getCurrentY(),null);
			g2.draw(boy.getBoundingBox());
		}
	}
	
	//function called by the GameManager to add the boy (protagonist) to the play panel at runtime
	//the PlayPanel needs a reference to the boy since he's drawn a LOT of times 
	public void addBoy(Boy boy) {
		this.boy=boy;
	}
	
	//height of the terrain in pixels - this is basically the distance of the boy's feet 
	//from the bottom border of the window you play the game in
	public static final int TERRAIN_HEIGHT=192;
	
	//height of the PlayPanel 
	public static final int PLAY_PANEL_HEIGHT=640;
	
	//reference to the protagonist of the game
	private Boy boy;
}
