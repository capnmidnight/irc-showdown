/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 31, 2004
 */
package IRC;
import graphics.ImageLoader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import widgets.ChooserList;
import widgets.GUIImage;
/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class IRCChannel extends JInternalFrame
		implements
			KeyListener,
			InternalFrameListener
{
	private JTextPane			chan;
	private JScrollPane			scrollChan;
	private JTextField			input;
	private ChooserList			userList;
	private String				chanName;
	private static int			left	= 0;
	private static int			top		= 0;
	private String				userString;
	private ArrayList<String>	backLog;
	private int					backLogCursor;
	public IRCChannel(String channelName, boolean listUsers)
	{
		super(channelName, false, true, false, true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.addInternalFrameListener(this);
		chan = new JTextPane();
		backLog = new ArrayList<String>();
		backLogCursor = 0;
		chan.setEditable(false);
		chan.setBackground(Color.BLACK);
		chan.setForeground(Color.WHITE);
		chanName = channelName;
		scrollChan = new JScrollPane(chan,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.getContentPane().add(scrollChan, BorderLayout.CENTER);
		input = new JTextField();
		input.addKeyListener(this);
		input.requestFocus();
		this.getContentPane().add(input, BorderLayout.SOUTH);
		userList = new ChooserList();
		if(listUsers)
		{
			this.getContentPane()
					.add(userList.getScroller(), BorderLayout.WEST);
			this.setBackground(new Color(198, 156, 109));
			this.getContentPane().add(
					new GUIImage(ImageLoader.getImage("bartop.jpg")),
					BorderLayout.NORTH);
			this.getContentPane().add(
					new GUIImage(ImageLoader.getImage("barright.jpg")),
					BorderLayout.EAST);
			this.setLocation(left, top);
			this.pack();
		}
		else
		{
			this.setBounds(left, top, left + 610, top + 405);
			this.setResizable(true);
		}
		left += 25;
		top += 25;
		left %= 400;
		top %= 300;
		this.show();
	}
	public void keyPressed(KeyEvent arg0)
	{
		int code = arg0.getKeyCode();
		IRCConnection connection = new IRCConnection();
		if(code == KeyEvent.VK_ENTER)
		{
			String text = input.getText();
			backLog.add(0, text);
			String[] parts = text.split(" ");
			if(parts[0].equalsIgnoreCase("/MSG"))
			{
				String msg = "";
				for(int i = 2; i < parts.length; i++)
				{
					msg += parts[i] + " ";
				}
				connection.send("PRIVMSG " + parts[1] + " :" + msg);
				addText("<" + (new IRCClient().getNick()) + "> >" + parts[1]
						+ "< '" + text + "'");;
			}
			else if(parts[0].equalsIgnoreCase("/ME"))
			{
				connection.send("PRIVMSG " + chanName + " :" + ((char) 1)
						+ "ACTION " + text.substring(3) + ((char) 1));
			}
			else if(parts[0].equalsIgnoreCase("/CHALLENGE"))
			{
				connection.send("PRIVMSG " + parts[1] + " :draw");
				connection.send("PRIVMSG " + chanName + " :" + parts[1]
						+ ", you dirty varmint! I'm gonna shoot you!");
				addText("***You have challenged " + parts[1] + "***");
				new Thread(new Runnable()
				{
					public void run()
					{
						new IRCClient().getDueler()
								.start(GameRunner.SERVER, "");
					}
				}).start();
			}
			else if(parts[0].equalsIgnoreCase("/QUIT"))
			{
				new IRCClient().shutdown();
			}
			else if(parts[0].equalsIgnoreCase("/PART"))
			{
				internalFrameClosing(null);
			}
			else if(parts[0].equalsIgnoreCase("/WHOIS"))
			{
				connection.send(makeMessage("WHOIS " + parts[1]));
				addText("looking up info on " + parts[1]);
			}
			else if(parts[0].equalsIgnoreCase("/JOIN"))
			{
				connection.send("JOIN " + parts[1]);
			}
			else if(text.length() > 0)
			{
				connection
						.send(makeMessage("PRIVMSG " + chanName + " :" + text));
				addText("<" + (new IRCClient().getNick()) + "> " + text);
			}
			input.setText("");
		}
		else if(code == KeyEvent.VK_UP && backLogCursor < backLog.size())
		{
			input.setText(backLog.get(backLogCursor));
			++backLogCursor;
		}
		else if(code == KeyEvent.VK_DOWN && backLogCursor > 0)
		{
			--backLogCursor;
			input.setText(backLog.get(backLogCursor));
		}
	}
	public void addText(String str)
	{
		String temp = chan.getText() + "\n" + str;
		chan.setText(temp);
		chan.setCaretPosition(temp.length());
	}
	private String makeMessage(String text)
	{
		return text + "\r\n";
	}
	public void addUser(String user)
	{
		userList.addItem(user);
	}
	public void removeUser(String user)
	{
		int size = userList.getListSize();
		for(int i = 0; i < size; ++i)
		{
			String temp = userList.getItem(i);
			if(temp.equals(user))
			{
				userList.removeItem(i);
				i = size;
			}
		}
	}
	public void prepareUserList(String partialList)
	{
		if(userString == null)
			userString = partialList;
		else
			userString += " " + partialList;
	}
	public void processUserList()
	{
		if(userString != null)
		{
			String[] names = userString.split(" ");
			Arrays.sort(names);
			for(int i = 0; i < names.length; ++i)
			{
				userList.addItem(names[i]);
			}
		}
	}
	public String getChannelName()
	{
		return chanName;
	}
	public void keyReleased(KeyEvent arg0)
	{}
	public void keyTyped(KeyEvent arg0)
	{}
	public void internalFrameActivated(InternalFrameEvent arg0)
	{
		new IRCClient().setCurChan(this);
	}
	public void internalFrameClosed(InternalFrameEvent arg0)
	{}
	public void internalFrameClosing(InternalFrameEvent arg0)
	{
		if(chanName.charAt(0) == '#')
		{
			new IRCConnection().send("PART " + chanName + " :"
					+ new IRCClient().getTitle());
		}
		new IRCChannelList().remove(chanName);
		this.dispose();
		new IRCClient().repaint();
	}
	public void internalFrameDeactivated(InternalFrameEvent arg0)
	{}
	public void internalFrameDeiconified(InternalFrameEvent arg0)
	{}
	public void internalFrameIconified(InternalFrameEvent arg0)
	{}
	public void internalFrameOpened(InternalFrameEvent arg0)
	{
		this.grabFocus();
	}
}