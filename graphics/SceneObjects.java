package graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;


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
public class SceneObjects
{
	private ArrayList<Sprite>	list;
	/**
	 * get the list going
	 * 
	 * @author capn_midnight
	 *  
	 */
	public SceneObjects()
	{
		list = new ArrayList<Sprite>();
	}

	/**
	 * add an image to the scene list
	 * 
	 * @param img
	 */
	public void add(Sprite img)
	{
		list.add(img);
	}
	/**
	 * paint all the sprites in the list
	 * 
	 * @param g
	 */
	public void paint(Graphics2D g)
	{
		for (int i = 0; i < list.size(); i++)
		{
			list.get(i).paint(g);
		}
	}
	/**
	 * collision detection for all objects
	 * 
	 * @param x
	 * @param y
	 * @return index of item hit, -1 on miss
	 */
	public int checkHits(int x, int y)
	{
		for (int i = list.size()-1; i >=0; i--)
		{
			Sprite sp = list.get(i);
			if (sp instanceof ShootableSprite)
			{
				int minX = sp.getX();
				int maxX = minX + sp.getWidth();
				int minY = sp.getY();
				int maxY = minY + sp.getHeight();
				int dx = x - minX;
				int dy = y - minY;
				if (minX < x && x < maxX && minY < y && y < maxY)
				{
					int res=((ShootableSprite) sp).checkHit(dx, dy);
					if (res != ShootableSprite.MISS)
						return res;
				}
			}
		}
		return -1;
	}
}