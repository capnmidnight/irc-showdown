/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Aug 2, 2004
 */
package IRC;

import graphics.ImageLoader;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JDesktopPane;

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class IRCDesktop
{
	private static JDesktopPane	desktop;
	private static Image		back;
	private static boolean		initialized	= false;
	public IRCDesktop()
	{
		if (!initialized)
		{
			back = ImageLoader.getImage("back.jpg");
			desktop = new JDesktopPane()
			{
				public void paint(Graphics g)
				{
					super.paint(g);
					g.drawImage(back, 0, 0, this);
					super.paintChildren(g);
				}
			};
			initialized = true;
		}
	}
	public JDesktopPane getDesktop()
	{
		return desktop;
	}
}