package ga.gussio.ld38.screen;

import java.awt.Graphics;

public abstract class Screen {
	
	public abstract void render(Graphics g);
	public abstract void tick();
	public abstract void dispose();
	
}
