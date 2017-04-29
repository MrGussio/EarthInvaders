package ga.gussio.ld38.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;

import ga.gussio.ld38.Game;
import ga.gussio.ld38.InputHandler;
import ga.gussio.ld38.MouseInput;
import ga.gussio.ld38.buttons.Button;
import ga.gussio.ld38.entities.Entity;
import ga.gussio.ld38.entities.Meteorite;
import ga.gussio.ld38.entities.Player;
import ga.gussio.ld38.entities.particles.Particle;
import ga.gussio.ld38.math.Circle;

public class GameScreen extends Screen implements MouseInput{
	
	public static Circle earth;
	private Player player;
	public Rectangle rect;
	public InputHandler input;
	private BufferedImage earthImg;
	private BufferedImage[] meteorite;
	public static CopyOnWriteArrayList<Entity> entities;
	private Particle[] background;
	private long spawnTimer;
	private long startTime;
	private int spawnFactor = 6000;
	private double maxHealth = 100;
	private double health = maxHealth;
	private Font titleFont, scoreFont;
	private static int score = 0;
	private int scoreTimer = 0;
	private int highscore;
	
	public Button exit, retry;
	
	private static double dmgAnimation = 0;
	
	public GameScreen(InputHandler input){
		this.input = input;
		score = 0;
		earth = new Circle(Game.WIDTH/2-Game.HEIGHT*0.2, Game.HEIGHT/2-Game.HEIGHT*0.2, Game.HEIGHT*0.2);
		player = new Player(input);
		entities = new CopyOnWriteArrayList<Entity>();
		entities.add(player);
		titleFont = new Font("Raleway Medium", Font.PLAIN, 60);
		scoreFont = new Font("Raleway Medium", Font.PLAIN, 30);
		try {
			earthImg = ImageIO.read(Game.class.getClassLoader().getResource("ga/gussio/ld38/res/world.png"));
			BufferedImage full = ImageIO.read(Game.class.getClassLoader().getResource("ga/gussio/ld38/res/meteorite.png"));
			meteorite = new BufferedImage[4];
			for(int i = 0; i < meteorite.length; i++){
				meteorite[i] = full.getSubimage(i*20, 0, 20, 20);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Random r = new Random();
		background = new Particle[r.nextInt(55-45)+45];
		for(int i = 0; i < background.length; i++){
			int size = r.nextInt(4)+1;
			int x = r.nextInt(Game.WIDTH);
			int y = r.nextInt(Game.HEIGHT);
			background[i] = new Particle(x, y, 0, 0, -1, new Color(207, 187, 20), size);
		}
		Game.playSound("zandtrek.wav", true, -10);
		exit = new Button(750, 340, "ga/gussio/ld38/res/buttons/exit.png");
		retry = new Button(370, 340, "ga/gussio/ld38/res/buttons/retry.png");
		
		exit.setScale(0.7f);
		retry.setScale(0.7f);
		startTime = System.currentTimeMillis();
	}	
	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(new Color(23, 23, 23));
		g2d.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		for(int i = 0; i < background.length; i++){
			background[i].render(g);
		}
		//draw earth
		g2d.setColor(Color.WHITE);
//		g2d.fillOval((int) earth.getX(), (int) earth.getY(), (int) earth.getRadius()*2, (int) earth.getRadius()*2);
		g2d.drawImage(earthImg, (int) earth.getX(), (int) earth.getY(), (int) earth.getRadius()*2, (int) earth.getRadius()*2, null);
		
		//healthbar
		g2d.setColor(Color.GRAY);
		g2d.fillRect(10-3, Game.HEIGHT-10-20-3, 175+6, 20+6);
		g2d.setColor(Color.RED);
		g2d.fillRect(10, Game.HEIGHT-10-20, 175, 20);
		g2d.setColor(Color.GREEN);
		g2d.fillRect(10, Game.HEIGHT-10-20, (int) (175*(health/maxHealth)), 20);
		
		//score
		g2d.setColor(Color.WHITE);
		g2d.setFont(scoreFont);
		g2d.drawString("Score: "+score, 195, Game.HEIGHT-10);
		//draw player
		for(Entity e : entities){
			e.render(g);
		}
		
		if(health <= 0){
			g2d.setColor(Color.GRAY);
			Rectangle gobg = new Rectangle((int) (Game.WIDTH*0.5-Game.WIDTH*0.5/2), (int) (Game.HEIGHT*0.5-Game.HEIGHT*0.3), (int) (Game.WIDTH*0.5), (int) (Game.HEIGHT*0.4));
			g2d.fill(gobg);
			g2d.setColor(Color.WHITE);
			Game.drawCenteredString(g2d, "Game over!", (int) (Game.HEIGHT*0.5-Game.HEIGHT*0.3+60), gobg, titleFont);
			Game.drawCenteredString(g2d, "Score: "+score, (int) (Game.HEIGHT*0.5-Game.HEIGHT*0.3+95), gobg, scoreFont);
			Game.drawCenteredString(g2d, "Highscore: "+highscore, (int) (Game.HEIGHT*0.5-Game.HEIGHT*0.3+125), gobg, scoreFont);
			exit.render(g);
			retry.render(g);
		}
	}

	@Override
	public void tick() {
		if(health > 0){
			for(Entity e : entities){
				e.tick();
			}
			if(System.currentTimeMillis() > spawnTimer){
				entities.add(new Meteorite(meteorite));
				long dtime = System.currentTimeMillis()-startTime;
				if(dtime > 10000){
					spawnFactor*=0.8;
					startTime = System.currentTimeMillis();
				}
				spawnTimer = System.currentTimeMillis()+spawnFactor;
			}
			
			if(dmgAnimation > 0){
				dmgAnimation--;
				health--;
				if(health == 0){
					Game.saveHighscore(score);
					highscore = Game.getHighscore();					
				}
			}
			scoreTimer++;
			if(scoreTimer > 60){
				score++;
				scoreTimer = 0;
			}
		}else{
			if(exit.clicked){
				exit.clicked = false;
				Game.setMenuScreen();
			}
			if(retry.clicked){
				retry.clicked = false;
				Game.setCurrentScreen(Game.generateNewGameScreen());
			}
		}
	}
	
	public static void damageEarth(int hits){
		dmgAnimation+=hits;
	}
	
	public static void addScore(int amount){
		score+=amount;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(health <= 0){
			exit.click(e.getPoint());
			retry.click(e.getPoint());
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
	@Override
	public void dispose() {
		earthImg.getGraphics().dispose();
		for(int i = 0; i < meteorite.length; i++){
			meteorite[i].getGraphics().dispose();
		}
		exit.dispose();
		retry.dispose();
	}

}
