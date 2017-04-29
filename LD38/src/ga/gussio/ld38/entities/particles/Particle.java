package ga.gussio.ld38.entities.particles;

import java.awt.Color;
import java.awt.Graphics;

import ga.gussio.ld38.entities.Entity;

public class Particle extends Entity {

	private long expireTime;
	private Color color;
	private int size;
	private int speed;
	private int dir;
	
	public Particle(float x, float y, int dir, int speed, long timeInMilliseconds, Color color, int size) {
		super(x, y);
		expireTime = System.currentTimeMillis()+timeInMilliseconds;
		if(timeInMilliseconds < 0)
			expireTime = -1;
			
		this.color = color;
		this.size = size;
		this.dir = dir;
		this.speed = speed;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(color);
		g.fillRect((int) x, (int) y, size, size);
	}

	@Override
	public void tick() {
		x += speed * Math.cos(Math.toRadians(dir));
		y += speed * Math.sin(Math.toRadians(dir));
	}
	
	public boolean hasExpired(){
		if(expireTime < 0)
			return false;
		
		return System.currentTimeMillis() > expireTime;
	}

}
