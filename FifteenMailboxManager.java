//******************************************************************************
//
// File:    FifteenMailboxManager.java
// Package: ---
// Unit:    Class MailboxManager
//
//******************************************************************************

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.HashMap;

/**
 * Class FifteenMailboxManager provides the server program's mailbox manager in the
 * Network Go Game. The mailbox manager keeps track of all view proxy objects,
 * reads all incoming datagrams, and forwards each datagram to the appropriate
 * view proxy.
 *
 * @author  Alan Kaminsky
 * @author  Marko Galesic
 * @version 28-Sep-2013
 */
public class FifteenMailboxManager
{

	// Hidden data members.

	private DatagramSocket mailbox;

	private HashMap<SocketAddress,FifteenViewProxy> proxyMap = new HashMap<SocketAddress,FifteenViewProxy>();

	private byte[] payload = new byte [128]; /* CAREFUL OF BUFFER LENGTH */

	private FifteenSessionManager sessionManager = new FifteenSessionManager();

	// Exported constructors
	/**
	 * Construct a new mailbox manager.
	 *
	 * @param  mailbox  Mailbox from which to read datagrams.
	 */
	public FifteenMailboxManager(DatagramSocket mailbox)
	{
		this.mailbox = mailbox;
	}

	// Exported operations
	/**
	 * Receive and process the next datagram.
	 *
	 * @exception  IOException Thrown if an I/O error occurred.
	 */
	public void receiveMessage() throws IOException
	{
		DatagramPacket packet = new DatagramPacket (payload, payload.length);	
		mailbox.receive (packet);
		SocketAddress clientAddress = packet.getSocketAddress();
		FifteenViewProxy proxy = proxyMap.get (clientAddress);
		if (proxy == null)
		{
			proxy = new FifteenViewProxy (mailbox, clientAddress);
			proxy.setViewListener (sessionManager);
			proxyMap.put (clientAddress, proxy);
		}
		if (proxy.process (packet)) // Returns true to discard view proxy
		{
			proxyMap.remove (clientAddress);
		}
	}
}
