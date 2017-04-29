package ga.gussio.ld38.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import ga.gussio.ld38.Game;
import ga.gussio.ld38.InputHandler;
import ga.gussio.ld38.screen.GameScreen;

public class Player extends Entity {

	public int size = 30;
	private double radius = GameScreen.earth.getRadius()-3;
	private double angle = Math.toRadians(180);
	
	private int maxShootInterval = 20;
	private int shootInterval = maxShootInterval;
	
	private InputHandler input;
	
	private BufferedImage img;
	
	public Player(InputHandler input) {
		super(0, 0);
		this.x = (float) ((Game.WIDTH/2) + radius * Math.sin(0));
		this.y = (float) ((Game.HEIGHT/2) + radius * Math.cos(0));
		this.input = input;
		try {
			img = ImageIO.read(Game.class.getClassLoader().getResource("ga/gussio/ld38/res/player.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Color.RED);
		g2d.rotate(-angle, x, y);
//		g2d.fillRect((int) x-size/2, (int) y, size, size);
		g2d.drawImage(img, (int) x-size/2, (int) y, size, size, null);
		g2d.rotate(+angle, x, y);
	}

	@Override
	public void tick() {
		x = (int)((Game.WIDTH/2) + radius * Math.sin(angle));
		y = (int)((Game.HEIGHT/2) + radius * Math.cos(angle));
		handleMoves();
		
		if(shootInterval != maxShootInterval){
			shootInterval--;
			if(shootInterval <= 0){
				shootInterval = maxShootInterval;
			}
		}
	}
	public void handleMoves(){
		if(input.left){
			angle = Math.toRadians(Math.toDegrees(angle)+1);
		}else if(input.right){
			angle = Math.toRadians(Math.toDegrees(angle)-1);
		}
		
		if(input.shoot){
			if(shootInterval == maxShootInterval){
			GameScreen.entities.add(new Bullet(Game.WIDTH/2, Game.HEIGHT/2, angle));
			Random rd = new Random();
			Game.playSound("SHOOT"+(rd.nextInt(4)+1)+".wav", false, -20);
			shootInterval--;
			}
		}
	}
	

}
