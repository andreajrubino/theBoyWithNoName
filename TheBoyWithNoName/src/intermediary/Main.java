package intermediary;

import gui.GameFrame;
import gui.GamePanel;

public class Main {
	public static void main(String[] args) {
		//Initialise the gamePanel
		GamePanel gamePanel=new GamePanel();
		
		//Initialise and start the main thread of the game
		GameManager gameManager=new GameManager(gamePanel);
		gameManager.start();
		
		//start-up the game main frame 
		GameFrame gameFrame=new GameFrame(gamePanel);
	}
}
