package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import logic.Block;
import logic.Boy;
import logic.Collectible;
import logic.NPC;
import logic.Tile;
import logic.Weapon;
import logic.World;

//PlayPanel - Is the panel where you see the actual game in motion,
//all the big part under the stats panel 
public class PlayPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	public PlayPanel(){
		
		loadInformations();
		
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
	
	private void loadInformations() {
		try {
			speechBalloon=ImageIO.read(getClass().getResource("../images/speechBalloon.png"));
			pixelFont=Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("../fonts/pixel.ttf")).deriveFont(25.0f);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		collectibleAnimationCount++;
		if(collectibleAnimationCount%30==0){
			collectible_y_offset=-collectible_y_offset;
		} 
		
		if(collectibleAnimationCount>60){
			collectibleAnimationCount=0;
		}
		
		Graphics2D g2=(Graphics2D)g;
		g2.setFont(pixelFont);
		
		//use antialiasing to draw smoother images and lines
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.drawImage(World.CURRENT_BACKGROUND,0,-Tile.TILE_SIZE,GameFrame.WIDTH,PLAY_PANEL_HEIGHT, null);
		
		for(int i=0; i<World.ROWS; i++){
			for(int j=0; j<World.COLS; j++){
				if(World.tiledMap[i][j] instanceof Block){
					g2.drawImage(World.tiledMap[i][j].getImage(), World.tiledMap[i][j].getCurrentX(), 
							World.tiledMap[i][j].getCurrentY(), null);
				} else if(World.tiledMap[i][j] instanceof Collectible){
					if(World.tiledMap[i][j] instanceof Weapon){
						g2.drawImage(World.tiledMap[i][j].getImage(), World.tiledMap[i][j].getCurrentX(), 
								World.tiledMap[i][j].getCurrentY()+new Random().nextInt(2), null);
					} else {
						g2.drawImage(World.tiledMap[i][j].getImage(), World.tiledMap[i][j].getCurrentX(), 
								World.tiledMap[i][j].getCurrentY()+collectible_y_offset, null);
					}
				}
			}
		}
		
		//draw the protagonist of the game
		if(!boy.getRestoring()){
			g2.drawImage(boy.getCurrentFrame(),boy.getCurrentX(),boy.getCurrentY(),null);
		}
		
		if(currentNPCs.size()>0){
			NPC currentNPC;
			for(int i=0; i<currentNPCs.size(); i++){
				currentNPC=currentNPCs.get(i);
				//we draw the npc character from one row above his real position simply because the image of an npc 
				//is twice as tall as the protagonist's
				g2.drawImage(currentNPC.getCurrentFrame(),currentNPC.getCurrentX(),currentNPC.getCurrentY()-Tile.TILE_SIZE,null);
				if(currentNPC.isTalking()){
						
					if(++balloonCount%30==0){
						if(dynamicBalloonOffset==2){
							dynamicBalloonOffset=0;
						} else {
							dynamicBalloonOffset=2;
						}
					}
					
					g2.drawImage(speechBalloon,currentNPC.getCurrentX()-speechBalloon.getWidth()/2+
							Tile.TILE_SIZE/2-SPEECH_BALLOON_X_OFFSET,currentNPC.getCurrentY()+Tile.TILE_SIZE+1
							+dynamicBalloonOffset,null);
					tempSentence=currentNPC.getSentence();
					
					if(tempSentence.contains("NEWLINE")){
						g2.drawString(tempSentence.split(" NEWLINE ")[0], currentNPC.getCurrentX()-speechBalloon.getWidth()/3-
								SPEECH_BALLOON_X_OFFSET*5,
								currentNPC.getCurrentY()+speechBalloon.getHeight()-20+dynamicBalloonOffset);
						g2.drawString(tempSentence.split(" NEWLINE ")[1], currentNPC.getCurrentX()-speechBalloon.getWidth()/3,
								currentNPC.getCurrentY()+speechBalloon.getHeight()+dynamicBalloonOffset);
					} else {
						g2.drawString(tempSentence, currentNPC.getCurrentX()-speechBalloon.getWidth()/3-SPEECH_BALLOON_X_OFFSET*5,
								currentNPC.getCurrentY()+speechBalloon.getHeight()-20+dynamicBalloonOffset);
					}
					
				}
			}
		}
	}
	
	//function called by the GameManager to add the boy (protagonist) to the play panel at runtime
	//the PlayPanel needs a reference to the boy since he's drawn a LOT of times 
	public void addBoy(Boy boy) {
		this.boy=boy;
	}
	

	public void addNPCs(ArrayList<NPC> currentNPCs) {
		this.currentNPCs=currentNPCs;
	}
	
	public void clearNPCs() {
		currentNPCs.clear();
	}
	
	//height of the terrain in pixels - this is basically the distance of the boy's feet 
	//from the bottom border of the window you play the game in
	public static final int TERRAIN_HEIGHT=192;
	
	//height of the PlayPanel 
	public static final int PLAY_PANEL_HEIGHT=640;
	
	//reference to the protagonist of the game
	private Boy boy;
	
	private ArrayList<NPC> currentNPCs;
	
	private BufferedImage speechBalloon;
	
	private static final int SPEECH_BALLOON_X_OFFSET=5;
	
	private Font pixelFont;
	
	private int dynamicBalloonOffset=0;
	private int balloonCount=0;
	
	private String tempSentence;
	
	private int collectibleAnimationCount=0;
	private int collectible_y_offset=2;
}
