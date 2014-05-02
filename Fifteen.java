//******************************************************************************
//
// File:    Fifteen.java
// Package: ---
// Unit:    Class Fifteen
//
//******************************************************************************

import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.*;
import java.net.DatagramSocket;

/**
 * A class that starts a network game of 'Fifteen'. Initializes a connection to a
 * server, the game UI, a model proxy object, and a model clone object.
 * 
 * Usage: java Fifteen <I>playername</I> <I>host</I> <I>port</I>
 *
 * @author  Marko Galesic
 * @version 28-March-2014
 */
public class Fifteen
{
	/**
	 * Main method
	 */
	public static void main(String args[]) throws Exception
	{
		InetSocketAddress address = null;
		
		// Print usage message if incorrect number of parameters have been specified
		if (args.length != 5)
		{
			System.err.println("Usage: java Fifteen <playername> <clienthost> <clientport> <serverhost> <serverport>");
			System.exit(0);
		}

		// Get client-side params
		String playerName = args[0];
		int clientport = Integer.parseInt(args[2]);
		String clienthost = args[1];

		// Get server-side params
		int serverport = Integer.parseInt(args[4]);
		String serverhost = args[3];

		DatagramSocket mailbox = new DatagramSocket(new InetSocketAddress (clienthost, clientport));

		// Create the fifteen view		
		FifteenView view = new FifteenView (playerName);

		// Create the fifteen model proxys
		final FifteenModelProxy proxy = new FifteenModelProxy (mailbox, new InetSocketAddress (serverhost, serverport));
		
		// Set view and model listeners
		view.setViewListener (proxy);


		Runtime.getRuntime().addShutdownHook (new Thread()
		{
			public void run()
			{
				try { proxy.quit(); }
				catch (IOException exc) {}
			}
		});

		// Ping server to join a game, ready to join a game
		proxy.join (null, playerName);
	}
}
