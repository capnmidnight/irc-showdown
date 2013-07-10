/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 16, 2004
 */
import graphics.ImageLoader;
import graphics.Painter;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.*;

import sound.SoundManager;

/**
 * A basic window to which to draw graphics.
 * 
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class DrawingWindow
{
	/**
	 * the width of the window for drawing
	 */
	public static final int WIN_WIDTH = 800;
	/**
	 * the height of the window for drawing
	 */
	public static final int WIN_HEIGHT = 600;

	private JFrame window;
	private BufferStrategy bs;
	private Timer graphicsTimer;
	private Painter painter;

	/**
	 * The main constructor for the window, creates the window, adds a basic
	 * window event listener, and initializes the drawing thread. Does not show
	 * the window or start the drawing.
	 * 
	 * @param title
	 * @param p
	 *            the thread that runs the painting
	 * @param w
	 *            the object that monitors window events
	 */
	public DrawingWindow(String title, Painter p, WindowListener w)
	{
		window = new JFrame(title);
		window.addWindowListener(w);
		painter = p;
		configureWindow();
		initializeTimers();
	}
	/**
	 * setting the window metrics and hiding the cursor
	 *  
	 */
	private void configureWindow()
	{
		window.setSize(WIN_WIDTH, WIN_HEIGHT);
		window.setIgnoreRepaint(true);
		window.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		window.setIconImage(ImageLoader.getImage("badge.png"));
		window.setResizable(false);

		BufferedImage cursor = new BufferedImage(16, 16,
				BufferedImage.TYPE_INT_ARGB);
		Cursor transparentCursor = Toolkit.getDefaultToolkit()
				.createCustomCursor(cursor, new Point(0, 0),
						"TransparentCursor");
		window.setCursor(transparentCursor);
	}
	/**
	 * double buffering eliminates un-sightly flicker.
	 *  
	 */
	private void setupDoubleBuffering()
	{
		try
		{
			//initializing a front and back buffer, both with hardware
			// acceleration when possible.
			window.createBufferStrategy(2, new BufferCapabilities(
					new ImageCapabilities(true), new ImageCapabilities(true),
					null)); //double buffering
			//and save the buffer strategy for later use
			bs = window.getBufferStrategy();
		}
		catch (Exception e)
		{
			//Since it's so important, don't allow the program to continue if
			// it can't be used.
			JOptionPane.showMessageDialog(null,
					"Double buffering not setup, dieing now...");
			System.exit(-1);
		}
	}
	/**
	 * The window does not paint often enough, nor does it provide the proper
	 * graphics context. Therefore, it is necessary to override the normal paint
	 * method and obtain our own graphcis context within that paint method.
	 *  
	 */
	private void initializeTimers()
	{
		//The painter thread, running at best at 30 fps.

		graphicsTimer = new Timer(33, new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				Graphics2D g = (Graphics2D) bs.getDrawGraphics();
				painter.paint(g);
				bs.show();
			}
		});
	}

	/**
	 * get the window running and start painting. An error will occur if the
	 * window is hidden while the painting thread is running.
	 *  
	 */
	public void start()
	{
		window.setVisible(true);
		//You must make the window visible before you can setup double
		// buffering
		setupDoubleBuffering();
		graphicsTimer.start();
		//play a song
		new SoundManager().loop("song1");
	}
	/**
	 * Stops the window from drawing and hides the window from view. An error
	 * will occur if the window is hidden before the painting thread is stopped.
	 *  
	 */
	public void stop()
	{
		graphicsTimer.stop();
		window.setVisible(false);
	}

	/**
	 * 
	 * @return the width of the drawing window
	 */
	public int getWidth()
	{
		return WIN_WIDTH;
	}
	/**
	 * 
	 * @return the height of the drawing window
	 */
	public int getHeight()
	{
		return WIN_HEIGHT;
	}
	/**
	 * @return JFrame the actual frame on which drawing occurs.
	 */
	public JFrame getWindow()
	{
		return window;
	}
}