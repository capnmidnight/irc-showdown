/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 20, 2004
 */
package IRC;

import java.io.Serializable;

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class IRCServerInfo implements Serializable
{
	private String	name;
	private int		port;
	public IRCServerInfo(String name, int port)
	{
		this.name = name;
		this.port = port;
	}
	public IRCServerInfo(String name)
	{
		this(name, 6667);
	}
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	public int getPort()
	{
		return port;
	}
}