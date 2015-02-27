package gui;

import java.awt.Toolkit;
import javax.swing.JFrame;

//the main game frame of the game 
public class GameFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public GameFrame(GamePanel gamePanel){
		
		//set frame to appear at the center of the screen
		this.setLocation((int)((Toolkit.getDefaultToolkit().getScreenSize().getWidth()-WIDTH)/2),
				((int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()-HEIGHT)/2));		
	
		//set the size of the frame
		this.setSize(WIDTH,HEIGHT);
		
		//set the title of the frame
		this.setTitle("THE BOY WITH NO NAME");
		
		//make the frame visible
		this.setVisible(true);
		
		//set the operation that will happen when closing the frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//make the frame impossible to resize 
		this.setResizable(false);
		
		this.add(gamePanel);
		
		gamePanel.grabFocus();
		gamePanel.requestFocusInWindow();
	}
	
	public static final int WIDTH=1280;
	public static final int HEIGHT=640;
}
