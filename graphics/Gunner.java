
package graphics;


/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 8, 2004
 */

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class Gunner extends MovingTarget
{
	/**
	 * The main constructor for the Gunner class.
	 * 
	 * @author capn_midnight
	 * @param imageFileName
	 *            pretty self explanetory
	 * @param lx
	 *            x coordinate of the gunner
	 * @param ly
	 *            y coordinate of the gunner
	 * @param sx
	 *            x coord of the sun
	 */
	public Gunner(String imageFileName, int lx, int ly, int sx)
	{
		super(imageFileName, lx, ly, sx);
	}
	protected void move(){
		//do nothing
	}
	protected boolean done(){
		return true;
	}
}