package logic;

import gui.GameFrame;
import gui.PlayPanel;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


//the boy is the main character of the game, the one you control with your arrow keys
public class Boy {
	public Boy(){
		//initialize the buffers that will store the run sprites
		run_L=new BufferedImage[BUFFER_RUN_SIZE];
		run_R=new BufferedImage[BUFFER_RUN_SIZE];
		
		//load all the images
		loadInformations();
		
		//initally, the character is standing still with his head turned right
		currentFrame=idle_R;
		
		//initialize the bounding box of the character, this will be very important
		//when we manage collisions between objects
		boundingBox=new Rectangle(currentX,currentY,BOY_WIDTH,BOY_HEIGHT);
	}
	
	
	//loads all the sprites needed to animate the character 
	private void loadInformations() {
		try {
			idle_R=ImageIO.read(getClass().getResource("../images/idle_R.png"));
			idle_L=ImageIO.read(getClass().getResource("../images/idle_L.png"));
			
			run_R[0]=ImageIO.read(getClass().getResource("../images/run_R0.png"));
			run_L[0]=ImageIO.read(getClass().getResource("../images/run_L0.png"));
			
			run_R[1]=ImageIO.read(getClass().getResource("../images/run_R1.png"));
			run_L[1]=ImageIO.read(getClass().getResource("../images/run_L1.png"));
			
			run_R[2]=ImageIO.read(getClass().getResource("../images/run_R2.png"));
			run_L[2]=ImageIO.read(getClass().getResource("../images/run_L2.png"));
			
			run_R[3]=ImageIO.read(getClass().getResource("../images/run_R3.png"));
			run_L[3]=ImageIO.read(getClass().getResource("../images/run_L3.png"));
			
			run_R[4]=ImageIO.read(getClass().getResource("../images/run_R4.png"));
			run_L[4]=ImageIO.read(getClass().getResource("../images/run_L4.png"));
			
			run_R[5]=ImageIO.read(getClass().getResource("../images/run_R5.png"));
			run_L[5]=ImageIO.read(getClass().getResource("../images/run_L5.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//function called by the GameManager's manageKeys() function
	public void move(int direction) {
		switch (direction) {
			//in case you have to move left..
			case KeyEvent.VK_LEFT:
				//update the character's position
				currentX=currentX-DISPLACEMENT;
				
				//update the character's bounding box position
				boundingBox.setLocation(currentX, currentY);
				
				//change the current frame in animation
				setFrameNumber();
				currentFrame=run_L[currentFrameNumber];
				
				//set the left direction as the last one 
				last_direction=KeyEvent.VK_LEFT;
				break;
			
			//in case you have to move right..
			case KeyEvent.VK_RIGHT:
				//update the character's position
				currentX=currentX+DISPLACEMENT;
				
				//update the character's bounding box position
				boundingBox.setLocation(currentX, currentY);
				
				//change the current frame in animation
				setFrameNumber();
				currentFrame=run_R[currentFrameNumber];
				
				//set the right direction as the last one 
				last_direction=KeyEvent.VK_RIGHT;
				break;
				
			default:
				break;
		}
		moveCounter++;
	}
	
	//sets the current frame when the boy is moving - we have a total of 5 frames for 
	//each run direction. The variable moveCounter is incremented each time the gameManager
	//calls the move function on the Boy. So according to moveCounter we can choose the current
	//frame. The frame changes every MOVE_COUNTER_THRESH increments of the moveCounter variable.
	//In this case MOVE_COUNTER_THRESH is set to 5
	private void setFrameNumber() {
		if(moveCounter>=0 && moveCounter<=MOVE_COUNTER_THRESH*1){
			currentFrameNumber=0;
		} else if(moveCounter>MOVE_COUNTER_THRESH*1 && moveCounter<=MOVE_COUNTER_THRESH*2){
			currentFrameNumber=1;
		} else if(moveCounter>MOVE_COUNTER_THRESH*2 && moveCounter<=MOVE_COUNTER_THRESH*3){
			currentFrameNumber=2;
		} else if(moveCounter>MOVE_COUNTER_THRESH*3 && moveCounter<=MOVE_COUNTER_THRESH*4){
			currentFrameNumber=3;
		} else if(moveCounter>MOVE_COUNTER_THRESH*4 && moveCounter<=MOVE_COUNTER_THRESH*5){
			currentFrameNumber=4;
		} else if(moveCounter>MOVE_COUNTER_THRESH*5 && moveCounter<=MOVE_COUNTER_THRESH*6){
			currentFrameNumber=5;
		} 
		
		if(moveCounter>MOVE_COUNTER_THRESH*6){
			moveCounter=0;
		}
	}


	//gets the current frame of the animation
	public BufferedImage getCurrentFrame(){
		return currentFrame;
	}

	//gets x-position of the character
	public int getCurrentX(){
		return currentX;
	}
	
	//gets y-position of the character
	public int getCurrentY(){
		return currentY;
	}
	
	//gets the bounding box of the character
	public Rectangle getBoundingBox() {
		return boundingBox;
	}
	
	//the stop() function sets an idle position as current frame
	//this is done by examining the last_direction variable.
	public void stop() {
		//if the last direction was right, set the idle-right position
		//as the current frame 
		if(last_direction==KeyEvent.VK_RIGHT){
			currentFrame=idle_R;
		//otherwise set the idle-left position
		} else {
			currentFrame=idle_L;
		}
	}
	
	//initially the last direction is right 
	private int last_direction=KeyEvent.VK_RIGHT;
	
	//MOVE_COUNTER_THRESH is explained in the setFrameNumber function's comment
	private static final int MOVE_COUNTER_THRESH=5;
	
	//moveCounter is explained in the setFrameNumber function's comment
	private int moveCounter=0;
	
	//the boundingBox is essentially the hitBox of the character
	//it defines the space occupied by the character at the specific moment 
	private Rectangle boundingBox;
	
	//DISPLACEMENT is the distance is the distance covered by a single step of the character
	private static final int DISPLACEMENT=4;
	
	//current frame in the animation
	private BufferedImage currentFrame;
	
	//size of the run animation buffer - a slot for each frame
	private static final int BUFFER_RUN_SIZE=6;
	
	//all the bufferedImages used in the character's animation 
	private BufferedImage idle_R;
	private BufferedImage idle_L;
	private BufferedImage[] run_R;
	private BufferedImage[] run_L;
	
	//determines the currentFrame to be used in a run animation
	private int currentFrameNumber=0;
	
	//the initial width offset of the character
	public static final int BOY_START_X=128;
	
	//height of the main character (used to set the boundingBox)
	private final int BOY_HEIGHT=64;
	
	//width of the main character (used to set the boundingBox)
	private final int BOY_WIDTH=40;
	
	//current position of the character along the x-axis 
	//initially the character is placed at BOY_START_X
	private int currentX=BOY_START_X;
	
	//current position of the character along the y-axis 
	//initially the character is placed at BOY_START_X
	private int currentY=GameFrame.HEIGHT-PlayPanel.TERRAIN_HEIGHT-BOY_HEIGHT;
	
}
