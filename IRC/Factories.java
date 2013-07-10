/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Aug 3, 2004
 */
package IRC;

import java.util.HashMap;

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class Factories
{
	public Factories()
	{
		AbstractFactory.factories = new HashMap<String, AbstractFactory>();
		new Join();
		new Default();
		new N353();
		new N363();
		new Part();
		new Ping();
		new PrivMsg();
		new Quit();
	}
	public static class Join extends AbstractFactory
	{
		public Join()
		{
			factories.put("JOIN", this);
		}

		public BasicMessage createMessage(String msg)
		{
			return new JoinMessage(msg);
		}
		public static class JoinMessage extends BasicMessage
		{
			public JoinMessage(String msg)
			{
				super(msg);
				this.normalMessageParse();
				if (!this.getNick().equalsIgnoreCase(new IRCClient().getNick()))
				{
					IRCChannel channel;
					String chanName = this.getParams()[0];
					if (new IRCChannelList().contains(chanName))
					{
						channel = new IRCChannelList().get(chanName);
					}
					else
					{
						channel = new IRCChannel(chanName, true);
						new IRCChannelList().put(chanName, channel);
						new IRCDesktop().getDesktop().add(channel);
					}
					channel.addUser(this.getNick());
					channel.addText(this.getNick() + " has joined");
				}
			}
		}
	}
	public static class Default extends AbstractFactory
	{
		public Default()
		{
			factories.put("default", this);
		}

		public BasicMessage createMessage(String msg)
		{
			return new DefaultMessage(msg);
		}
		public class DefaultMessage extends BasicMessage
		{
			public DefaultMessage(String msg)
			{
				super(msg);
				this.normalMessageParse();
				IRCChannel channel = new IRCChannelList().get("notify");
				channel.addText(this.getIdent() + ": \"" + this.getText()
						+ "\"");
			}
		}
	}
	public static class N353 extends AbstractFactory
	{
		public N353()
		{
			factories.put("353", this);
		}

		public BasicMessage createMessage(String msg)
		{
			return new N353Message(msg);
		}
		public class N353Message extends BasicMessage
		{
			//Names List
			public N353Message(String msg)
			{
				super(msg);
				this.normalMessageParse();
				IRCChannel channel;
				String chanName = this.getParams()[2];
				if (new IRCChannelList().contains(chanName))
				{
					channel = new IRCChannelList().get(chanName);
				}
				else
				{
					channel = new IRCChannel(chanName, true);
					new IRCChannelList().put(chanName, channel);
					new IRCDesktop().getDesktop().add(channel);
				}
				channel.prepareUserList(this.getText());
			}
		}
	}
	public static class N363 extends AbstractFactory
	{
		public N363()
		{
			factories.put("366", this);
		}

		public BasicMessage createMessage(String msg)
		{
			return new N366Message(msg);
		}
		public class N366Message extends BasicMessage
		{
			//End of Names list
			public N366Message(String msg)
			{
				super(msg);
				this.normalMessageParse();
				IRCChannel channel;
				String chanName = this.getParams()[1];
				if (new IRCChannelList().contains(chanName))
				{
					channel = new IRCChannelList().get(chanName);
				}
				else
				{
					channel = new IRCChannel(chanName, true);
					new IRCChannelList().put(chanName, channel);
					new IRCDesktop().getDesktop().add(channel);
				}
				channel.processUserList();
			}
		}
	}
	public static class Part extends AbstractFactory
	{
		public Part()
		{
			factories.put("PART", this);
		}

		public BasicMessage createMessage(String msg)
		{
			return new PartMessage(msg);
		}
		public class PartMessage extends BasicMessage
		{
			public PartMessage(String msg)
			{
				super(msg);
				this.normalMessageParse();
				IRCChannel channel = new IRCChannelList().get(params[0]);
				String nick = this.getNick();
				String user = new IRCClient().getNick();
				if (nick.equalsIgnoreCase(user))
				{
					new IRCChannelList().remove(channel.getChannelName());
					channel.hide();
					new IRCClient().repaint();
				}
				channel.removeUser(this.getNick());
				channel.addText(this.getNick() + " has left: \""
						+ this.getText() + "\"");
			}
		}
	}
	public static class Ping extends AbstractFactory
	{
		public Ping()
		{
			factories.put("PING", this);
		}

		public BasicMessage createMessage(String msg)
		{
			return new PingMessage(msg);
		}
		public class PingMessage extends BasicMessage
		{
			public PingMessage(String msg)
			{
				super(msg);

				String[] parts = message.split(":");
				command = parts[0].substring(0, 4);
				origin = parts[1];

				//reply
				IRCConnection connection = new IRCConnection();
				connection.send("PONG :" + origin);
				if (!connection.isServerNameKnown())
				{
					connection.setServerName(origin);
				}
			}
		}
	}
	public static class PrivMsg extends AbstractFactory
	{
		private static final char	I	= (char) 1;
		public PrivMsg()
		{
			factories.put("PRIVMSG", this);
		}

		public BasicMessage createMessage(String msg)
		{
			return new PrivMessage(msg);
		}
		public class PrivMessage extends BasicMessage
		{
			public PrivMessage(String msg)
			{
				super(msg);
				this.normalMessageParse();
				IRCChannel channel;

				String chanName = this.params[0];
				if (chanName.charAt(0) != '#')
				{
					chanName = this.getNick();
				}

				if (new IRCChannelList().contains(chanName))
				{
					channel = new IRCChannelList().get(chanName);
				}
				else
				{
					channel = new IRCChannel(chanName,
							chanName.charAt(0) == '#');
					new IRCChannelList().put(chanName, channel);
					new IRCDesktop().getDesktop().add(channel);
				}

				String[] parts = this.text.split(" ");
				if (this.text.equalsIgnoreCase("draw"))
				{
					channel.addText(this.getNick() + " has challenged you: "+this.getHost());
					new IRCClient().getDueler().start(GameRunner.CLIENT,
							this.getHost());

				}
				else if (parts[0].equalsIgnoreCase(I + "ACTION"))
				{
					channel.addText(this.getNick()
							+ this.text.substring(7, this.text.length() - 1));
				}
				else
				{
					channel.addText("<" + this.getNick() + "> "
							+ this.getText());
				}
			}
		}
	}
	public static class Quit extends AbstractFactory
	{
		public Quit()
		{
			factories.put("QUIT", this);
		}

		public BasicMessage createMessage(String msg)
		{
			return new QuitMessage(msg);
		}
		public class QuitMessage extends BasicMessage
		{
			public QuitMessage(String msg)
			{
				super(msg);
				this.normalMessageParse();
				IRCChannel channel = new IRCChannelList().get("notify");
				channel.removeUser(this.getNick());
				channel.addText(this.getNick() + " has quit: \""
						+ this.getText() + "\"");
			}
		}
	}

}