/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 11, 2004
 */
package graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class Sprite
{
	protected Image	avatar;
	protected int	x, y, avatar_w, avatar_h;
	/**
	 * The main constructor for the Sprite class.
	 * 
	 * @author capn_midnight
	 * @param imageFileName
	 *            pretty self explanetory
	 * @param lx
	 *            the x location of the image
	 * @param ly
	 *            the y location of the image
	 */
	public Sprite(String imageFile, int lx, int ly)
	{
		//load the image
		Image tImage = ImageLoader.getImage(imageFile);
		ImageIcon temp = new ImageIcon(tImage);
		//save it in a usuable format
		avatar = temp.getImage();

		//retrieve the images attributes
		avatar_w = temp.getIconWidth();
		avatar_h = temp.getIconHeight();

		//store the image's location on the screen
		x = lx;
		y = ly;
	}

	/**
	 * @return the width of the image
	 */
	public int getWidth()
	{
		return avatar_w;
	}

	/**
	 * @return the height of the image
	 */
	public int getHeight()
	{
		return avatar_h;
	}

	/**
	 * basic
	 * 
	 * @return
	 */
	public int getX()
	{
		return x;
	}
	/**
	 * basic
	 * 
	 * @param x
	 */
	public void setX(int x)
	{
		this.x = x;
	}
	/**
	 * basic
	 * 
	 * @return
	 */
	public int getY()
	{
		return y;
	}
	/**
	 * basic
	 * 
	 * @param y
	 */
	public void setY(int y)
	{
		this.y = y;
	}
	/**
	 * Paints the sprite to the graphics context provided by g
	 * 
	 * @param g
	 *            the graphics context
	 */
	public void paint(Graphics2D g)
	{
		//it's handy to organize it in this way, because later I can override
		// the paintSprite method without overriding the paint method. This
		// becomes a noticeable distinction further down the inheritance
		// hiearchy, when the paint method has been overridden and it's
		// functionallity is still desired.
		paintSprite(g);
	}
	protected void paintSprite(Graphics2D g)
	{
		//the parameters are obvious, except for null. One would pass an
		// ImageObserver in place of null if they wanted to know when the image
		// finished painting. I, quite frankly, don't give a shit.
		g.drawImage(avatar, x, y, null);
	}
}

