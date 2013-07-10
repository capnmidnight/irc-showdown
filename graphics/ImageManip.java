package graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

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
public class ImageManip
{
	/**
	 * 
	 * @param img
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	public static int[] getRaster(Image img, int x, int y, int w, int h)
	{
		int[] pix = new int[w * h];
		PixelGrabber pg = new PixelGrabber(img, x, y, w, h, pix, 0, w);
		try
		{
			pg.grabPixels(0);
		}
		catch (Exception e)
		{
			//this exception should never happen, as it only happens on a
			// timeout, and the timeout is disabled with a time of 0
			// milliseconds.
			e.printStackTrace();
		}
		return pix;
	}
	/**
	 * 
	 * @param pix
	 * @param w
	 * @param h
	 * @return
	 */
	public static Image makeImage(int[] pix, int w, int h)
	{
		return Toolkit.getDefaultToolkit().createImage(
				new MemoryImageSource(w, h, pix, 0, w));
	}
}