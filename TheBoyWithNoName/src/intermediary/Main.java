package intermediary;

import gui.GameFrame;
import gui.GamePanel;

public class Main {
	public static void main(String[] args) {
		//initialize the gamePanel
		GamePanel gamePanel=new GamePanel();
		
		//initialize and start the main thread of the game
		GameManager gameManager=new GameManager(gamePanel);
		gameManager.start();
		
		//start-up the game main frame 
		@SuppressWarnings("unused")
		GameFrame gameFrame=new GameFrame(gamePanel);
	}
}
