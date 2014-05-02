//******************************************************************************
//
// File:    FifteenModelProxy.java
// Package: ---
// Unit:    Class FifteenModelProxy
//
//******************************************************************************

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.InetSocketAddress;

/**
 * Class FifteenModelProxy provides an network proxy for the model server.
 * Implements the server and client inter-protocol.
 *
 * @author  Marko Galesic
 * @version 28-Mar-2014
 */
public class FifteenModelProxy implements FifteenViewListener
{
	// Hidden data members
	private DatagramSocket _mailbox;
	private SocketAddress _destination;
	private FifteenModelListener _modelListener;
	
	/**
	 * Class ReaderThread receives messages from the network, decodes them, and
	 * invokes the proper methods to process them.
	 *
	 * @author  Alan Kaminsky
	 * @author  Marko Galesic
	 * @version 03-04-2014
	 */
	private class ReaderThread extends Thread
	{
		public void run()
		{	
			byte[] payload = new byte [128]; /* CAREFUL OF BUFFER SIZE! */
			try
			{			
				for (;;)
				{
					DatagramPacket packet = new DatagramPacket (payload, payload.length);
					_mailbox.receive (packet);
					DataInputStream in = new DataInputStream(new ByteArrayInputStream(payload, 0, packet.getLength()));
					char c;
					int id, score;
					String[] messageParts;
					byte b = in.readByte();
					switch (b)
					{
						// id <id> message
						case 'i':
							id = in.readInt();
							_modelListener.localId(id);
							break;
						// name <id> <name> message
						case 'n':
							messageParts = in.readUTF().split(" ");
							id = Integer.parseInt(messageParts[1]);
							String playerName = messageParts[2];
							_modelListener.playerIdAndName(id, playerName);
							break;
						// digits <ddddddddd> message
						case 'd':
							char[] buttonStates = new char[10];
							for (int state = 0; state < 9; state++)
							{
								buttonStates[state] = (char)in.readByte();
							}
							_modelListener.buttonStates(buttonStates);
							break;
						// score <id> <score> message
						case 's':
							messageParts = in.readUTF().split(" ");
							id = Integer.parseInt(messageParts[1]);
							score = Integer.parseInt(messageParts[2]);
							_modelListener.scoreReport(id, score);
							break;
						// turn <id> message
						case 't':
							id = in.readInt();
							_modelListener.turnId(id);
							break;
						// win <id> message
						case 'w':
							id = in.readInt();
							_modelListener.winId(id);
							break;
						// quit message
						case 'q':
							_modelListener.quit();
							break;
						default:
							System.err.println ("Bad message");
							break;
					}
				}
			}
			catch(IOException e){}
			finally{_mailbox.close();}
		}
	}

	/**
	 * Construct a new FifteenModelProxy
	 *
	 * @param  socket  Socket.
	 * @exception  IOException Thrown if an I/O error occurred.
	 */
	public FifteenModelProxy (DatagramSocket mailbox, SocketAddress destination) throws IOException
	{
		_mailbox = mailbox;
		_destination = destination;
	}

	/**
	 * Set the model listener object for this model proxy.
	 *
	 * @param  modelListener  Model listener.
	 */
	public void setModelListener (FifteenModelListener modelListener) throws IOException
	{
		_modelListener = modelListener;
		new ReaderThread().start();
	}

	/**
	 * Ask to join with specified playerName
	 *
	 * @param playerName player's name that wants to join
	 */
	public void join(FifteenViewProxy proxy, String playerName) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeByte ('j');
		out.writeUTF(playerName);
		out.close();
		byte[] payload = baos.toByteArray();
		_mailbox.send(new DatagramPacket (payload, payload.length, _destination));
	}

	/**
	 * Report when a digit button has been pressed
	 *
	 * @param i number the button represents
	 */
	public void digitButtonPressed(int i) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeByte ('d');
		out.writeInt(i);
		out.close();
		byte[] payload = baos.toByteArray();
		_mailbox.send(new DatagramPacket (payload, payload.length, _destination));
	}

	/**
	 * Report when the new game button has been pressed
	 */
	public void newGamePressed() throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeByte ('n');
		out.close();
		byte[] payload = baos.toByteArray();
		_mailbox.send(new DatagramPacket (payload, payload.length, _destination));
	}

	/**
	 * Report that a user quit
	 */ 
	public void quit() throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream (baos);
		out.writeByte ('q');
		out.close();
		byte[] payload = baos.toByteArray();
		_mailbox.send(new DatagramPacket (payload, payload.length, _destination));
	}

	public static void main(String[] args) throws Exception
	{
		DatagramSocket mailbox = new DatagramSocket(new InetSocketAddress ("localhost", 5679));

		FifteenModelProxy proxy = new FifteenModelProxy (mailbox, new InetSocketAddress ("localhost", 5678));

		switch(args[0].charAt(0))
		{
			case 'j':
				proxy.join(null, args[0].substring(1, args[0].length()));
				break;
			case 'd':
				proxy.digitButtonPressed(Integer.parseInt(args[0].charAt(1) + ""));
				break;
			case 'n':
				proxy.newGamePressed();
				break;
			case 'q':
				proxy.quit();
				break;
		}
	}
}
