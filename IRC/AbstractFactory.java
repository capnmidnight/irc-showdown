/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 30, 2004
 */
package IRC;

import java.util.HashMap;

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public abstract class AbstractFactory
{
	public static HashMap<String, AbstractFactory>	factories;
	public abstract BasicMessage createMessage(String msg);
}