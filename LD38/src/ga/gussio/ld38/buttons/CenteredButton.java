package ga.gussio.ld38.buttons;

import java.awt.Graphics;

import ga.gussio.ld38.Game;

public class CenteredButton extends Button {

	public CenteredButton(int y, String filepath) {
		super(0, 0, filepath);
		x = (int) (Game.WIDTH/2-(img.getWidth()*scale)/2);
		this.y = y;
	}
	
	@Override
	public void render(Graphics g){
		g.drawImage(img, x, y, (int) (img.getWidth()*scale), (int) (img.getHeight()*scale), null);
	}
	
	@Override
	public void setScale(float scale){
		this.scale = scale;
		x = (int) (Game.WIDTH/2-(img.getWidth()*scale)/2);
		width = (int) (img.getWidth()*scale);
		height = (int) (img.getHeight()*scale);
		collision.setBounds(x, y, width, height);
	}

}
