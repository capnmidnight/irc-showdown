/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Aug 2, 2004
 */
package IRC;

import network.SocketWrapper;

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class IRCConnection
{
	private static boolean			initialized	= false;
	private static String			serverName;
	private static SocketWrapper	connection;
	public IRCConnection()
	{
		return;
	}
	public IRCConnection(String address, int port)
	{
		if (!initialized)
		{
			connection = new SocketWrapper(address, port);
			serverName = null;
		}
	}
	public void send(String data)
	{
		connection.send(data);
	}
	public String read()
	{
		return connection.read();
	}
	public boolean isServerNameKnown()
	{
		return serverName != null;
	}

	public String getServerName()
	{
		return serverName;
	}
	public void setServerName(String serverName)
	{
		IRCConnection.serverName = serverName;
	}
	public boolean isReady()
	{
		return connection != null && !connection.isClosed()
				&& connection.isConnected();
	}
}