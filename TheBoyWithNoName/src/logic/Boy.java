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
		//Initialise the buffers that will store the run sprites
		run_L=new BufferedImage[BUFFER_RUN_SIZE];
		run_R=new BufferedImage[BUFFER_RUN_SIZE];
		
		//load all the images
		loadInformations();
		
		//Initially, the character is standing still with his head turned right
		currentFrame=idle_R;
		
		//Initialise the bounding box of the character, this will be very important
		//when we manage collisions between objects
		boundingBox=new Rectangle(BOY_START_X+DISPLACEMENT,currentY,BOY_WIDTH,BOY_HEIGHT);
	}
	
	
	//loads all the sprites needed to animate the character 
	private void loadInformations() {
		try {
	        BufferedImage spritesheet = ImageIO.read(getClass().getResource("/images/player.png"));
		  
			idle_R=spritesheet.getSubimage(0, 0, 40, 64);
			idle_L=spritesheet.getSubimage(0, 64, 40, 64);
			
			for (int i = 0; i < 6; i++) {
			    run_R[i] = spritesheet.getSubimage((i+1)*40, 0, 40, 64);
			    run_L[i] = spritesheet.getSubimage((i+1)*40, 64, 40, 64);
			}
			
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//function called by the GameManager's manageKeys() function
	public void move(int direction) {
	    this.idle=false;
	    
		switch (direction) {
			//in case you have to move left..
			case KeyEvent.VK_LEFT:
				//update the character's position
				currentX=currentX-DISPLACEMENT;
				
				//you can't go back
                if(currentX<=0){
                    currentX=0;
                }
				
				//update the character's bounding box position
				boundingBox.setLocation(currentX, currentY);
				
				//change the current frame in animation
				if(!jumping && !falling){
	                currentFrame=run_L[currentFrameNumber];
                    setFrameNumber();
                    currentFrame=run_L[currentFrameNumber];
                } else {
                    currentFrame=run_L[0];
                }
			
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
				if(!jumping && !falling){
                    currentFrame=run_R[currentFrameNumber];
                    setFrameNumber();
                    currentFrame=run_R[currentFrameNumber];
                } else {
                    currentFrame=run_R[0];
                }
				
				//set the right direction as the last one 
				last_direction=KeyEvent.VK_RIGHT;
				break;
				
			default:
				break;
		}
		currentRow=currentY/Tile.TILE_SIZE;
        currentCol=currentX/Tile.TILE_SIZE;
		moveCounter++;
	}
	
	public void checkRestoringCount() {
        if(restoring_count>0){
            restoring_count--;
            if(restoring_count%RESTORING_MODULE==0){
                restoring=!restoring;
            }
        } 
    }

    //checks and handles possible collisions with static blocks (Block class)
    public void checkBlockCollisions(){

        //position of the character's feet on the y-axis
        int footY=(int)(boundingBox.getMaxY());

        //if the character is jumping, his head must not touch a block;
        //if it touches a block, stop the ascending phase of the jump (start falling)
        if(jumping){
          //row position of the cell above the character's head (in the tiled map)
            int upRow=(int)((boundingBox.getMinY()-1)/Tile.TILE_SIZE);

            //tile position relative to the upper-left corner of the character's bounding box
            int upLeftCornerCol=(int)(boundingBox.getMinX()/Tile.TILE_SIZE);

            //tile position relative to the upper-right corner of the character's bounding box
            int upRightCornerCol=(int)((boundingBox.getMaxX())/Tile.TILE_SIZE);

            if(currentRow>=0){
                if(World.tiledMap[upRow][upLeftCornerCol] instanceof Block){
                    //if the upper-left corner stats intersecting a block, stop the jumping phase
                    //and start the falling phase, setting the jump_count to 0
                    if(World.tiledMap[upRow][upLeftCornerCol].getBoundingBox().intersects(boundingBox)){
                        jumping=false;
                        jump_count=0;
                        falling=true;
                        return;
                    }
                }
                if(World.tiledMap[upRow][upRightCornerCol] != null){
                    //if the upper-right corner stats intersecting a block, stop the jumping phase
                    //and start the falling phase, setting the jump_count to 0
                    if(World.tiledMap[upRow][upRightCornerCol].getBoundingBox().intersects(boundingBox)){
                        jumping=false;
                        jump_count=0;
                        falling=true;
                        return;
                    }
                }
            }

        }

        //if last direction was right..
        if(last_direction==KeyEvent.VK_RIGHT){

            //get the left side of the bounding box
            int footX=(int)boundingBox.getMinX();

            //get the tile position (in the tiled map) 
            //relative to the tile in front of the character
            int tileInFrontOfFootRow=((footY-1)/Tile.TILE_SIZE);
            int tileInFrontOfFootCol=(footX/Tile.TILE_SIZE)+1;

            if(tileInFrontOfFootCol<World.COLS){
                //if the tile in front of the character contains a block..
                if(World.tiledMap[tileInFrontOfFootRow][tileInFrontOfFootCol] instanceof Block){
                    //..and the character's bounding box intersect the block's one
                    if(boundingBox.intersects(World.tiledMap[tileInFrontOfFootRow][tileInFrontOfFootCol].getBoundingBox())){
                        //push the character away and re-set its position
                        currentX-=DISPLACEMENT;
                        boundingBox.setLocation(currentX, currentY);
                        currentCol=currentX/Tile.TILE_SIZE;
                    }
                }

                if(World.tiledMap[currentRow][currentCol] instanceof Block){
                    //if the tile the character finds himself in contains a block, act like above
                    if(boundingBox.intersects(World.tiledMap[currentRow][currentCol].getBoundingBox())){
                        currentX-=DISPLACEMENT;
                        boundingBox.setLocation(currentX, currentY);
                        currentCol=currentX/Tile.TILE_SIZE;
                    }
                }
            }
        } else {
            //get the right side of the bounding box
            int footX=(int) boundingBox.getMaxX();

            //get the tile position (in the tiled map) 
            //relative to the tile in front of the character
            int tileInFrontOfFootRow=((footY-1)/Tile.TILE_SIZE);
            int tileInFrontOfFootCol=(footX/Tile.TILE_SIZE)-1;

            if(tileInFrontOfFootCol>=0){
                //if the tile in front of the character contains a block..
                if(World.tiledMap[tileInFrontOfFootRow][tileInFrontOfFootCol] instanceof Block){
                    //..and the character's bounding box intersect the block's one
                    if(boundingBox.intersects(World.tiledMap[tileInFrontOfFootRow][tileInFrontOfFootCol].getBoundingBox())){
                        //push the character away and re-set its position
                        currentX+=DISPLACEMENT;
                        boundingBox.setLocation(currentX, currentY);
                        currentCol=currentX/Tile.TILE_SIZE;
                    }
                }

                if(World.tiledMap[currentRow][currentCol] instanceof Block){
                    //if the tile the character finds himself in contains a block, act like above
                    if(boundingBox.intersects(World.tiledMap[currentRow][currentCol].getBoundingBox())){
                        currentX+=DISPLACEMENT;
                        boundingBox.setLocation(currentX, currentY);
                        currentCol=currentX/Tile.TILE_SIZE;
                    }
                }
            }   
        }
    }


    public void checkFallingState(){
        if(boundingBox.getMaxY()/Tile.TILE_SIZE>=World.ROWS){
            die();
        }


        if(jumping){
            return;
        }

        if(falling){
            currentY+=DISPLACEMENT;
            currentRow=currentY/Tile.TILE_SIZE;
            boundingBox.setLocation(currentX, currentY);
        }

        int lowLeftX=(int)boundingBox.getMinX()+1;
        int lowRightX=(int) boundingBox.getMaxX()-1;

        int underlyingTileXR=lowRightX/Tile.TILE_SIZE;
        int underlyingTileXL=lowLeftX/Tile.TILE_SIZE;

        if(currentRow+1>=World.ROWS || underlyingTileXR>=World.COLS){
            return;
        }

        if(!((World.tiledMap[currentRow+1][underlyingTileXR]) instanceof Block)
            && !((World.tiledMap[currentRow+1][underlyingTileXL]) instanceof Block)){
            falling=true;
            return;
        }

        falling=false;
    }

    private void die() {
        currentX=BOY_START_X;
        currentY=GameFrame.HEIGHT-PlayPanel.TERRAIN_HEIGHT-BOY_HEIGHT;
        currentCol=currentX/Tile.TILE_SIZE;
        currentRow=currentY/Tile.TILE_SIZE;
        boundingBox=new Rectangle(BOY_START_X+DISPLACEMENT,currentY,BOY_WIDTH,BOY_HEIGHT);
        last_direction=KeyEvent.VK_RIGHT;
        falling=false;
        restoring=true;
        restoring_count=RESTORING_THRESH;
        life--;
    }

    public void reinitialize() {
        currentX=0;
        currentY=GameFrame.HEIGHT-PlayPanel.TERRAIN_HEIGHT-BOY_HEIGHT;
        currentCol=0;
        currentRow=currentY/Tile.TILE_SIZE;
        boundingBox=new Rectangle(BOY_START_X+DISPLACEMENT,currentY,BOY_WIDTH,BOY_HEIGHT);
        last_direction=KeyEvent.VK_RIGHT;
        falling=false;
    }
    
    //checks the jumping variables and animates jumps 
    //check the comments above 'jumping' and 'jump_count' variables
    //for more details
    public void checkJumpState() {
        if(jumping){
            if(jump_count<JUMP_COUNTER_THRESH){
                currentY-=DISPLACEMENT;
                boundingBox.setLocation(currentX, currentY);
            } 

            jump_count++;

            if(jump_count>=JUMP_COUNTER_THRESH){
                jumping=false;
                jump_count=0;
                falling=true;
            }
        }
    }
	
	//sets the current frame when the boy is moving - we have a total of 5 frames for 
	//each run direction. The variable moveCounter is incremented each time the gameManager
	//calls the move function on the Boy. So according to moveCounter we can choose the current
	//frame. The frame changes every MOVE_COUNTER_THRESH increments of the moveCounter variable.
	//In this case MOVE_COUNTER_THRESH is set to 5
	private void setFrameNumber() {
		currentFrameNumber  = moveCounter/MOVE_COUNTER_THRESH;
		currentFrameNumber %= 6;
		
		if(moveCounter>MOVE_COUNTER_THRESH*6){
			moveCounter=0;
		}
	}

	//called every time the player presses the jump key (SPACE for now)
    //if the character is not already jumping (boolean jumpin=true)
    public void jump() {
        //sets the jumping state to true
        this.jumping=true;

        //reinitialize the jump_count, useful to determine for how 
        //much time the character is going to stay in the air
        this.jump_count=0;

        //sets the current jumping frame based on the last direction 
        if(last_direction==KeyEvent.VK_RIGHT){
            currentFrame=run_R[2];
        } else {
            currentFrame=run_L[2];
        }
    }

    public boolean getJumping() {
        return jumping;
    }

    //jump_count works with JUMP_COUNTER_THRESH: in particular this 
    //variable is incremented every time the main thread calls the checkState()
    //function and goes on until jump_count has not reached JUMP_COUNTER_THRESH
    //making the currentY of the character smaller and smaller so that he keeps
    //ascending. When the jump_count reaches JUMP_COUNTER_THRESH the currentY is 
    //incremented for the character is in the descending phase of the jump. It goes on
    //incrementing until jump_count reaches JUMP_COUNTER_THRESH*2, then the jumping 
    //boolean is set to false and the count is reinitialized
    private int jump_count=0;

    //jumping is 'true' when the character is actually jumping
    //is 'false' when the character is not up in the air
    private boolean jumping;

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
		this.idle=true;
	}
	
	public boolean getFalling(){
        return falling;
    }


    public boolean getRestoring() {
        return restoring;
    }

    public int getLife() {
        return life;
    }


    public boolean outOfBounds() {
        if(currentX>=GameFrame.WIDTH){
            return true;
        }

        return false;
    }

    private final static int RESTORING_THRESH=84;

    private final static int RESTORING_MODULE=12;

    private int restoring_count=0;

    //restoring is true when the character has just died and remains
    //true until his body flashes 3 times
    private boolean restoring=false;

    //true when the character is falling
    //false when the character is not falling
    //initially the protagonist is not falling
    private boolean falling=false;

    //JUMP_COUNTER_THRESH is the upper bound to the counter jump_count:
    //- from 0 to JUMP_COUNTER_THRESH the character is going up 
    //- from JUMP_COUNTER_THRESH to JUMP_COUNTER_THRESH*2 the character is going down 
    private static final int JUMP_COUNTER_THRESH=20;
	
	//initially the last direction is right 
	private int last_direction=KeyEvent.VK_RIGHT;
	
	//MOVE_COUNTER_THRESH is explained in the setFrameNumber function's comment
	private static final int MOVE_COUNTER_THRESH=5;
	
	//moveCounter is explained in the setFrameNumber function's comment
	private int moveCounter=0;
	
	//the boundingBox is essentially the hitBox of the character
	//it defines the space occupied by the character at the specific moment 
	private Rectangle boundingBox;
	
	//DISPLACEMENT is the distance covered by a single step of the character
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
	
	public static final int MAX_LIFE = 3;
	
	//height of the main character (used to set the boundingBox)
	private final int BOY_HEIGHT=64;
	
	//width of the main character (used to set the boundingBox)
	private final int BOY_WIDTH=32;
	
	//current position of the character along the x-axis 
	//initially the character is placed at BOY_START_X
	private int currentX=BOY_START_X;
	
	//current position of the character along the y-axis 
	//initially the character is placed at BOY_START_X
	private int currentY=GameFrame.HEIGHT-PlayPanel.TERRAIN_HEIGHT-BOY_HEIGHT;
	
	private int currentCol=currentX/Tile.TILE_SIZE;

    private int currentRow=currentY/Tile.TILE_SIZE;

    //idle is 'true' if the character is not moving, false otherwise
    private boolean idle=true;
    
    //life initially equals 3, every time the character dies it decrements (life--)
    private int life=MAX_LIFE;
}
