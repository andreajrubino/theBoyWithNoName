package intermediary;

import java.awt.event.KeyEvent;
import java.util.HashSet;

import logic.Boy;
import logic.KeyboardController;
import logic.NPC;
import logic.World;
import gui.GamePanel;

//the GameManager is the main thread of the game, it calls repaints 
//for the play panel and statsPanel when necessary and manages keys 
//pressed, associating them to actual actions 
public class GameManager extends Thread {
	public GameManager(GamePanel gamePanel){
		this.world=new World();
		this.world.initializeStage(currentLevel);
		
		this.npcManager=new NPCManager(currentLevel);
		
		//initialize the protagonist of the game
		this.boy=new Boy();
		
		//stores the gamePanel and adds the boy and the npcs to it
		this.gamePanel=gamePanel;
		this.gamePanel.addBoy(boy);
		
		if(npcManager.getNPCs().size()>0){
			gamePanel.addNPCs(npcManager.getNPCs());
		} else {
			gamePanel.clearNPCs();
		}
		
		//while you're playing the game, the gameIsRunning is set to true
		this.gameIsRunning=true;
	}
	
	@Override
	public void run() {
		while(gameIsRunning){
			
			if(boy.outOfBounds()){
				currentLevel++;
				npcManager.initializeStage(currentLevel);
				world.initializeStage(currentLevel);
				boy.reinitialize();
			}
			
			boy.checkFallingState();
			
			//updates the character movement if he's 'jumping'
			boy.checkJumpState();
			
			//manage the keys currently pressed
			manageKeys();
			
			boy.checkCollectibles();
			
			boy.checkBlockCollisions();
			
			boy.checkRestoringCount();
				
			gamePanel.repaintGame();
			
			try {
				Thread.sleep(MAIN_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	//the function manages the keys currently pressed associating concrete
	//actions to them
	private void manageKeys() {
		//get the currently pressed keys from the KeyboardController
		HashSet<Integer> currentKeys=KeyboardController.getActiveKeys();
		
		if(!listening){
			//manage the two possible run direction
			if(currentKeys.contains(KeyEvent.VK_RIGHT)){
				//move right
				boy.move(KeyEvent.VK_RIGHT);
			} else if (currentKeys.contains(KeyEvent.VK_LEFT)){
				//move left
				boy.move(KeyEvent.VK_LEFT);
			} else if(currentKeys.isEmpty() && !boy.getJumping() && !boy.getFalling()){
				//if the player is not pressing keys, the protagonist stands still
				boy.stop();
			}
		}
		
		if(currentKeys.contains(KeyEvent.VK_SPACE)) {
			if(!boy.getJumping() && !boy.getFalling()){
				boy.jump();
			}
		}
		
		if(currentKeys.contains(KeyEvent.VK_ENTER)){
			NPC tempNpc;
			//find the closest npc according to the character's position
			if((tempNpc=npcManager.closestNPC(boy.getRow(),boy.getCol()))!=null){
				
				//if the npc is already talking, keep talking...
				if(tempNpc.isTalking()){
					if(!(tempNpc.continueTalking())){
						listening=false;
					}
				
				//otherwise interact with the npc
				} else {
					tempNpc.interact();
					
					//put the character in <idle> status when he's talking
					boy.stop();
					
					//prevent the character from moving when talking
					listening=true;
				}
			}
			currentKeys.remove(KeyEvent.VK_ENTER);
		}
		
	}

	public Boy getBoy(){
		return boy;
	}
	
	private boolean listening=false;
	
	//number of the current level the character finds himself in
	private int currentLevel=1;
	
	//variable set to 'true' if the game is running, 'false' otherwise
	private boolean gameIsRunning;
	
	//reference to the gamePanel
	private GamePanel gamePanel;
	
	//main sleep time of the GameManager thread - in this case
	//the gameManager does al he has to do and then waits for 18ms
	//before starting once again
	private static final int MAIN_SLEEP_TIME=16;
	
	//reference to the game main character
	private Boy boy;
	
	private World world;
	
	private NPCManager npcManager;
}
