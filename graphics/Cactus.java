/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 13, 2004
 */
package graphics;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class Cactus extends AnimatedSprite
{
	private double	r;
	/**
	 * @author capn_midnight
	 * @param imageFileName
	 * @param lx
	 * @param ly
	 * @param sx
	 */
	public Cactus(String imageFileName, int lx, int ly, int sx)
	{
		super(imageFileName, lx, ly, sx);
		r = 0.0;
	}

	/**
	 * @author capn_midnight
	 *  
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see sprite.MovingTarget#move()
	 */
	protected void move()
	{
		r += 0.1;
	}

	/**
	 * @author capn_midnight
	 * @return
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see sprite.MovingTarget#done()
	 */
	protected boolean done()
	{
		return false;
	}
	protected void paintSprite(Graphics2D g)
	{
		AffineTransform trans = new AffineTransform();
		trans.translate(x, y+avatar_h);
		trans.shear(Math.cos(r)/50, 0);
		trans.translate(0, -avatar_h);
		g.drawImage(avatar, trans, null);
	}
}