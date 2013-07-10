/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 20, 2004
 */
package IRC;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class IRCNetworkInfo implements Serializable, Comparable<Object>
{
	private String		name;
	private ArrayList<IRCServerInfo>	servers;
	public IRCNetworkInfo(String name)
	{
		this.name = name;
		servers = new ArrayList<IRCServerInfo>();
	}
	public void addServer(IRCServerInfo server)
	{
		servers.add(server);
	}
	public IRCServerInfo getServer(int i)
	{
		return servers.get(i);
	}
	public ArrayList<IRCServerInfo> getServerList()
	{
		return servers;
	}
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	public void removeServer(int index)
	{
		if (index >= 0 && index < servers.size())
		{
			servers.remove(index);
		}
	}

	public int compareTo(Object arg0)
	{
		return this.name.compareTo(((IRCNetworkInfo) arg0).name);
	}
}