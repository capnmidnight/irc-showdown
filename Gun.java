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
public interface Gun
{
	/**
	 * start position
	 */
	public static final int GUN_HOLSTERED = 0;
	/**
	 * gun grabbed
	 */
	public static final int PULL_STARTED = 1;
	/**
	 * gun halfway out of holster
	 */
	public static final int GUN_PULLED = 2;
	/**
	 * gun out of holster and ready to fire
	 */
	public static final int GUN_DRAWN = 3;
	/**
	 * pulling the trigger on a gun, sending the shot information to the network
	 * connection
	 * 
	 * @param x
	 *            coordinate component
	 * @param y
	 *            coordinate componen
	 */
	public void fire(int x, int y);
	/**
	 * play the shot sound, update the scene
	 * 
	 * @param x
	 * @param y
	 */
	public void shot(int x, int y);
	/**
	 * 
	 * @return one of the GUN_ IDs
	 * 
	 * <pre>
	 *  start position = GUN_HOLSTERED = 0;
	 *  gun grabbed =  PULL_STARTED = 1;
	 *  gun halfway out of holster = GUN_PULLED = 2; 
	 *  gun out of holster and ready to fire = GUN_DRAWN = 3;
	 * </pre>
	 *  
	 */
	public int getGunState();
	/**
	 * Set the gun state, used for reholstering the weapon.
	 * 
	 * @param gunState
	 *            one of the GUN_ IDs
	 * 
	 * <pre>
	 *  start position = GUN_HOLSTERED = 0;
	 *  gun grabbed =  PULL_STARTED = 1;
	 *  gun halfway out of holster = GUN_PULLED = 2; 
	 *  gun out of holster and ready to fire = GUN_DRAWN = 3;
	 * </pre>
	 */
	public void setGunState(int gunState);
	/**
	 * incrementing the gun state to the next position. Utilized during the
	 * process of drawwing the weapon
	 *  
	 */
	public void incGunState();
	/**
	 * 
	 * @return the number of bullets left in the weapon
	 */
	public int getShots();
	/**
	 * decrement the number of shots left in the weapon, because a shot was
	 * fired
	 *  
	 */
	public void decShots();
	/**
	 * reload the weapon.
	 *  
	 */
	public void reload();
}