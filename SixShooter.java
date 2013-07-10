
import network.SocketWrapper;
import sound.SoundManager;

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
public class SixShooter implements Gun
{
	private int	gunState;
	private int	shots;

	public SixShooter()
	{
		gunState = 0;
		shots = 6;
	}
	public int getGunState()
	{
		return gunState;
	}
	public void setGunState(int gunState)
	{
		this.gunState = gunState;
	}
	public void incGunState()
	{
		gunState++;
	}
	public int getShots()
	{
		return shots;
	}
	public void decShots()
	{
		shots--;
	}
	public void reload()
	{
		shots = 6;
	}
	public void fire(int x, int y)
	{
		SocketWrapper connection;
		try
		{
			connection = new NetworkManager().getConnection();
			shot(x, y);
			if (connection != null && connection.isConnected()
					&& !connection.isClosed())
				connection.send("(" + x + " " + y + ")");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public void shot(int x, int y)
	{
		new SoundManager().play("shot");
		//ShootableSprite player = new NetCharacter().getAvatar();
	}
}