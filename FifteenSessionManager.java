//******************************************************************************
//
// File:    FifteenView.java
// Package: ---
// Unit:    Class FifteenView
//
//******************************************************************************

import java.util.*;
import java.io.IOException;

/**
 * Class FifteenSessionManager manages sessions the the server creates for Fifteen games
 *
 * @author  Marko Galesic
 * @version 21-Apr-2014
 */
public class FifteenSessionManager implements FifteenViewListener
{
	private Player currentPlayer1;
	private FifteenModel currentModel;
	private FifteenViewProxy currentProxy;
	
	/*
	 * FifteenSessionManager constructor
	 */
	public FifteenSessionManager()
	{
	}

	
	/**
	 * Handles when a player wants to join a session
	 *
	 * @param playerName player's name that wants to join
	 * @exception IOException Thrown if an I/O error occurred.
	 */
	public synchronized void join(FifteenViewProxy proxy, String name) throws IOException
	{
		if (currentPlayer1 == null)
		{
			// Create a player 1 instance for this game session
			currentPlayer1 = new Player(name);
			
			// Create a new model for this Fifteen game session
			currentModel = new FifteenModel(currentPlayer1);

			// Notify model to send commands to the player 1's proxy
			currentModel.addModelListener (proxy);
		}
		else
		{
			// Set the second player up in the model
			currentModel.addSecondPlayer(new Player(name));

			// Notify model to send commands to player 2's proxy
			currentModel.addModelListener (proxy);

			// Reset the player 1 referece; next time a request comes in, another game
			// will start
			currentPlayer1 = null;
		}
		
		// Tell the proxy to listen to the model
		proxy.setViewListener (currentModel);
	}

	/**
	 * Report when a digit button has been pressed
	 *
	 * @param i number the button represents
	 * @exception IOException Thrown if an I/O error occurred.
	 */
	public void digitButtonPressed(int i) throws IOException{}

	/**
	 * Report when the new game button has been pressed
	 * @exception IOException Thrown if an I/O error occurred.
	 */
	public void newGamePressed() throws IOException{}

	/**
	 * Report that a user quit
	 * @exception IOException Thrown if an I/O error occurred.
	 */
	public void quit() throws IOException{}
}
