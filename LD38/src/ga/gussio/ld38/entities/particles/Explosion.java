package ga.gussio.ld38.entities.particles;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import ga.gussio.ld38.entities.Entity;

public class Explosion extends Entity {

	public CopyOnWriteArrayList<Particle> particles = new CopyOnWriteArrayList<Particle>();
	
	public Explosion(float x, float y) {
		super(x, y);
		Random r = new Random();
		int amount = r.nextInt(60-10+1)+10;
		for(int i = 0; i < amount; i++){
			int dir = r.nextInt(360);
			int speed = r.nextInt(2)+1;
			int duration = r.nextInt(1500-800+1)+800;
			int colorVariant = r.nextInt(255);
			int size = r.nextInt(5)+1;
			Color color = new Color(colorVariant, colorVariant, colorVariant);
			particles.add(new Particle(x, y, dir, speed, duration, color, size));
		}
	}

	@Override
	public void render(Graphics g) {
		for(Particle p : particles){
			p.render(g);
		}
	}

	@Override
	public void tick() {
		for(Particle p : particles){
			p.tick();
			if(p.hasExpired())
				particles.remove(p);
		}
	}

}
