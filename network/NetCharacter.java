package network;
import graphics.Gunner;

/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 17, 2004
 */

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class NetCharacter
{
	private static Gunner	avatar;
	private static boolean	initialized	= false;
	public NetCharacter()
	{
		if (!initialized)
		{
			avatar = new Gunner("gunner.png", 550, 325, 700);
			initialized = true;
		}
	}

	public Gunner getAvatar()
	{
		return avatar;
	}
	public void stop()
	{
		initialized = false;
	}
}