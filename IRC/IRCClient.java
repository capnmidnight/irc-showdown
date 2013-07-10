package IRC;
import graphics.ImageLoader;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 20, 2004
 */

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class IRCClient extends JFrame implements Runnable, WindowListener
{
	private static IRCDesktop		desktop;
	private static String			nick;
	private static Thread			netWatcher;

	private static IRCChannelList	channels;
	private static IRCConnection	connection;
	private static boolean			initialized	= false;
	private static GameRunner			dueler;
	private static IRCChannel		curChan;
	private static int				wins, losses, draws;
    private static boolean running;
	public IRCClient()
	{
		//do nothing
	}
	public IRCClient(String address, int port, String nickName,
			String userName, String realName, String VERSION, GameRunner duel)
	{
		super("Showdown " + VERSION + " IRC Client");
		if (!initialized && running)
		{
			dueler = duel;
			this.addWindowListener(this);
			this.setIconImage(ImageLoader.getImage("badge.png"));
			this.setBounds(200, 200, 800, 600);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setVisible(true);
			this.getGraphics().drawString("Connecting...", 100, 100);
			desktop = new IRCDesktop();
			this.setContentPane(desktop.getDesktop());

			channels = new IRCChannelList();
			connection = new IRCConnection(address, port);
			nick = nickName;
			String nickCom = "NICK " + nick;
			String userCom = "USER " + userName + " 0 * :" + realName;
			connection.send(nickCom);
			connection.send(userCom);
			/////////////////////////////////////////////
			IRCChannel serverNotice = new IRCChannel("Server Notification",
					false);
			channels.put("notify", serverNotice);
			desktop.getDesktop().add(serverNotice);

			netWatcher = new Thread(this);
            running = true;
			netWatcher.start();
			wins = losses = draws = 0;
			curChan = null;
			initialized = true;
		}
	}
	public void run()
	{
		String temp;

		while (connection.isReady())
		{
			temp = connection.read();
			new IRCMessageFactory().createMessage(temp);
		}
	}
	public void shutdown()
	{
		if (netWatcher != null)
			running = false;
		if (connection != null)
			connection
					.send("QUIT :"
							+ this.getTitle()
							+ ". Available at http://cutter.ship.edu/~smcbeth/showdown/");
		System.exit(1);
	}
	public void windowActivated(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}
	public void windowClosed(WindowEvent arg0)
	{
		// TODO Auto-generated method stub
	}
	public void windowClosing(WindowEvent arg0)
	{
		shutdown();
	}
	public void windowDeactivated(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}
	public void windowDeiconified(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}
	public void windowIconified(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}
	public void windowOpened(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}
	public String getNick()
	{
		return nick;
	}
	public GameRunner getDueler()
	{
		return dueler;
	}
	public static void main(String[] args)
	{
		new IRCClient("", 0, "test", "test", "test", "TEST", null);
		;
	}
	public void result(int i)
	{
		String msg = "PRIVMSG " + curChan + " :" + ((char) 1) + "ACTION ";
		String act = "";
		if (i == -1)
		{
			act += "dies";
			losses++;
		}
		else if (i == 1)
		{
			act += "wins";
			wins++;
		}
		else
		{
			act += "quit";
			draws++;
		}
		int total = wins + losses + draws;
		int perc = wins * 100 / total;
		act += " (w" + wins + ", l" + losses + ", t" + total + ", " + perc
				+ "%)";
		connection.send(msg + act + ((char) 1));
		curChan.addText(nick + " " + act);
	}
	public void setCurChan(IRCChannel chan)
	{
		curChan = chan;
	}
}