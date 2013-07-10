/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Aug 2, 2004
 */
package IRC;

/**
 * @author capn_midnight <br>
 * Sean T. McBeth  <br>
 * sm8236@ship.edu
 */
public interface GameRunner
{
	public static final int	SERVER	= 0;
	public static final int	CLIENT	= 1;
	public void start(int i, String a);
}
