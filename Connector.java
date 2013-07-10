/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 20, 2004
 */

import graphics.ImageLoader;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import widgets.*;
import IRC.*;

/**
 * The IRC network selector and user information entry window
 * 
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class Connector extends JFrame implements WindowListener, ActionListener
{
	private ArrayList<IRCNetworkInfo> networks;
	private static final String SER_FILE = "netinfo.list";
	private ChooserList listNet, listServ;
	private LabeledTextField txtNick, txtUser, txtRealName;
	private JButton butConnect, butCancel;
	private ChooserListEditor netEd, servEd;
	/**
	 * the version string, utilized in frame titles across the program
	 */
	public static final String VERSION = "v0.8.1";
	/**
	 * main constructor
	 */
	public Connector()
	{
		super("IRC Network Connector, Showdown " + VERSION);
		//Initialize the message factory for the IRC client
		new Factories();
		//Listens for the window close event to ensure the network list gets
		// saved
		this.addWindowListener(this);
		this.setIconImage(ImageLoader.getImage("badge.png"));

		//Try to set the look and feel
		try
		{
			//find out which look-and-feels are available
			final UIManager.LookAndFeelInfo[] info = UIManager
					.getInstalledLookAndFeels();
			//loop through the list
			for (int i = 0; i < info.length; i++)
			{
				String className = info[i].getClassName();
				String name = info[i].getName();
				//find the look-and-feel for windows
				if (name.equalsIgnoreCase("windows"))
				{
					//and activate it
					UIManager.setLookAndFeel(className);
					SwingUtilities.updateComponentTreeUI(this);
				}
			}
		} catch (Exception e)
		{
			//not that important, don't worry about it
		}

		//well, duh
		getNetworkInfo();

		//the main component panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		createNetworkBox(mainPanel);
		createServerBox(mainPanel);
		createUserBox(mainPanel);

		//the container of the main window
		Container c = this.getContentPane();
		c.add(mainPanel);

		//The Show-down banner
		c.add(new GUIImage(ImageLoader.getImage("banner.jpg")),
				BorderLayout.NORTH);

		//add all of the networks to the network list
		for (int n = 0; n < networks.size(); n++)
		{
			listNet.addItem( networks.get(n).getName());
		}
		listNet.setSelectedIndex(0);

		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	/**
	 * The network box is a panel that contains all of the controls for
	 * selecting and editing the IRC Network list
	 * 
	 * @param c
	 *            the Container to which to add the network box
	 */
	private void createNetworkBox(Container c)
	{
		JPanel panNet = new JPanel();
		panNet.setLayout(new BorderLayout());

		JLabel lblNet = new JLabel("Network");
		panNet.add(lblNet, BorderLayout.NORTH);

		listNet = new ChooserList();
		listNet.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent lse)
			{
				networkSelected();
			}
		});
		panNet.add(listNet.getScroller());

		netEd = new ChooserListEditor();
		netEd.setName("net");
		netEd.addActionListenerAdd(this);
		netEd.addActionListenerDel(this);
		netEd.addActionListenerEdit(this);
		panNet.add(netEd, BorderLayout.SOUTH);

		c.add(panNet);
	}

	/**
	 * The action listener that tracks the buttons for the list controls
	 * 
	 * @param arg0
	 *            information on the action event, specifically, which button
	 *            was clicked
	 */
	public void actionPerformed(ActionEvent arg0)
	{
		String name = ((JButton) arg0.getSource()).getName();
		if (name.equals("netadd"))
		{
			addNetwork();
		} else if (name.equals("netdel"))
		{
			delNetwork();
		} else if (name.equals("netedit"))
		{
			editNetwork();
		} else if (name.equals("servadd"))
		{
			addServer();
		} else if (name.equals("servdel"))
		{
			delServer();
		} else if (name.equals("servedit"))
		{
			editServer();
		}
		//Sort the lists
		listNet.sort();
		Collections.sort(networks);
	}
	/**
	 * When a network name is selected in the network list, this method displays
	 * the server information for that particular network.
	 *  
	 */
	private void networkSelected()
	{
		int i = listNet.getSelectedIndex();
		if (i >= 0)
		{
			listServ.clear();
			IRCNetworkInfo net = networks.get(i);
			int j = net.getServerList().size();
			for (int n = 0; n < j; n++)
			{
				IRCServerInfo serv = net.getServer(n);
				listServ.addItem(serv.getName() + "/" + serv.getPort());
			}
			listServ.setSelectedIndex(0);
		} else
		{
			listServ.clear();
		}
	}
	/**
	 * The server box is a panel that contains all of the controls for selecting
	 * and editing the list of servers for the currently selected IRC network
	 * 
	 * @param c
	 *            the Container to which to add the server box
	 */
	private void createServerBox(Container c)
	{
		JPanel panServ = new JPanel();
		panServ.setLayout(new BorderLayout());

		JLabel lblServ = new JLabel("Server");
		panServ.add(lblServ, BorderLayout.NORTH);

		listServ = new ChooserList();
		panServ.add(listServ.getScroller());

		servEd = new ChooserListEditor();
		servEd.setName("serv");
		servEd.addActionListenerAdd(this);
		servEd.addActionListenerDel(this);
		servEd.addActionListenerEdit(this);
		panServ.add(servEd, BorderLayout.SOUTH);

		c.add(panServ);
	}
	/**
	 * When a network name is typed into the network editor text box, and the
	 * "add" button is clicked, this method creates a new container for this
	 * network's server list.
	 *  
	 */
	private void addNetwork()
	{
		String name = netEd.getText();
		if (name != null)
		{
			IRCNetworkInfo net = new IRCNetworkInfo(name);
			networks.add(net);
			listNet.addItem(name);
			netEd.setText("");
		}
	}
	/**
	 * removes a network from the IRC network list
	 *  
	 */
	private void delNetwork()
	{
		int index = listNet.getSelectedIndex();
		if (index >= 0)
		{
			listNet.removeItem(index);
			networks.remove(index);
		}
	}
	/**
	 * when an IRC network name is selected in the network list, and a new name
	 * is typed in the network editor box, then this method replaces that
	 * network name with the new network name.
	 *  
	 */
	private void editNetwork()
	{
		int index = listNet.getSelectedIndex();
		String name = netEd.getText();
		if (name != null && index >= 0)
		{
			listNet.changeItem(index, name);
			 networks.get(index).setName(name);
			netEd.setText("");
		}
	}
	/**
	 * when an IRC network is selected in the network list, and a server address
	 * is typed into the server editor box, then this method adds a new server
	 * listing for the currently selected IRC network.
	 *  
	 */
	private void addServer()
	{
		int netIndex = listNet.getSelectedIndex();
		if (netIndex >= 0)
		{
			IRCNetworkInfo net =  networks.get(netIndex);
			String address = servEd.getText();
			if (address != null)
			{
				IRCServerInfo serv = new IRCServerInfo(address);
				net.addServer(serv);
				listServ.addItem(serv.getName() + "/" + serv.getPort());
				servEd.setText("");
			}
		}
	}
	/**
	 * when an IRC network is selected in the network list, and a server address
	 * is selected in the server list, then this method removes the currently
	 * selected server from the currently selected IRC network.
	 *  
	 */
	private void delServer()
	{
		int netIndex = listNet.getSelectedIndex();
		int servIndex = listServ.getSelectedIndex();
		if (netIndex >= 0 && servIndex >= 0)
		{
			IRCNetworkInfo net =  networks.get(netIndex);
			net.removeServer(servIndex);
		}
	}
	/**
	 * when an IRC network is selected in the network list, a server address is
	 * selected in the server list, and a new server address is typed in the
	 * server editor box, then this method replaces the currently selected
	 * server address for the currently selected network with the new server
	 * address.
	 *  
	 */
	private void editServer()
	{
		int netIndex = listNet.getSelectedIndex();
		int servIndex = listServ.getSelectedIndex();
		String name = servEd.getText();
		if (netIndex >= 0 && servIndex >= 0 && name != null)
		{
			IRCNetworkInfo net = networks.get(netIndex);
			IRCServerInfo serv = net.getServer(servIndex);
			serv.setName(name);
			listServ.changeItem(servIndex, name);
			servEd.setText("");
		}
	}
	/**
	 * The user box is a panel that holds the data entry controls for the user
	 * information, specifically desired nickname, user name, and real name. It
	 * also holds the control buttons for connecting to the selected server or
	 * exiting the program.
	 * 
	 * @param c
	 *            the container to which to add the user box.
	 */
	private void createUserBox(Container c)
	{
		JPanel panUser = new JPanel();
		panUser.setLayout(new BoxLayout(panUser, BoxLayout.Y_AXIS));

		txtNick = new LabeledTextField("Nickname", LabeledTextField.NORTH);
		txtUser = new LabeledTextField("User Name", LabeledTextField.NORTH);
		txtRealName = new LabeledTextField("Real Name", LabeledTextField.NORTH);
		butConnect = new JButton("Connect");
		butConnect.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				connect();
			}
		});
		butCancel = new JButton("Cancel");
		butCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				saveNetworkInfo();
				System.exit(1);
			}
		});
		panUser.add(txtNick);
		panUser.add(new JLabel(" "));
		panUser.add(txtUser);
		panUser.add(new JLabel(" "));
		panUser.add(txtRealName);
		panUser.add(new JLabel(" "));
		panUser.add(new JLabel(" "));
		JPanel butPan = new JPanel();
		butPan.setLayout(new BoxLayout(butPan, BoxLayout.X_AXIS));
		butPan.add(butConnect);
		butPan.add(butCancel);
		panUser.add(butPan);
		panUser.add(new JLabel(" "));
		c.add(panUser);
	}
	/**
	 * uhmm, duh, connect to the selected IRC network at the selected server
	 * address using the indicated nickname, user name, and real name.
	 *  
	 */
	private void connect()
	{
		int netIndex, servIndex;
		netIndex = listNet.getSelectedIndex();
		servIndex = listServ.getSelectedIndex();
		String nick = txtNick.getText();
		String user = txtUser.getText();
		String real = txtRealName.getText();
		if (nick.equals("") || user.equals("") || real.equals(""))
		{
			//TODO: enter the appropriate info
		} else
		{
			if (netIndex >= 0 && servIndex >= 0)
			{
				saveNetworkInfo();
				IRCNetworkInfo network = networks.get(netIndex);
				IRCServerInfo server = network.getServer(servIndex);
				//Open the IRC client window and connect to the server.
				new IRCClient(server.getName(), server.getPort(), nick, user,
						real, VERSION, new Duel());
				//get rid of the current window, not going to allow
				// reconnection
				this.dispose();
			} else
			{
				//TODO: select a network and server
			}
		}
	}
	/**
	 * loads the saved list of IRC networks from a Java Serialized file.
	 *  
	 */
	@SuppressWarnings("unchecked")
	private void getNetworkInfo()
	{
		try
		{
			ObjectInputStream in = new ObjectInputStream(this.getClass()
					.getResourceAsStream(SER_FILE));
			networks = (ArrayList<IRCNetworkInfo>) in.readObject();
		} catch (Exception e)
		{
			networks = new ArrayList<IRCNetworkInfo>();
		}
	}
	/**
	 * saves the list of IRC networks to a Java Serialized file.
	 *  
	 */
	private void saveNetworkInfo()
	{
		try
		{
			FileOutputStream fout = new FileOutputStream(SER_FILE);
			ObjectOutputStream out = new ObjectOutputStream(fout);
			out.writeObject(networks);
		} catch (Exception e)
		{
		}
	}

	/**
	 * empty
	 * 
	 * @param arg0
	 *            unused
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowActivated(WindowEvent arg0)
	{
	}
	/**
	 * empty
	 * 
	 * @param arg0
	 *            unused
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowClosed(WindowEvent arg0)
	{
	}
	/**
	 * when the current window is closed, ensure that the network information
	 * list is saved
	 * 
	 * @param arg0
	 *            unused
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent arg0)
	{
		saveNetworkInfo();
	}
	/**
	 * empty
	 * 
	 * @param arg0
	 *            unused
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowDeactivated(WindowEvent arg0)
	{
	}
	/**
	 * empty
	 * 
	 * @param arg0
	 *            unused
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowDeiconified(WindowEvent arg0)
	{
	}
	/**
	 * empty
	 * 
	 * @param arg0
	 *            unused
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowIconified(WindowEvent arg0)
	{
	}
	/**
	 * empty
	 * 
	 * @param arg0
	 *            unused
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowOpened(WindowEvent arg0)
	{
	}
	/**
	 * main program entry point
	 * 
	 * @param args
	 *            command line arguments, unused
	 */
	public static void main(String[] args)
	{
		new Connector();
	}
}