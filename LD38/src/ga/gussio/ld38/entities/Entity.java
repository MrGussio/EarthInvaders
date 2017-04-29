package ga.gussio.ld38.entities;

import java.awt.Graphics;

public abstract class Entity {

	protected float x, y;
	
	public Entity(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public abstract void render(Graphics g);
	public abstract void tick();
	
}
