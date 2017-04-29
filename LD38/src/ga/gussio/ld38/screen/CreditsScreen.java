package ga.gussio.ld38.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import ga.gussio.ld38.Game;
import ga.gussio.ld38.MouseInput;
import ga.gussio.ld38.buttons.Button;
import ga.gussio.ld38.buttons.CenteredButton;

public class CreditsScreen extends Screen implements MouseInput{

	private Font title, text;
	private BufferedImage logo;
	private Button back;
	
	public CreditsScreen(){
		title = new Font("Raleway Medium", Font.PLAIN, 30);
		text = new Font("Raleway Medium", Font.PLAIN, 22);
		back = new CenteredButton(600, "ga/gussio/ld38/res/buttons/back.png");
		back.setScale(0.7f);
		try {
			logo = ImageIO.read(Game.class.getClassLoader().getResource("ga/gussio/ld38/res/logo.png"));
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void render(Graphics g) {
		g.setColor(new Color(23, 23, 23));
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		g.drawImage(logo, Game.WIDTH/2-(logo.getWidth()*9/2)/2, 50, logo.getWidth()*9/2, logo.getHeight()*9/2, null);
		Rectangle screen = new Rectangle(0, 0, Game.WIDTH, Game.HEIGHT);
		g.setColor(Color.WHITE);
		Game.drawCenteredString(g, "A game made for Ludum Dare 38 within 72 hours", 250, screen, title);
		Game.drawCenteredString(g, "Ludum Dare is a game jam that happens 3 times a year.", 275, screen, text);
		Game.drawCenteredString(g, "Every jam, there's a different theme. The theme this time was \"A Small World\".", 295, screen, text);
		Game.drawCenteredString(g, "The game is made by Jelle \"MrGussio\" (Developer), Maurice \"Maus\" (Graphics)", 315, screen, text);
		Game.drawCenteredString(g, "and Tigo \"tigokok\" (Gave ideas and music)", 335, screen, text);
		Game.drawCenteredString(g, "Controls:", 400, screen, title);
		Game.drawCenteredString(g, "Use A and D to move, press W to fire.", 417, screen, text);
		Game.drawCenteredString(g, "For more info, please visit http://gussio.ga/", 500, screen, text);
		
		back.render(g);
	}

	@Override
	public void tick() {
		if(back.clicked){
			back.clicked = false;
			dispose();
			Game.setMenuScreen();
		}
	}

	@Override
	public void dispose() {
		back.dispose();
		logo.getGraphics().dispose();
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		back.click(e.getPoint());
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
