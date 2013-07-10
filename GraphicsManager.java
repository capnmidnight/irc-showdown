/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 17, 2004
 */
import graphics.*;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;

import network.NetCharacter;

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class GraphicsManager implements Painter, WindowListener
{
	private static Image back, bullet, gs[];
	private static ImageIcon cursor;
	private static SceneObjects scene;
	private static final int GUN_IMG_X = 10;
	private static final int GUN_IMG_Y = 360;
	private static final int BUL_IMG_X = 10;
	private static final int BUL_IMG_Y = 520;
	private static final int BUL_IMG_W = 25;
	private static DrawingWindow window;
	private static boolean initialized = false;
	private static int shake, shakeMag;
	private static int countDown, countUp;
	/**
	 * Main constructor for the GraphicsManager singleton. The first time it is
	 * called, it creates a new drawing window and loads all the necessary
	 * images.
	 *  
	 */
	public GraphicsManager()
	{
		if (!initialized)
		{
			window = new DrawingWindow("Duel", this, this);
			back = ImageLoader.getImage("back.jpg");
			bullet = ImageLoader.getImage("bullet.png");

			gs = new Image[4];
			gs[0] = ImageLoader.getImage("gunstate0.png");
			gs[1] = ImageLoader.getImage("gunstate1.png");
			gs[2] = ImageLoader.getImage("gunstate2.png");
			gs[3] = ImageLoader.getImage("gunstate3.png");

			cursor = new ImageIcon(ImageLoader.getImage("cross.png"));

			scene = new SceneObjects();
			scene.add(new Tumbleweed("tumbleweed.png", 0,
					window.getHeight() - 200, 700));
			scene.add(new Cactus("cactus.png", 250, window.getHeight() - 325,
					700));
			countDown = 3;
			countUp = 0;
			setListeners();
			initialized = true;
			window.start();
		}
	}
	private void setListeners()
	{
		InputManager im = new InputManager();
		window.getWindow().addMouseMotionListener(im);
		window.getWindow().addMouseListener(im);
	}
	/**
	 * the rendering method
	 * 
	 * @param g
	 */
	public void paint(Graphics2D g)
	{
		NetCharacter net = new NetCharacter();
		PlayerCharacter player = new PlayerCharacter();
		InputManager input = new InputManager();
		input.move();

		Gun gun = player.getGun();
		int gunstate = player.getGun().getGunState();
		if (shake > 0)
		{
			double x = shakeMag * (Math.cos(shake) - Math.cos(4 * shake) / 2);
			double y = shakeMag * (Math.sin(shake) - Math.sin(3 * shake) / 2);
			g.translate(x, y);
			shake--;
		}
		//draw the background
		g.drawImage(back, 0, 0, window.getWindow());

		//draw the objects in the scene
		scene.paint(g);
		net.getAvatar().paint(g);
		//draw the ammo indicator

		for (int i = 0; i < gun.getShots(); i++)
		{
			g.drawImage(bullet, BUL_IMG_X + i * BUL_IMG_W, BUL_IMG_Y, window
					.getWindow());
		}

		g.setFont(Font.decode("Times New Roman-PLAIN-70"));
		g.setColor(Color.RED);

		//draw the countdown to play
		if (countDown > 0)
		{
			g.drawString(Integer.toString(countDown), 575, 400);
			++countUp;
			if (countUp > 25)
			{
				--countDown;
				countUp = 0;
			}
		}
		else if (countDown == 0)
		{
			g.drawString("DRAW", 500, 402);
			++countUp;
			if (countUp > 10)
			{
				--countDown;
				countUp = 0;
			}
		}
		else
		{

			//draw the gun holster state indicator
			g.drawImage(gs[gunstate], GUN_IMG_X, GUN_IMG_Y, window.getWindow());

			g.drawImage(cursor.getImage(), input.getCdx()
					- cursor.getIconWidth() / 2, input.getCdy()
					- cursor.getIconHeight() / 2, window.getWindow());

			//Draw pull indicator
			g.setStroke(new BasicStroke(2.0f));
			g.setColor(Color.WHITE);
			g.drawRect(GUN_IMG_X + 145, GUN_IMG_Y + 25, 10, 100);
			g.setColor(Color.BLUE);
			g.drawLine(GUN_IMG_X + 145, GUN_IMG_Y + 75, GUN_IMG_X + 155,
					GUN_IMG_Y + 75);
			if (gunstate > Gun.GUN_HOLSTERED && gunstate < Gun.GUN_DRAWN)
			{
				g.setStroke(new BasicStroke(10.0f));
				g.setColor(Color.RED);
				g
						.drawLine(
								GUN_IMG_X + 150,
								GUN_IMG_Y + 75,
								GUN_IMG_X + 150,
								GUN_IMG_Y
										+ 75
										+ (int) (input.getPullPercentage() * 45));
			}
		}
	}

	/**
	 * sets the parameters for making the screen shake
	 * 
	 * @param mag
	 *            the magnitude of the shake
	 */
	public void Shake(int mag)
	{
		shake = 10;
		shakeMag = mag;
	}
	/**
	 * 
	 * @return a list of the images that are visible in the current timeslice
	 *         for the current scene.
	 */
	public SceneObjects getScene()
	{
		return scene;
	}
	/**
	 * Ensures that the window is halted properly before it is hidden
	 *  
	 */
	public void stop()
	{
		initialized = false;
		window.stop();
	}
	/**
	 * Ensures that the game is properly stopped if the game window is closed
	 * 
	 * @param arg0
	 *            unused
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent arg0)
	{
		Duel.stop(0);
	}
	/**
	 * empty
	 * 
	 * @param arg0
	 *            unused
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowActivated(WindowEvent arg0)
	{
	}
	/**
	 * empty
	 * 
	 * @param arg0
	 *            unused
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowClosed(WindowEvent arg0)
	{
	}

	/**
	 * empty
	 * 
	 * @param arg0
	 *            unused
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowDeactivated(WindowEvent arg0)
	{
	}
	/**
	 * empty
	 * 
	 * @param arg0
	 *            unused
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowDeiconified(WindowEvent arg0)
	{
	}
	/**
	 * empty
	 * 
	 * @param arg0
	 *            unused
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowIconified(WindowEvent arg0)
	{
	}
	/**
	 * empty
	 * 
	 * @param arg0
	 *            unused
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowOpened(WindowEvent arg0)
	{
	}
}