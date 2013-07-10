/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 30, 2004
 */
package IRC;

import java.util.ArrayList;

/*
 * A message is separated into three sections, the origin, the command
 * (with parameters) and the text. Both the command and the text begin
 * with a colon (:). Splitting the string into chunks defined by the
 * colon will result in a blank chunk, the message command chunk, and
 * the message text chunk. There is one special case, the PING command,
 * which must be handled separately.
 */
/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class BasicMessage
{
	protected String	message;
	protected String	origin;
	protected String	command;
	protected String[]	params;
	protected String	text;

	/**
	 * breaks a message into its constituent parts so that the type of message
	 * may be determined and the message further processed
	 * 
	 * @author capn_midnight
	 * @param msg
	 *            the raw message from the IRC server
	 */
	public BasicMessage(String msg)
	{
		message = msg;
	}
	protected void normalMessageParse()
	{
		String[] parts = message.split(":");
		/*
		 * We can discard the first chunk, parts[0]
		 */
		/*
		 * If the text contained a colon, then it needs to be pieced back
		 * together and saved as a complete unit.
		 */
		text = parts[parts.length - 1];
		//if there are only three parts, then this for loop will not
		// execute
		for (int i = 3; i < parts.length; i++)
		{
			text += ":" + parts[i];
		}
		/*
		 * The command chunk consists of three parts, the origin, the command,
		 * and the parameters of the command, all seperated by spaces.
		 */
		parts = parts[1].split(" ");
		//first is the origin
		origin = parts[0];
		//second is the command
		command = parts[1].toUpperCase();
		//and finally, the parameters;
		if (parts.length > 2) //are there any params?
		{
			ArrayList<String> temp = new ArrayList<String>();
			for (int i = 2; i < parts.length; ++i)
			{
				temp.add(parts[i]);
			}
			Object[] tempParams = temp.toArray();
			params = new String[tempParams.length];
			for (int i = 0; i < tempParams.length; ++i)
			{
				params[i] = (String) tempParams[i];
			}
		}
	}

	/**
	 * @return the raw message as received from the server
	 */
	public String getRawMessage()
	{
		return message;
	}
	/**
	 * @return the full identity string of the origin of the message
	 */
	public String getIdent()
	{
		return origin;
	}

	/**
	 * @return the command associated with this message
	 */
	public String getCommand()
	{
		return command;
	}
	/**
	 * @return the parameters of the command
	 */
	public String[] getParams()
	{
		return params;
	}
	/**
	 * @return the text portion of the message
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * There are two possible ways that the origin of a message is defined: by
	 * just the user's nickname, or by the user's identity (nick+user+host-name).
	 * The user's host-name contains the nickname and some more information. We
	 * know the host-name is being used when it contains the '!' character, which
	 * Separates the nickname and the DNS address. If the origin of the message
	 * was the IRC server, then getNick returns the DNS address of the server.
	 * 
	 * @return The nick name of the origin of the message
	 */
	public String getNick()
	{
		int i = origin.indexOf('!');
		if (i >= 0)
		{
			return origin.substring(0, i);
		}
		return origin;
	}
	/**
	 * The user name is a secondary identification name used by IRC servers.
	 * Chatters may have different nicknames, but on servers that utilize
	 * authentication, they must always use the same user name. If the origin of
	 * the message is only defined by the nickname (or the origin was from the
	 * IRC server), then this method returns just the nick (or server DNS
	 * address).
	 * 
	 * @return the user name of the origin of the message
	 */
	public String getUser()
	{
		int i = origin.indexOf('!');
		int j = origin.indexOf('@');
		if (i > 0 && j > i)
		{
			return origin.substring(i + 1, j);
		}
		return origin;
	}
	/**
	 * The host name is the actual DNS address of the origin of the message. In
	 * most cases, this address is volatile and will change when a person
	 * reconnects to the Internet (or over time if that person has a persistent
	 * connection). If the origin of the message is only defined by the nickname
	 * (or the origin was from the IRC server), then this method returns just
	 * the nick (or server DNS address).
	 * 
	 * @return the host name of the origin of the message
	 */
	public String getHost()
	{
		int i = origin.indexOf('@');
		if (i > 0)
		{
			return origin.substring(i + 1);
		}
		return origin;
	}
}