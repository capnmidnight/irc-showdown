package graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

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
public abstract class AnimatedSprite extends ShadowedSprite
{
	private Timer	t;
	/**
	 * @author capn_midnight
	 * @param imageFileName
	 * @param lx
	 * @param ly
	 * @param sx
	 */
	public AnimatedSprite(String imageFileName, int lx, int ly, int sx)
	{
		super(imageFileName, lx, ly, sx);
		ActionListener al = new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				move();
				if (done())
					t.stop();
			}
		};
		t = new Timer(16, al);
		t.start();
	}
	protected abstract void move();
	protected abstract boolean done();
}