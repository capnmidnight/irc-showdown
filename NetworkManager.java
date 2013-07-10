/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 17, 2004
 */

import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.JOptionPane;

import network.SocketWrapper;

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class NetworkManager implements Runnable
{
	private static SocketWrapper connection;
	private static boolean initialized = false;
	private static Thread networkWatcher;
    private static boolean running;
	/**
	 * returns an instance of the NetworkManager singleton. This is only useful
	 * if the NetworkManager has been previously initialized by calling
	 * NetworkManager(boolean,String,int)
	 *  
	 */
	public NetworkManager()
	{
		//		if (!initialized)
		//			throw new Exception("Network connection not initialized");
	}
	/**
	 * initializes the network manager. Subsequent calls will do nothing more
	 * than a call to NetworkManager().
	 * 
	 * @param isServer
	 *            wether this connection is to be used as a server or client
	 * @param address
	 *            the address to which to connect or on which to listen
	 * @param port
	 *            the port to which to connect or on which to listen.
	 */
	public NetworkManager(boolean isServer, String address, int port)
	{
		if (!initialized)
		{
			networkWatcher = new Thread(this);
			if (isServer)
			{
				//start listening as a server
				try
				{
					ServerSocket s = new ServerSocket(port);
					connection = new SocketWrapper(s);
					start();
				}
				catch (IOException e)
				{
					JOptionPane.showMessageDialog(null,
							"Server could not be started. " + e.getMessage());
					e.printStackTrace();
				}
			}
			else
			{
				//client code
				//connect
				if (address != null && !address.equals(""))
					connection = new SocketWrapper(address, port);
				else
					connection = null; //For clarification purposes, set the
				// connection to null.
				start();
			}

			//monitor the network connection, and recieve information from it.
			initialized = true;
		}
	}
	/**
	 * the runner thread, monitors the network connection and disperses the
	 * information retrieved from it.
	 *  
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		String temp;
		PlayerCharacter pc = new PlayerCharacter();
		//read only while the connection is actually connected
		while (connection != null && connection.isConnected()
				&& !connection.isClosed() && running)
		{
			//read in the next line
			temp = connection.read();
			if (temp != null)
			{
				if (temp.equals("DEAD"))
				{
					//If the remote character dies, stop the game
					// and report the win
					Duel.stop(1);
				}
				else if (temp.charAt(0) == '(')
				{
					//convert to an integer number, checking for
					String[] parts = temp.split(" ");
					String strX = parts[0].substring(1);
					String strY = parts[1].substring(0, parts[1].length() - 1);
					int x = Integer.parseInt(strX);
					int y = Integer.parseInt(strY);
					//fire the shot, without depleting bullets.
					pc.getShot(x, y);
				}
			}
			if (pc.isDead())
			{
				//If the local character dies, inform the opponent
				connection.send("DEAD");
				Duel.stop(-1);
			}
		}
		//close the socket once the connection is lost
		if (connection != null)
			connection.close();
	}
	/**
	 * after the network connection has been initialized, it must be started so
	 * that the monitoring threads can run.. 
	 */
	public void start()
	{
        running = true;
		networkWatcher.start();
	}
	/**
	 * ensure that the network connection is closed
	 * 
	 * @param i
	 *            the result of the duel
	 */
	public void stop()
	{
		running = false;
		if (connection != null)
			connection.close();
		initialized = false;
	}
	public SocketWrapper getConnection()
	{
		return connection;
	}
}