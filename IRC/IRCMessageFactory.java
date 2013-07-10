/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 30, 2004
 */
package IRC;


/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class IRCMessageFactory
{
	public IRCMessageFactory()
	{
	}
	public BasicMessage createMessage(String msg)
	{
		if (msg != null && msg.length() > 0)
		{
			String com = "";
			try
			{
				com = msg.split(":")[1].split(" ")[1];
			}
			catch (Exception e)
			{
				com = msg.split(" ")[0];
			}
			com = com.toUpperCase();
			BasicMessage bmsg;
			if (AbstractFactory.factories.containsKey(com))
				bmsg = AbstractFactory.factories.get(com).createMessage(msg);
			else
				bmsg =  AbstractFactory.factories.get("default").createMessage(msg);
			return bmsg;
		}
		return null;
	}
}