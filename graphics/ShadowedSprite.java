/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 11, 2004
 */
package graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class ShadowedSprite extends Sprite
{
	protected int	sun_x;
	protected Image	shadow;
	/**
	 * The main constructor for the Gunner class.
	 * 
	 * @author capn_midnight
	 * @param imageFileName
	 *            pretty self explanetory
	 * @param lx
	 *            x coord of the object
	 * @param ly
	 *            y coord of the object
	 * @param sx
	 *            x coord of the sun
	 *  
	 */
	public ShadowedSprite(String imageFileName, int lx, int ly, int sx)
	{
		super(imageFileName, lx, ly);
		//store the location of the sun
		sun_x = sx;

		//retrieve the raw image data for the sprite so that it may be editted
		int[] shadowPix = ImageManip
				.getRaster(avatar, 0, 0, avatar_w, avatar_h);

		//normalize all opaque pixels to be semi-transparent and black
		for (int i = 0; i < shadowPix.length; i++)
			shadowPix[i] &= 0xa0000000;

		//save the manipulated image
		shadow = ImageManip.makeImage(shadowPix, avatar_w, avatar_h);
	}
	/**
	 * Paints the sprite and the sprite's shadow to the graphics context
	 * provided by g
	 * 
	 * @param g
	 *            the graphics context
	 */
	public void paint(Graphics2D g)
	{
		super.paint(g);
		paintShadow(g);
	}
	protected void paintShadow(Graphics2D g)
	{
		//used in calculating the direction of the shadow, helping to fake a
		// perspective scene.
		double dx = ((x + avatar_w / 2.0) - sun_x) / 100;
		
		AffineTransform cur = new AffineTransform();
		//4 steps, read them in reverse order

		//4.) put image in place
		cur.translate(x + 5, y + avatar_h - 8);
		//3.) angle the image away from the sun
		cur.shear(dx, 0);
		//2.) flip the image vertically, and fake some perspective
		cur.scale(1, -0.5);
		//1.) center the picture on the origin
		cur.translate(0, -avatar_h);

		g.drawImage(shadow, cur, null);
	}
}