package network;
import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;

/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 14, 2004
 */

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class SocketWrapper
{
    private Socket         connection;
    private PrintWriter    out;
    private BufferedReader in;

    /**
     * 
     * @param listen
     */
    public SocketWrapper (ServerSocket listen)
    {
        try
        {
            System.out.println ("Waiting for client connection");
            connection = listen.accept ();
            out = new PrintWriter (connection.getOutputStream (), true);
            in = new BufferedReader (new InputStreamReader (connection
                    .getInputStream ()));
        }
        catch (IOException e)
        {
            System.err.println ("Server Exception: " + e.getMessage ());
            e.printStackTrace ();
        }
    }
    public SocketWrapper (String address)
    {
        this (address, 1337);
    }
    public SocketWrapper (String address, int port)
    {
        try
        {
            connection = new Socket (address, port);
            out = new PrintWriter (connection.getOutputStream (), true);
            in = new BufferedReader (new InputStreamReader (connection
                    .getInputStream ()));
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog (null, "Host " + address + ":" + port
                    + " = " + e.getMessage ());
        }
    }
    public void send (String data)
    {
        if (connection != null)
            out.println (data);
    }
    public String read ()
    {
        String ret = "";
        try
        {
            if (connection != null)
                ret = in.readLine ();
        }
        catch (IOException e)
        {
            close ();
        }
        return ret;
    }
    public boolean isConnected ()
    {
        return connection != null && connection.isConnected ();
    }
    public boolean isClosed ()
    {
        return connection != null && connection.isClosed ();
    }
    public void close ()
    {
        try
        {
            connection.close ();
        }
        catch (IOException e)
        {
            System.err
                    .println ("An exception occured while trying to close the connection.");
            e.printStackTrace ();
        }
    }
    public void bindRnd ()
    {
        try
        {
            connection.bind (new InetSocketAddress (0));
        }
        catch (IOException e)
        {
            System.out.println (e.getMessage ());
            e.printStackTrace ();
        }
    }
}