/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 20, 2004
 */
package widgets;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class GUIImage extends JComponent
{
	private Image	img;
	private int		w, h;
	public GUIImage(Image file)
	{
		super();
		img = file;
		ImageIcon tester = new ImageIcon();
		tester.setImage(img);
		w = tester.getIconWidth();
		h = tester.getIconHeight();
		Dimension d = new Dimension(w, h);
		this.setPreferredSize(d);
	}
	public void paint(Graphics g)
	{
		super.paint(g);
		g.drawImage(img, 0, 0, null);
	}
}