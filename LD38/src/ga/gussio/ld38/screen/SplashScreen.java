package ga.gussio.ld38.screen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import ga.gussio.ld38.Game;

public class SplashScreen extends Screen {

	private MenuScreen nextScreen;
	private BufferedImage splash;
	
	private long timer;
	
	public SplashScreen(MenuScreen ms){
		nextScreen = ms;
		timer = System.currentTimeMillis()+3000;
		try {
			splash = ImageIO.read(Game.class.getClassLoader().getResource("ga/gussio/ld38/res/gussio.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		g.drawImage(splash, Game.WIDTH/2-splash.getWidth()*12/2, Game.HEIGHT/2-splash.getHeight()*12/2,(int) splash.getWidth()*12, (int) splash.getHeight()*12, null);
	}

	@Override
	public void tick() {
		if(System.currentTimeMillis() > timer){
			Game.setCurrentScreen(nextScreen);
			dispose();
		}
	}

	@Override
	public void dispose() {
		splash.getGraphics().dispose();
	}

}
