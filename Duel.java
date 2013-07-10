import network.NetCharacter;
import sound.SoundManager;
import IRC.GameRunner;
import IRC.IRCClient;

/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 21, 2004
 */

/**
 * An encapsulation of all the components of the game, ensuring everything gets
 * started in the right order
 * 
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class Duel implements GameRunner
{
	/**
	 * get all the sub systems running
	 * 
	 * @param type
	 *            the type of network connection to run
	 * @param address
	 *            the network address to use for the network connection
	 */
	public void start(int type, String address)
	{
		//loads sounds and music
		new SoundManager();
		//tracks user input
		new InputManager(DrawingWindow.WIN_WIDTH, DrawingWindow.WIN_HEIGHT);
		//tracks network activity, setups the game server or client, sends
		// information back and forth during the game
		new NetworkManager(type == 0, address, 1337);
		//information regarding the local character
		new PlayerCharacter();
		//information regarding the remote character
		new NetCharacter();
		//loads all images, tracks graphical output
		new GraphicsManager();
	}
	/**
	 * ensure that the game is shutdown properly, reset for the next game.
	 * 
	 * @param i
	 *            the result of the duel
	 */
	public static void stop(int i)
	{
		//stop drawing
		new GraphicsManager().stop();
		//reset the remote player
		new NetCharacter().stop();
		//reset the local player
		new PlayerCharacter().stop();
		//stop monitoring the network connection and close the socket
		new NetworkManager().stop();
		//stop any music that is playing
		new SoundManager().stop("song1");
		//stop monitoring input
		new InputManager().stop();
		//report the result of the duel
		new IRCClient().result(i);
	}
	/**
	 * for testing purposes, allows the game to be started without a network
	 * connection.
	 * 
	 * @param args
	 *            command line arguements, unused
	 */
	public static void main(String[] args)
	{
		new Duel().start(3, "");
	}
}