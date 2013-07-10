/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Aug 2, 2004
 */
package IRC;

import java.util.HashMap;

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class IRCChannelList
{
	private static HashMap<String, IRCChannel>	channels	= new HashMap<String, IRCChannel>();
	private static boolean	initialized	= false;

	public IRCChannelList()
	{
		if (!initialized)
		{
			channels = new HashMap<String, IRCChannel>();
			initialized = true;
		}
	}
	public void put(String key, IRCChannel text)
	{
		channels.put(key, text);
	}
	public IRCChannel get(String key)
	{
		return channels.get(key);
	}
	public boolean contains(String key)
	{
		return channels.containsKey(key);
	}
	public void remove(String key)
	{
		channels.remove(key);
	}
}