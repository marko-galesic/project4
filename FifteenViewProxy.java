//******************************************************************************
//
// File:    FifteenViewProxy.java
// Package: ---
// Unit:    Class FifteenViewProxy
//
//******************************************************************************

import java.net.DatagramPacket;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.*;
import java.util.Scanner;
import java.net.SocketAddress;
import java.net.DatagramSocket;

/**
 * Class FifteenViewProxy provides an network proxy for the view \ client program.
 * Implements the server and client inter-protocol.
 *
 * @author  Marko Galesic
 * @version 28-Mar-2014
 */
public class FifteenViewProxy implements FifteenModelListener
{
	// Hidden data members
	private DatagramSocket _mailbox;
	private SocketAddress _player1Address;
	private FifteenViewListener _viewListener;
	
	/**
	 * Class ReaderThread receives messages from the network, decodes them, and
	 * invokes the proper methods to process them.
	 *
	 * @author  Alan Kaminsky
	 * @author  Marko Galesic
	 * @version 03-04-2014
	 */
	public boolean process(DatagramPacket datagram) throws IOException
	{
		boolean discard = false;
		DataInputStream in = new DataInputStream(new ByteArrayInputStream (datagram.getData(), 0, datagram.getLength()));
		char c;
		String name;
		int d;
		String[] messageParts;
		char message = (char)in.readByte();
		switch (message)
		{
			// join message
			case 'j':
				name = in.readUTF();
				System.out.println(_player1Address + " -> j" + name);
				_viewListener.join(this, name);
				break;
			// digit message
			case 'd':
				d = in.readInt();
				System.out.println(_player1Address + " -> d" + d);
				_viewListener.digitButtonPressed(d);
				break;
			// newgame message
			case 'n':
				System.out.println(_player1Address + " -> n");
				_viewListener.newGamePressed();
				break;
			// quit message
			case 'q':
				System.out.println(_player1Address + " -> q");
				discard = true;
				_viewListener.quit();
				break;
			default:
				System.err.println ("Bad message");
				break;
		}
		return discard;
	}

	/**
	 * Construct a new FifteenModelProxy
	 *
	 * @param  socket  Socket.
	 */
	public FifteenViewProxy (DatagramSocket mailbox, SocketAddress player1Address)
	{
		_mailbox = mailbox;
		_player1Address = player1Address;
	}
	/**
	 * Set the view listener object for this view proxy.
	 *
	 * @param  viewListener  View listener.
	 */
	public void setViewListener (FifteenViewListener viewListener) throws IOException
	{
		_viewListener = viewListener;
	}

	/**
	 * Report the id of the local player
	 *
	 * @param id id of the local player
	 */
	public void localId(int id) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeByte('i');
		out.writeInt(id);
		out.close();
		byte[] payload = baos.toByteArray();
		_mailbox.send (new DatagramPacket (payload, payload.length, _player1Address));
		System.out.println(_player1Address + " <- i" + id);
	}

	/**
	 * Report a players id and name
	 *
	 * @param id a players id
	 * @param String playerName
	 */
	public void playerIdAndName(int id, String playerName) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeByte('n');
		out.writeUTF(id + " " + playerName);
		out.close();
		byte[] payload = baos.toByteArray();
		_mailbox.send (new DatagramPacket (payload, payload.length, _player1Address));
		System.out.println(_player1Address + " <- n" + id + " " + playerName);
	}

	/**
	 * Report button states
	 *
	 * @param buttonStates 	an array that represents whether
	 *			or not buttons 1-9 are depressed or
	 *			not
	 */
	public void buttonStates(char[] buttonStates) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeByte('d');
		System.out.print(_player1Address + " <- d");
		for(int b = 0; b < buttonStates.length; b++)
		{
			out.writeByte(buttonStates[b]);
			System.out.print(buttonStates[b]);
		}
		System.out.println();
		out.writeByte('\n');
		out.close();
		byte[] payload = baos.toByteArray();
		_mailbox.send (new DatagramPacket (payload, payload.length, _player1Address));
	}

	/**
	 * Report score for an id
	 *
	 * @param id id of the player the score report is for
	 * @param score score of the player
	 */
	public void scoreReport(int id, int score) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeUTF("s" + id + " " + score + "\n");
		out.close();
		byte[] payload = baos.toByteArray();
		_mailbox.send (new DatagramPacket (payload, payload.length, _player1Address));
		System.out.println(_player1Address + " <- s" + id + " " + score);
	}

	/**
	 * Report the turn id
	 *
	 * @param id id of the player whose turn it is
	 */
	public void turnId(int id) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeByte('t');
		out.writeInt(id);
		out.close();
		byte[] payload = baos.toByteArray();
		_mailbox.send (new DatagramPacket (payload, payload.length, _player1Address));
		System.out.println(_player1Address + " <- t" + id);
	}

	/**
	 * Report a win.
	 *
	 * @param id id of the player who won
	 */
	public void winId(int id) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeByte('w');
		out.writeInt(id);
		out.close();
		byte[] payload = baos.toByteArray();
		_mailbox.send (new DatagramPacket (payload, payload.length, _player1Address));
		System.out.println(_player1Address + " <- w" + id);
	}

	/**
	 * Send a message to quit
	 */
	public void quit() throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeByte('q');
		out.close();
		byte[] payload = baos.toByteArray();
		_mailbox.send (new DatagramPacket (payload, payload.length, _player1Address));
		System.out.println(_player1Address + " <- q");
	}
}
