import java.awt.event.*;
/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 17, 2004
 */

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class InputManager implements MouseListener, MouseMotionListener
{
	private static int mouseDragStart_y;
	private static boolean initialized = false;
	private static double cdx, cdy, cvx, cvy, mdx, mdy;
	private static final double SPEED_X = 3.0;
	private static final double SPEED_Y = 3.0;
	private static int max_x, max_y;
	/**
	 * returns an instance of the InputManager singleton, only usefull if the
	 * input manager has been initialized previously with a call to
	 * InputManager(int, int).
	 *  
	 */
	public InputManager()
	{

	}
	/**
	 * Initializes the input manager on the first call of this constructor.
	 * Subsequent calls do nothing more than calling InputManager()
	 * 
	 * @param mx
	 *            the maximum width of the input area
	 * @param my
	 *            the maximum height of the input area
	 */
	public InputManager(int mx, int my)
	{
		if (!initialized)
		{
			cvx = 0;
			cvy = 0;
			cdx = 400;
			cdy = 300;
			max_x = mx;
			max_y = my;
			initialized = true;
		}
	}
	/**
	 * tracks the gun drawing process (unholstering, not painting)
	 * 
	 * @param me
	 *            the mouse event information
	 */
	public void mouseDragged(MouseEvent me)
	{
		Gun gun = new PlayerCharacter().getGun();
		if (gun.getGunState() == Gun.GUN_HOLSTERED)
		{
			gun.incGunState();
			mouseDragStart_y = me.getY();
		}
		else if (gun.getGunState() == Gun.PULL_STARTED
				&& me.getY() - mouseDragStart_y >= 100)
		{
			gun.incGunState();
			mouseDragStart_y = me.getY();
		}
		else if (gun.getGunState() == Gun.GUN_PULLED
				&& mouseDragStart_y - me.getY() >= 100)
		{
			gun.incGunState();
			cvx = Math.random() * 20 - 10;
			cvy = Math.random() * 20 - 10;
			mouseDragStart_y = me.getY();
		}
		mdx = me.getX();
		mdy = me.getY();
	}
	/**
	 * Pull percentage is the progress towards pulling the weapon from the
	 * holster.
	 * 
	 * @return the difference between the current pull and the necessary pull,
	 *         as a percentage
	 */
	public double getPullPercentage()
	{
		return (mdy - mouseDragStart_y) / 100.0;
	}
	/**
	 * handles different operations involving the gun Fireing, reloading, and
	 * holstering
	 * 
	 * @param arg0
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent arg0)
	{
		PlayerCharacter player = new PlayerCharacter();
		int state = player.getState();
		if (arg0.getButton() == MouseEvent.BUTTON1)
		{
			if (state == PlayerCharacter.READY_TO_FIRE)
			{
				player.pullTrigger((int) cdx, (int) cdy);
				cdy -= 25;
			}
			else if (state == PlayerCharacter.WAITING)
			{
				mouseDragStart_y = arg0.getY();
			}
		}
		else if (arg0.getButton() == MouseEvent.BUTTON3)
		{
			player.getGun().setGunState(Gun.GUN_HOLSTERED);
		}
		else if (state == PlayerCharacter.WAITING)
		{
			player.getGun().reload();
		}
	}
	/**
	 * If the mouse is released before the gun can be completely pulled, then
	 * the gun is returned to the holster, and back to the start pull state.
	 * 
	 * @author capn_midnight
	 * @param arg0
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent arg0)
	{
		Gun gun = new PlayerCharacter().getGun();
		if (gun.getGunState() != Gun.GUN_DRAWN)
			gun.setGunState(Gun.GUN_HOLSTERED);
	}
	/**
	 * empty
	 * 
	 * @param arg0
	 */
	public void mouseEntered(MouseEvent arg0)
	{
		// do nothing
	}

	/**
	 * empty
	 * 
	 * @param arg0
	 */
	public void mouseExited(MouseEvent arg0)
	{
		// do nothing
	}
	/**
	 * 
	 * @author capn_midnight
	 * @param arg0
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent arg0)
	{
		//do nothing
	}

	/**
	 * Updating the current location of the mouse, for use with updating the
	 * location of the cursor.
	 * 
	 * @author capn_midnight
	 * @param me
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent me)
	{
		double oldx, oldy, dx, dy;
		oldx = mdx;
		oldy = mdy;
		mdx = me.getX();
		mdy = me.getY();
		dx = mdx - oldx;
		dy = mdy - oldy;
		cvx += dx;
		cvy += dy;
	}
	/**
	 * the cursor moves in a different manner depending on the state of the gun.
	 * If the gun is holstered, then the cursor follows the mouse. If the gun is
	 * drawn, then the cursor's velocity is controlled by movements of the
	 * mouse. This is a design decision intended to make the process of drawing
	 * and fireing more difficult than just "point and click" and give the game
	 * a skill element.
	 */
	public void move()
	{
		if (new PlayerCharacter().getGun().getGunState() == Gun.GUN_DRAWN)
		{
			cdx += cvx / SPEED_X;
			cdy += cvy / SPEED_Y;

			if (cdx > max_x)
				cdx = max_x;
			else if (cdx < 0)
				cdx = 0;
			if (cdy > max_y)
				cdy = max_y;
			else if (cdy < 25)
				cdy = 25;
		}
		else
		{
			cdx = mdx;
			cdy = mdy;
		}
	}

	/**
	 * 
	 * @return the current change in the x location of the mouse since the last
	 *         call to move()
	 */
	public int getCdx()
	{
		return (int) cdx;
	}
	/**
	 * 
	 * @return the current change in the y location of the mouse since the last
	 *         call to move()
	 */
	public int getCdy()
	{
		return (int) cdy;
	}
	/**
	 * reset the input manager to an unitialized state.
	 *  
	 */
	public void stop()
	{
		initialized = false;
	}
}