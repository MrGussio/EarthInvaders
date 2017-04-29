package ga.gussio.ld38;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.prefs.Preferences;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import ga.gussio.ld38.screen.GameScreen;
import ga.gussio.ld38.screen.MenuScreen;
import ga.gussio.ld38.screen.Screen;
import ga.gussio.ld38.screen.SplashScreen;

public class Game extends Canvas implements Runnable, MouseListener{

	private static final long serialVersionUID = 1L;
	public final static int WIDTH = 1280, HEIGHT = 720;
	public final static String TITLE = "Earth Invaders";
	private JFrame frame;
	
	private static Screen currentScreen;
	private static InputHandler input;
	
	private boolean running = false;
	private Thread thread;
	
	private static MenuScreen menu;
	
	private static CopyOnWriteArrayList<MouseInput> listeners = new CopyOnWriteArrayList<MouseInput>();
	private static CopyOnWriteArrayList<Clip> playing = new CopyOnWriteArrayList<Clip>();
	public Game(){
		frame = new JFrame();
		frame.setResizable(false);
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
		setMinimumSize(new Dimension(WIDTH, HEIGHT));

		frame.setSize(new Dimension(WIDTH, HEIGHT));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(TITLE);
		frame.add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		ImageIcon icon = new ImageIcon(Game.class.getClassLoader().getResource("ga/gussio/ld38/res/ico.png"));
		frame.setIconImage(icon.getImage());

		
		input = new InputHandler(KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_W);
		addKeyListener(input);
		addMouseListener(this);
		menu = new MenuScreen();
		currentScreen = new SplashScreen(menu);
		listeners.add(menu);
		
		try {
		     GraphicsEnvironment ge = 
		         GraphicsEnvironment.getLocalGraphicsEnvironment();
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Game.class.getClassLoader().getResourceAsStream("ga/gussio/ld38/res/fonts/Raleway-Medium.ttf")));
				
		} catch (IOException|FontFormatException e) {
		     e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		Game game = new Game();
		game.start();

	}
	
	public synchronized void start(){
		if(!running){
			thread = new Thread(this);
			thread.start();
			running = true;
		}
	}

	@Override
	public void run() {
		long lastTick = System.nanoTime();
		double ticks = 60.0;
		double ns = 1000000000 / ticks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int fps = 0;
		int updates = 0;
		
		while(running){
			long currentTick = System.nanoTime();
			delta += (currentTick - lastTick)/ns;
			lastTick = currentTick;
			if(delta >= 1){
				tick();
				updates++;
				delta--;
			}
			render();
			fps++;
			if(System.currentTimeMillis() - 1000 > timer){
				timer += 1000;
				System.out.println("FPS: "+fps+" Tick: "+updates);
				fps = 0;
				updates = 0;
			}
		}
		
	}
	private void tick() {
		if(currentScreen != null)
			currentScreen.tick();
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		if(currentScreen != null)
			currentScreen.render(g);
		g.dispose();
		bs.show();	
	}
	
	public static synchronized Clip playSound(final String url, boolean loop, float deltaVolume) {
		  new Thread(new Runnable() {
			  public void run() {
				  try {
					  Clip clip = AudioSystem.getClip();
					  AudioInputStream inputStream = AudioSystem.getAudioInputStream(
			          getClass().getClassLoader().getResource("ga/gussio/ld38/res/sounds/" + url));
					  clip.open(inputStream);
					  clip.start();
					  FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
					  volume.setValue(deltaVolume);
					  if(loop)
						  clip.loop(Clip.LOOP_CONTINUOUSLY);
					  playing.add(clip);
			      } catch (Exception e) {
			    	  e.printStackTrace();
			      }
			  }
		}).start();
		return null;
	}
	
	public static void setCurrentScreen(Screen s){
		currentScreen = s;
		listeners.clear();
		listeners.add((MouseInput) s);
		for(Clip c : playing){
			c.stop();
			c.close();
		}
		playing.clear();
	}
	
	public static Screen getCurrentScreen(){
		return currentScreen;
	}
	
	public static GameScreen generateNewGameScreen(){
		GameScreen gs = new GameScreen(input);
		return gs;
	}
	
	public static void setMenuScreen(){
		setCurrentScreen(menu);
	}
	
	public static int getHighscore(){
		Preferences pref = Preferences.userNodeForPackage(Game.class);
		return pref.getInt("highscore", 0);
	}
	
	public static void saveHighscore(int highscore){
		Preferences pref = Preferences.userNodeForPackage(Game.class);
		if(getHighscore() < highscore)
			pref.putInt("highscore", highscore);
	}
	
	
	public static void drawCenteredString(Graphics g, String text, int y, Rectangle rect, Font font) {
	    FontMetrics metrics = g.getFontMetrics(font);
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    g.setFont(font);
	    g.drawString(text, x, y);
	}
	
	public static void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
	    FontMetrics metrics = g.getFontMetrics(font);
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
	    g.setFont(font);
	    g.drawString(text, x, y);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		for(MouseInput in : listeners){
			in.mouseClicked(e);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for(MouseInput in : listeners){
			in.mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for(MouseInput in : listeners){
			in.mouseReleased(e);
		}
	}
}
