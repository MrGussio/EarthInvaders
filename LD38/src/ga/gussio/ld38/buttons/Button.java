package ga.gussio.ld38.buttons;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import ga.gussio.ld38.Game;

public class Button {

	protected int x, y, width, height;
	protected float scale = 1.0f;
	protected String filepath;
	protected BufferedImage img;
	protected Rectangle collision;
	public boolean clicked;
	
	public Button(int x, int y, String filepath){
		this.x = x;
		this.y = y;
		this.filepath = filepath;
		try {
			img = ImageIO.read(Game.class.getClassLoader().getResource(filepath));
		} catch (IOException e) {
			System.err.println("Couldn't load file "+filepath+" in Button.java.");
		}
		this.width = img.getWidth();
		this.height = img.getHeight();
		collision = new Rectangle(x, y, width, height);
	}
	
	public void render(Graphics g){
		g.drawImage(img, x, y, width, height, null);
	}
	
	public void tick(){
		
	}
	
	public void dispose(){
		img.getGraphics().dispose();
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
		width = (int) (img.getWidth()*scale);
		height = (int) (img.getHeight()*scale);
		collision.setBounds(x, y, width, height);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getFilepath() {
		return filepath;
	}
	
	public void click(Point p){
		if(collision.contains(p)){
			this.clicked = true;
		}
		
	}
	
	
}
