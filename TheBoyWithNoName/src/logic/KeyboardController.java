package logic;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

//the keyboard controller is the KeyListener of the game, it register
//all the keys currently pressed in the activeKeys HashSet
public class KeyboardController implements KeyListener{

	public KeyboardController(){
		activeKeys=new HashSet<Integer>();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		activeKeys.add(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		activeKeys.remove(e.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	public static HashSet<Integer> getActiveKeys(){
		return activeKeys;
	}
	
	private static HashSet<Integer> activeKeys;
}
