/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 11, 2004
 */
package graphics;

import javax.swing.ImageIcon;
/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class ShootableSprite extends ShadowedSprite
{
	private int[]			mask;
	private int				mask_w, mask_h;
	/**
	 * red mask denotes a kill zone
	 */
	public static final int	KILL	= 0xffff0000;
	/**
	 * green mask denotes a critical wound zone
	 */
	public static final int	CRIT	= 0xff00ff00;
	/**
	 * blue mask denotes a minor wound zone
	 */
	public static final int	WOUND	= 0xff0000ff;
	/**
	 * black mask denotes a flesh wound zone
	 */
	public static final int	FLESH	= 0xff000000;
	/**
	 * transparent denotes a miss
	 */
	public static final int	MISS	= 0x00ffffff;

	/**
	 * The main constructor for the ShootableSprite class.
	 * 
	 * @author capn_midnight
	 * @param imageFileName
	 *            pretty self explanetory
	 * @param lx
	 *            width of the window the gunner will sit in
	 * @param ly
	 *            height of the window the gunner will sit in
	 * @param sx
	 *            x coord of the sun
	 */
	public ShootableSprite(String imageFileName, int lx, int ly, int sx)
	{
		super(imageFileName, lx, ly, sx);
		//The mask provides the hit zone data
		String tempName = imageFileName
				.substring(0, imageFileName.length() - 4);
		ImageIcon maskIcon = new ImageIcon(ImageLoader.getImage(tempName
				+ "mask.png"));
		mask_w = maskIcon.getIconWidth();
		mask_h = maskIcon.getIconHeight();
		mask = ImageManip.getRaster(maskIcon.getImage(), 0, 0, mask_w, mask_h);
	}

	/**
	 * checks for a hit on the avatar
	 * 
	 * @param x
	 *            the x coordinate ON THE IMAGE of the shot
	 * @param y
	 *            the y coordinate ON THE IMAGE of the shot
	 * @return
	 */
	public int checkHit(int x, int y)
	{
		if (x >= 0 && y >= 0 && x < avatar_w && y < avatar_h)
		{
			//2D to 1D math
			int i = mask[y * mask_w + x];
			if (i != MISS)
			{
				punchHole(x, y);
			}
			return i;
		}
		return -1;
	}
	private void punchHole(int x, int y)
	{
		int[] pix = ImageManip.getRaster(avatar, 0, 0, avatar_w, avatar_h);
		//drawing a 3X3 block that approximates a small hole
		for (int dy = -1; dy <= 1; dy++)
			if (y + dy >= 0 && y + dy < avatar_h)
				for (int dx = -1; dx <= 1; dx++)
					if (x + dx >= 0 && x + dx < avatar_w)
						pix[(y + dy) * avatar_w + (x + dx)] &= ((dx & dy) == -1 || (dx & dy) == 1)
								? 0x7f000000
								: 0x00ffffff;
		avatar = ImageManip.makeImage(pix, avatar_w, avatar_h);
	}
}