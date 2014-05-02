//******************************************************************************
//
// File:    FifteenModelServer.java
// Package: ---
// Unit:    Class FifteenModelServer
//
//******************************************************************************

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetSocketAddress;
import java.net.DatagramSocket;

/**
 * Class FifteenModelServer is the server side functionality of the Fifteen game.
 *
 * @author  Marko Galesic
 * @version 21-Apr-2014
 */

public class FifteenServer
{
	public static void main(String[] args) throws Exception
	{
		if (args.length != 2)
		{
			System.err.println("Usage: java FifteenServer <serverhost> <serverport>");
		}
		String host = args[0];
		int port = Integer.parseInt (args[1]);

		DatagramSocket mailbox = new DatagramSocket(new InetSocketAddress (host, port));

		FifteenMailboxManager manager = new FifteenMailboxManager (mailbox);

		for (;;)
		{
			manager.receiveMessage();
		}
	}
}
