package ga.gussio.ld38.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import ga.gussio.ld38.Game;
import ga.gussio.ld38.entities.particles.Explosion;
import ga.gussio.ld38.math.Circle;
import ga.gussio.ld38.screen.GameScreen;

public class Meteorite extends Entity {

	private int radius = 25;
	private int speed = 1;
	private double velX, velY;
	private double health;
	private double rotationSpeed = 1;
	private double rotation = 0;
	private BufferedImage[] img;
	private BufferedImage[] warning;
	private Rectangle warningRect;
	private long warningTime;
	
	Circle collision;
	public Meteorite(BufferedImage[] img) {
		super(0, 0);
		this.img = img;
		try {
			BufferedImage full = ImageIO.read(Game.class.getClassLoader().getResource("ga/gussio/ld38/res/warning.png"));
			warning = new BufferedImage[2];
			warning[0] = full.getSubimage(0, 0, 9, 9);
			warning[1] = full.getSubimage(9, 0, 9, 9);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		Random r = new Random();
		int direction = r.nextInt(360+1);
		health = r.nextInt(2)+3;
		
		double tempVelY = 10*Math.cos(Math.toRadians(direction));
        double tempVelX = 10*Math.sin(Math.toRadians(direction));
        
        if(r.nextInt(2) == 0)
        	tempVelX*=-1;
        if(r.nextInt(2) == 0)
        	tempVelY*=-1;
        Rectangle collision = new Rectangle((int)GameScreen.earth.getXCenter(), (int)GameScreen.earth.getYCenter(), radius*2, radius*2);
        Rectangle screen = new Rectangle(0, 0, Game.WIDTH, Game.HEIGHT);
        while(collision.intersects(screen)){
        	collision.translate((int) tempVelX, (int) tempVelY);
        }
        warningRect = new Rectangle(collision.x, collision.y, 30, 30);
        while(!screen.contains(warningRect)){
        	warningRect.translate((int) -tempVelX*2, (int) -tempVelY*2);
        }
        x = (float) collision.getX();
        y = (float) collision.getY();
		float xSpeed = (Game.WIDTH/2-x) / 1.0f;	
		float ySpeed = (Game.HEIGHT/2-y) / 1.0f;
		float factor = (float) (speed / Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed));
		xSpeed *= factor;
		ySpeed *= factor;
		
		velX = xSpeed;
		velY = ySpeed;
		this.collision = new Circle(x, y, radius);
		warningTime = System.currentTimeMillis()+3000;
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.YELLOW);
		g2d.rotate(Math.toRadians(rotation), collision.getXCenter(), collision.getYCenter());
		g2d.drawImage(img[(int) (4-health)], (int) x, (int) y, (int)collision.getRadius()*2, (int)collision.getRadius()*2, null);
		g2d.rotate(-Math.toRadians(rotation), collision.getXCenter(), collision.getYCenter());
		if(System.currentTimeMillis() < warningTime){
			g2d.drawImage(warning[warningTime-System.currentTimeMillis() < 1500 ? 1 : 0], (int)warningRect.x, warningRect.y, 30, 30, null);
		}
		//health bar
//		g.setColor(Color.RED);
//		g.fillRect((int) x, (int) (y+collision.getRadius()*2+5), (int) collision.getRadius()*2, 10);
//		
//		g.setColor(Color.GREEN);
//		g.fillRect((int) x, (int) (y+collision.getRadius()*2+5), (int) (collision.getRadius()*2*(health/maxHealth)), 10);

	}

	@Override
	public void tick() {
		if(System.currentTimeMillis() > warningTime){
			x += velX;
			y += velY;
		}
		collision.setX(x);
		collision.setY(y);
		if(collision.hasCollision(GameScreen.earth)){
			GameScreen.damageEarth((int)health*3);
			Game.playSound("METEOR1.wav", false, -20);
			destroy();
		}
		rotation += rotationSpeed;
	}
	
	public void damage(){
		health--;
		if(health <= 0){
			destroy();
			Game.playSound("METEOR1.wav", false, -20);
		}
	}
	
	public void destroy(){
		GameScreen.entities.add(new Explosion((int) collision.getXCenter(), (int) collision.getYCenter()));
		GameScreen.entities.remove(this);
	}
	

}
