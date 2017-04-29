package ga.gussio.ld38.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import ga.gussio.ld38.Game;
import ga.gussio.ld38.MouseInput;
import ga.gussio.ld38.buttons.Button;
import ga.gussio.ld38.buttons.CenteredButton;
import ga.gussio.ld38.entities.particles.Particle;

public class MenuScreen extends Screen implements MouseInput{
	
	public Button play, exit, credits;
	public BufferedImage logo;
	private Particle[] background;
	
	private Font subtitle, copyright;
	
	public MenuScreen(){
			play = new CenteredButton(300, "ga/gussio/ld38/res/buttons/play.png");
			credits = new CenteredButton(400,"ga/gussio/ld38/res/buttons/credits.png");
			exit = new CenteredButton(500, "ga/gussio/ld38/res/buttons/exit2.png");
			try {
				logo = ImageIO.read(Game.class.getClassLoader().getResource("ga/gussio/ld38/res/logo.png"));
			} catch (IOException e){
				e.printStackTrace();
			}
			play.setScale(0.65f);
			credits.setScale(0.65f);
			exit.setScale(0.65f);
			
			Random r = new Random();
			background = new Particle[r.nextInt(55-45)+45];
			for(int i = 0; i < background.length; i++){
				int size = r.nextInt(4)+1;
				int x = r.nextInt(Game.WIDTH);
				int y = r.nextInt(Game.HEIGHT);
				background[i] = new Particle(x, y, 0, 0, -1, new Color(207, 187, 20), size);
			}
			
			subtitle = new Font("Raleway Medium", Font.PLAIN, 24);
			copyright = new Font("Raleway Medium", Font.PLAIN, 15);
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(23, 23, 23));
		g2d.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		for(int i = 0; i < background.length; i++){
			background[i].render(g);
		}
		
		g.drawImage(logo, Game.WIDTH/2-(logo.getWidth()*9/2)/2, 100, logo.getWidth()*9/2, logo.getHeight()*9/2, null);
		g.setFont(subtitle);
		g.setColor(Color.GRAY);
		g.drawString("Made for LD38!", 670, 275);
		g.setFont(copyright);
		g.setColor(Color.WHITE);
		g.drawString("© Copyright By Gussio. All rights reserved.", 985, 715);
		play.render(g);
		credits.render(g);
		exit.render(g);
	}

	@Override
	public void tick() {
		if(play.clicked){
			play.clicked = false;
			Game.setCurrentScreen(Game.generateNewGameScreen());
		}
		if(credits.clicked){
			credits.clicked = false;
			Game.setCurrentScreen(new CreditsScreen());
		}
		if(exit.clicked){
			System.exit(0);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(Game.getCurrentScreen() == this){
			play.click(e.getPoint());
			exit.click(e.getPoint());
			credits.click(e.getPoint());
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
		logo.getGraphics().dispose();
		play.dispose();
		credits.dispose();
		exit.dispose();
	}

}
