package ga.gussio.ld38;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

	public boolean left, right, shoot;
	
	protected int leftKey, rightKey, shootKey;
	
	public InputHandler(int left, int right, int shoot){
		leftKey = left;
		rightKey = right;
		shootKey = shoot;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == leftKey){
			left = true;
		}
		if(e.getKeyCode() == rightKey){
			right = true;
		}
		if(e.getKeyCode() == shootKey){
			shoot = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == leftKey){
			left = false;
		}
		if(e.getKeyCode() == rightKey){
			right = false;
		}
		if(e.getKeyCode() == shootKey){
			shoot = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

}