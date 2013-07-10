package graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 12, 2004
 */

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class Tumbleweed extends AnimatedSprite
{
	private double	r;
	/**
	 * @author capn_midnight
	 * @param imageFileName
	 * @param lx
	 * @param ly
	 * @param sx
	 */
	public Tumbleweed(String imageFileName, int lx, int ly, int sx)
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
	 * @see MovingTarget#move()
	 */
	protected void move()
	{
		r += 0.1;
		x += 5;
		if (x > 800)
			x = -avatar_w;
	}

	/**
	 * @author capn_midnight
	 * @return
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see MovingTarget#done()
	 */
	protected boolean done()
	{
		return false;
	}
	protected void paintSprite(Graphics2D g)
	{
		AffineTransform trans = new AffineTransform();
		trans.translate(x + avatar_w / 2, y + avatar_h / 2);
		trans.rotate(r);
		trans.translate(-avatar_w / 2, -avatar_h / 2);
		g.drawImage(avatar, trans, null);
	}
	protected void paintShadow(Graphics2D g)
	{
		double dx = ((x + avatar_w / 2.0) - sun_x) / 100;

		AffineTransform trans = new AffineTransform();
		trans.translate(x + 5 + dx * 12, y + 1.25 * avatar_h);
		trans.shear(dx, 0);
		trans.scale(1, -0.5);

		trans.rotate(r);
		trans.translate(-avatar_w / 2, -avatar_h / 2);
		g.drawImage(shadow, trans, null);
	}
}