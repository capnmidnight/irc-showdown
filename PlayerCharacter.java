import graphics.Gunner;
import graphics.ShootableSprite;
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
public class PlayerCharacter
{
	private static Gunner	avatar;
	private static Gun		gun;
	private static boolean	initialized		= false;
	private static int		health;
	public static final int	READY_TO_FIRE	= 2;
	public static final int	DRAWING			= 1;
	public static final int	WAITING			= 0;
	public PlayerCharacter()
	{
		if (!initialized)
		{
			avatar = new Gunner("gunner.png", 550, 325, 700);
			gun = new SixShooter();
			initialized = true;
			health = 100;
		}
	}

	public Gunner getAvatar()
	{
		return avatar;
	}
	public Gun getGun()
	{
		return gun;
	}
	public void pullTrigger(int x, int y)
	{
		if (gun.getShots() > 0)
		{
			gun.fire(x, y);
			gun.decShots();
		}
		else
		{
			new SoundManager().play("miss");
		}
	}
	public int getState()
	{
		int state = gun.getGunState();
		if (state == Gun.GUN_DRAWN && !isDead())
			return READY_TO_FIRE;
		if (state == Gun.GUN_HOLSTERED)
			return WAITING;
		if (state == Gun.GUN_PULLED)
			return DRAWING;
		return WAITING;
	}
	public boolean isDead()
	{
		return health <= 0;
	}
	public void decHealth(int hit)
	{
		//I hate switch:case blocks
		switch (hit)
		{
			case ShootableSprite.KILL :
				health -= 100;
				new GraphicsManager().Shake(15);
				break;
			case ShootableSprite.CRIT :
				health -= 50;
				new GraphicsManager().Shake(7);
				break;
			case ShootableSprite.WOUND :
				health -= 25;
				new GraphicsManager().Shake(4);
				break;
			case ShootableSprite.FLESH :
				health -= 10;
				new GraphicsManager().Shake(1);
				break;
			default :
				;
		}
	}
	public void getShot(int x, int y)
	{
		new SoundManager().play("shot");
		int res = avatar.checkHit(x - avatar.getX(), y - avatar.getY());
		decHealth(res);
	}
	public void stop()
	{
		initialized = false;
	}
}